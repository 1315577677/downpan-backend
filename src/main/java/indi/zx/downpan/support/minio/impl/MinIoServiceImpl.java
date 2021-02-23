package indi.zx.downpan.support.minio.impl;

import indi.zx.downpan.support.minio.MinIoService;
import io.minio.MinioClient;
import io.minio.PutObjectOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;

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
        if(!minioClient.bucketExists(bucket)){
            minioClient.makeBucket(bucket);
        };
        minioClient.putObject(bucket,fileName,inputStream,new PutObjectOptions(inputStream.available(),PutObjectOptions.MAX_PART_SIZE));
        return true;
    }
}

