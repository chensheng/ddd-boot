package io.github.chensheng.dddboot.openfeign.config;

import io.github.chensheng.dddboot.openfeign.annotation.EnableFeignClients;
import io.github.chensheng.dddboot.openfeign.httpclient.ApacheHcFeignClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("openfeign.base-package")
@EnableConfigurationProperties(OpenFeignProperties.class)
@EnableFeignClients
public class OpenFeignAutoConfiguration {
    public static final String DEFAULT_FEIGN_CLIENT_BEAN_NAME = "defaultFeignClient";

    @Configuration
    @ConditionalOnMissingBean(name = {DEFAULT_FEIGN_CLIENT_BEAN_NAME})
    public static class ApacheHttpClientConfig {

        @Bean(DEFAULT_FEIGN_CLIENT_BEAN_NAME)
        public ApacheHcFeignClient apacheHcFeignClient(OpenFeignProperties properties) {
            return new ApacheHcFeignClient(properties);
        }

    }
}
