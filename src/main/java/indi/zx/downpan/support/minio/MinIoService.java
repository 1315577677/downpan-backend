package indi.zx.downpan.support.minio;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 15:01
 */


public interface MinIoService {

    boolean uploadFile(String bucket, String fileName, InputStream inputStream) throws Exception;

    void downLoadFile(String fileName, String bucket, OutputStream os);

    InputStream downLoadFile(String name,String bucket);
}
