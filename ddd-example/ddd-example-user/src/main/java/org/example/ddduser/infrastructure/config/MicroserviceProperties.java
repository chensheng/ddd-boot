package org.example.ddduser.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "microservice")
public class MicroserviceProperties {
    private MicroserviceConfig workspace;

    @Data
    public static class MicroserviceConfig {
        private String url;

        private String accessKey;

        private String secretKey;
    }
}
