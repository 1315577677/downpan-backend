package indi.zx.downpan.configure;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("downpan")
@Setter
@Getter
public class Properties {
    private String fileServerRootUrl;
}
