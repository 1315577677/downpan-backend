package indi.zx.downpan.service.impl;

import indi.zx.downpan.common.constants.GlobalConstants;
import indi.zx.downpan.entity.FileEntity;
import indi.zx.downpan.repository.FileRepository;
import indi.zx.downpan.service.FileService;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tika.Tika;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.activation.MimeType;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 15:46
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;

    private final Tika tika = new Tika();
    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void upload(MultipartFile[] files, String parent) {
        String username = SecurityUtil.getCurrentUsername();
        List<FileEntity> fileEntitys = new LinkedList<>();
        for (MultipartFile file : files) {
            FileEntity fileEntity = new FileEntity();
            try {
                fileEntity.setRealType(getType(file.getInputStream()));
                fileEntity.setMD5(DigestUtils.md5Hex(file.getInputStream()));
            } catch (Exception e) {
                continue;
            }
            FileEntity entity = fileRepository.findFileEntityByMD5(fileEntity.getMD5());
            if (entity == null){
                // 上传操作
            }
            fileEntity.setSize(file.getSize());
            fileEntity.setFileName(file.getOriginalFilename());
            fileEntity.setIsDelete(false);
            fileEntity.setRealPath( username + "/" + fileEntity.getMD5());
            fileEntity.setVirtualPath(parent+ "/"+ file.getOriginalFilename());
            fileEntity.setIsDir(false);
            fileEntity.setParent(parent.replace(".","/"));
            fileEntity.setCreateUser(username);
            fileEntity.setUpdateUser(username);
            try {

                file.transferTo(new File("D:\\work\\testFolder\\" + file.getOriginalFilename()));
            } catch (IOException e) {
                MessageUtil.parameter("上传失败");
            }
            fileEntitys.add(fileEntity);
        }

        fileRepository.saveAll(fileEntitys);

    }

    private String getType(InputStream inputStream) throws Exception {
        String detect = tika.detect(inputStream);
        return Arrays.stream(GlobalConstants.FileType.values())
                .filter(e -> detect.contains(e.getType()))
                .findAny()
                .orElse(GlobalConstants.FileType.NONE)
                .getType();
    }

    public void getFile(String id, HttpServletResponse response) {
        File file = new File("D:\\work\\testFolder\\" + id);
        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setHeader("Content-Disposition", "attachment; filename="+file.getName().replace(" ", "_"));
        try {
            FileInputStream fis = new FileInputStream(file);
            IOUtils.copyLarge(fis,response.getOutputStream());
        } catch (IOException e) {
            MessageUtil.parameter("资源读取出错");
        }
    }

    public void deleteFiles(String ids) {
        List<String> iterator = Arrays.stream(ids.split(",")).collect(Collectors.toList());
        Iterable<FileEntity> allById = fileRepository.findAllById(iterator);
        allById.forEach(fileEntity -> {
            fileEntity.setIsDelete(true);
        });
        fileRepository.saveAll(allById);
    }

    public void update(String id, String name) {
        Optional<FileEntity> byId = fileRepository.findById(id);
        FileEntity fileEntity = byId.get();
        fileEntity.setFileName(name);
        fileRepository.save(fileEntity);
    }

    public List<FileEntity> getData(String dir) {
        String username = SecurityUtil.getCurrentUsername();
        return fileRepository.findFileEntitysByCreateUserAndParent(username,dir)
                 .stream()
                 .filter(FileEntity::getIsDelete)
                 .collect(Collectors.toList());
    }
}
