package indi.zx.downpan;

import indi.zx.downpan.service.impl.FileServiceImpl;
import indi.zx.downpan.support.minio.MinIoService;
import io.minio.MinioClient;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

@SpringBootTest
class DownpanApplicationTests {

    @Autowired
    MinIoService minIoService;
    @Test
    void contextLoads() throws Exception {
//        FileUtils.forceDelete(new File())
       // FileServiceImpl.traverseFolder1(new File("D:\\Git"),new ArrayList<>());

    }

}
