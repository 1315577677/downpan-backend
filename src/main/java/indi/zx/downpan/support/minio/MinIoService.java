package indi.zx.downpan.support.minio;

import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 15:01
 */


public interface MinIoService {
    /**
     *
     * @param bucket 上传到某个目录
     * @param fileName 上传文件名
     * @param inputStream 上传文件
     * @return
     */
    boolean uploadFile(String bucket, String fileName, InputStream inputStream) throws Exception;
}
