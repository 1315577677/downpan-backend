package indi.zx.downpan.service.impl;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.configure.Properties;
import indi.zx.downpan.entity.FileEntity;
import indi.zx.downpan.entity.UserEntity;
import indi.zx.downpan.repository.FileRepository;
import indi.zx.downpan.repository.UserRepository;
import indi.zx.downpan.service.FileService;
import indi.zx.downpan.support.Compressor.Archiver;
import indi.zx.downpan.support.Compressor.MyZip;
import indi.zx.downpan.support.minio.MinIoService;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.utils.IOUtils;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 15:46
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    private final Properties properties;
    private final Tika tika = new Tika();
    private final UserRepository userRepository;
    private final Lock lock = new ReentrantLock();
    private final MinIoService minIoService;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository, Properties properties, UserRepository userRepository, MinIoService minIoService) {
        this.fileRepository = fileRepository;
        this.properties = properties;
        this.userRepository = userRepository;
        this.minIoService = minIoService;
    }

    @Override
    public void upload(MultipartFile files, String parent) {
        String username = SecurityUtil.getCurrentUsername();
        FileEntity fileEntity = new FileEntity();
        try {
            lock.lock();
            UserEntity user = userRepository.findUserEntityByUsername(username);
            user.setUsed(user.getUsed() + files.getSize());
            userRepository.save(user);
        } finally {
            lock.unlock();
        }
        try {
            fileEntity.setType(getType(files.getInputStream()));
            fileEntity.setMD5(DigestUtils.md5Hex(files.getInputStream()));
            List<FileEntity> entity = fileRepository.findFileEntitysByMD5(fileEntity.getMD5())
                    .stream()
                    .filter(e -> !e.getIsDelete())
                    .collect(Collectors.toList());

            if (entity.size() == 0) {
                minIoService.uploadFile(username, fileEntity.getMD5(), files.getInputStream());
            }
        } catch (Exception e) {
            MessageUtil.parameter("上传失败:" + files.getOriginalFilename());
        }
        fileEntity.setSize(files.getSize());
        fileEntity.setName(files.getOriginalFilename());
        fileEntity.setIsDelete(false);
        fileEntity.setUrl(properties.getFileServerRootUrl() + username + "/" + fileEntity.getMD5());
        fileEntity.setIsDir(false);
        fileEntity.setParent(parent);
        fileEntity.setCreateUser(username);
        fileEntity.setUpdateUser(username);
        fileRepository.save(fileEntity);


    }

    private String getType(InputStream inputStream) throws Exception {
        String detect = tika.detect(inputStream);
        return Arrays.stream(GlobalConstants.FileType.values())
                .filter(e -> detect.contains(e.getType()))
                .findAny()
                .orElse(GlobalConstants.FileType.NONE)
                .getViewType();
    }

    public void getFile(String id, String username, HttpServletResponse response) {
        try {
            minIoService.downLoadFile(id, username, response.getOutputStream());
        } catch (IOException e) {
            MessageUtil.parameter(e.getMessage());
        }
    }

    public void deleteFiles(String ids) {
        List<String> iterator = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        Iterable<FileEntity> allById = fileRepository.findAllById(iterator);
        List<FileEntity> result = new ArrayList<>();
        allById.forEach(item -> {
            if (item.getIsDir()) {
                String root = item.getParent() + "/" + item.getName();
                String username = SecurityUtil.getCurrentUsername();
                List<FileEntity> entities = fileRepository.findFileEntitysByCreateUser(username)
                        .stream().filter(e -> e.getParent().contains(root))
                        .collect(Collectors.toList());
                entities.forEach(entity -> entity.setIsDelete(true));
                result.addAll(entities);
            }
            item.setIsDelete(true);
            result.add(item);
        });
        fileRepository.saveAll(result);
        UserEntity user = userRepository.findUserEntityByUsername(SecurityUtil.getCurrentUsername());
        user.setUsed(user.getUsed() - result.stream().mapToLong(FileEntity::getSize).sum());
        userRepository.save(user);
    }

    public void update(String id, String name) {
        Optional<FileEntity> byId = fileRepository.findById(id);
        FileEntity fileEntity = byId.get();
        fileEntity.setName(name);
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getData(String dir, String orderBy) {
        String username = SecurityUtil.getCurrentUsername();
        List<FileEntity> result = new LinkedList<>();
        List<FileEntity> collect = fileRepository.findFileEntitysByCreateUserAndParent(username, dir)
                .stream()
                .filter(fileEntity -> !fileEntity.getIsDelete())
                .collect(Collectors.toList());
        if ("createdTime".equals(orderBy)) {
            result.addAll(collect.stream()
                    .filter(FileEntity::getIsDir)
                    .sorted(Comparator.comparing(FileEntity::getUpdateTime))
                    .collect(Collectors.toList()));
            result.addAll(collect.stream()
                    .filter(entity -> !entity.getIsDir())
                    .sorted(Comparator.comparing(FileEntity::getUpdateTime))
                    .collect(Collectors.toList()));
        } else {
            result.addAll(collect.stream()
                    .filter(FileEntity::getIsDir)
                    .sorted(Comparator.comparing(FileEntity::getName))
                    .collect(Collectors.toList()));
            result.addAll(collect.stream()
                    .filter(entity -> !entity.getIsDir())
                    .sorted(Comparator.comparing(FileEntity::getName))
                    .collect(Collectors.toList()));
        }

        return result;
    }

    public void createDir(String parent, String name) {
        FileEntity fileEntity = new FileEntity();
        fileEntity.setIsDir(true);
        fileEntity.setType(GlobalConstants.FileType.DIR.getViewType());
        fileEntity.setName(name);
        fileEntity.setCreateUser(SecurityUtil.getCurrentUsername());
        fileEntity.setIsDelete(false);
        fileEntity.setParent(parent);
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> findByName(String name) {
        return fileRepository.findAll((Specification<FileEntity>) (root, query, criteriaBuilder) -> {
            Predicate parkFullNamePredicate = criteriaBuilder.like(root.get("name").as(String.class), "%" + name + "%");
            query.where(criteriaBuilder.and(parkFullNamePredicate));
            query.orderBy(criteriaBuilder.asc(root.get("updateTime").as(Date.class)));
            return query.getRestriction();
        })
                .stream()
                .filter(entity -> !entity.getIsDir())
                .filter(entity -> !entity.getIsDelete())
                .collect(Collectors.toList());
    }

    public void getBackFile() {
        fileRepository.updateFileStatusByCreateUser(SecurityUtil.getCurrentUsername());
    }

    public void unzip(String parent, String name) {
        FileEntity fileEntity = fileRepository.findFileEntityByParentAndName(parent, name)
                .stream()
                .filter(e -> !e.getIsDelete())
                .findAny()
                .orElse(new FileEntity());
        String md5 = fileEntity.getMD5();
        String username = SecurityUtil.getCurrentUsername();
        try {
            Archiver archiver = new MyZip();
            String destPath = System.getProperties().getProperty("user.home") + File.separator;
            String fileName = fileEntity.getName();
            String substring = fileName.substring(0, fileName.lastIndexOf("."));
            IOUtils.copy(minIoService.downLoadFile(md5, username), new FileOutputStream(destPath + fileName));
            archiver.doUnArchiver(new File(destPath + fileName), destPath, null);
            List<FileEntity> fileEntities = new ArrayList<>();
            uploadAndSaveInfo(destPath, destPath, parent, new File(destPath + substring), fileEntities);
            FileEntity file = new FileEntity();
            file.setType(GlobalConstants.FileType.DIR.getViewType());
            file.setIsDir(true);
            file.setParent(parent);
            file.setName(substring);
            file.setCreateUser(SecurityUtil.getCurrentUsername());
            fileEntities.add(file);
            fileRepository.saveAll(fileEntities);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.parameter("解压失败:" + e.getMessage());
        }
    }

    private void uploadAndSaveInfo(String root, String roote, String parent, File file, List<FileEntity> fileEntities) throws Exception {
        File[] list = file.listFiles();
        String username = SecurityUtil.getCurrentUsername();
        for (File item : list) {
            FileEntity fileEntity = new FileEntity();
            if (item.isDirectory()) {
                roote = roote + item.getParent().substring(file.getParent().indexOf(root) + root.length() + 1);
                uploadAndSaveInfo(root, roote, parent, item, fileEntities);
                fileEntity.setIsDir(true);
                fileEntity.setType(GlobalConstants.FileType.DIR.getViewType());
            } else {
                fileEntity.setIsDir(false);
                try (FileInputStream fileInputStream = new FileInputStream(item)) {
                    fileEntity.setType(getType(fileInputStream));
                    fileEntity.setMD5(DigestUtils.md5Hex(fileInputStream));
                    fileEntity.setSize((long) fileInputStream.available());
                }
            }
            String p =  parent + "/" + item.getParent().substring(item.getParent().indexOf(root) + root.length()).replace("\\","/");
            fileEntity.setParent(p);
            fileEntity.setName(item.getName());
            fileEntity.setUpdateUser(username);
            fileEntity.setCreateUser(username);
            fileEntities.add(fileEntity);
        }
    }

}
