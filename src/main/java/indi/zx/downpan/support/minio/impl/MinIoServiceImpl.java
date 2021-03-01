package indi.zx.downpan.support.minio.impl;

import indi.zx.downpan.exception.InternalServerExecption;
import indi.zx.downpan.support.minio.MinIoService;
import indi.zx.downpan.support.util.MessageUtil;
import indi.zx.downpan.support.util.SecurityUtil;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 15:01
 */

@Component
public class MinIoServiceImpl implements MinIoService {


    private final MinioClient minioClient;

    @Autowired
    public MinIoServiceImpl(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    @Override
    public boolean uploadFile(String bucket, String fileName, InputStream inputStream) throws Exception {
        if (!minioClient.bucketExists(bucket)) {
            minioClient.makeBucket(bucket);
        }
        try {
            minioClient.putObject(bucket, fileName, inputStream, new PutObjectOptions(inputStream.available(), PutObjectOptions.MAX_PART_SIZE));
        } finally {
            inputStream.close();
        }
        return true;
    }

    @Override
    public void downloadFile(String md5 ,String bucket, OutputStream os) {
        try(InputStream is = minioClient.getObject(bucket, md5)) {
            IOUtils.copyLarge(is,os);
        } catch (Exception e) {
            MessageUtil.parameter("资源未找到");
        }
    }
}

