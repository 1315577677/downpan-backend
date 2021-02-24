package indi.zx.downpan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
public class DownpanApplication {

    public static void main(String[] args) {
        SpringApplication.run(DownpanApplication.class, args);
    }

}
