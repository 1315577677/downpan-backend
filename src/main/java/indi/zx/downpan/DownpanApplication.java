package indi.zx.downpan;

import ch.qos.logback.core.net.server.ServerRunner;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@EnableConfigurationProperties
@ConfigurationPropertiesScan
@SpringBootApplication
public class DownpanApplication implements CommandLineRunner {

    @Autowired
    SocketIOServer socketIOServer;
    public static void main(String[] args) {
        SpringApplication.run(DownpanApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        socketIOServer.start();
    }
}
