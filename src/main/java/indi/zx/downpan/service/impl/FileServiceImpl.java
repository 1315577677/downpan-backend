package indi.zx.downpan.service.impl;

import indi.zx.downpan.entity.FileEntity;
import indi.zx.downpan.repository.FileRepository;
import indi.zx.downpan.service.FileService;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-20 15:46
 */
@Service
public class FileServiceImpl implements FileService {

    private final FileRepository fileRepository;
    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void upload(MultipartFile[] files) {
        List<FileEntity> fileEntitys = new LinkedList<>();

        for (MultipartFile file : files) {
            FileEntity fileEntity = new FileEntity();
            try {
                fileEntity.setMD5(DigestUtils.md5Hex(file.getInputStream()));
            } catch (IOException e) {
                MessageUtil.parameter("md5提取失败");
            }
            fileEntity.setFileName(file.getName());
            fileEntity.setIsDelete(false);
            fileEntity.setRealPath(SecurityUtil.getCurrentUsernameFromContext() + "/" + fileEntity.getMD5());
            fileEntity.setVirtualPath("");
            try {

                file.transferTo(new File("D:\\work\\testFolder\\" + file.getOriginalFilename()));
            } catch (IOException e) {
                MessageUtil.parameter("上传失败");
            }
            fileEntitys.add(fileEntity);
        }

        fileRepository.saveAll(fileEntitys);

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
}
