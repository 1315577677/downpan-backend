package indi.zx.downpan.configure;

import io.minio.MinioClient;
import io.minio.MinioClient;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import lombok.Data;
import org.checkerframework.checker.units.qual.A;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
/**
 * @author xiang.zhang
 * @since CreateAt 2021-02-08 15:09
 */

@Data
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioConfig {

   //("endPoint是一个URL，域名，IPv4或者IPv6地址")
    private String endpoint;

   //("TCP/IP端口号")
    private int port;

   //("accessKey类似于用户ID，用于唯一标识你的账户")
    private String accessKey;

   //("secretKey是你账户的密码")
    private String secretKey;

   //("如果是true，则用的是https而不是http,默认值是true")
    private Boolean secure;


    @Bean("minioClient")
    public MinioClient getMinioClient() throws InvalidEndpointException, InvalidPortException {
        MinioClient minioClient = new MinioClient(endpoint, port, accessKey, secretKey,secure);
        return minioClient;
    }
}
