package org.example.ddduser.infrastructure.repository.microservice;

import io.github.chensheng.dddboot.microservice.core.ApplicationContextHolder;
import io.github.chensheng.dddboot.tools.text.MD5Util;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.example.ddduser.infrastructure.config.MicroserviceProperties;

public class WorkspaceSecurityInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate requestTemplate) {
        MicroserviceProperties microserviceProperties = ApplicationContextHolder.get().getBean(MicroserviceProperties.class);
        MicroserviceProperties.MicroserviceConfig config = microserviceProperties.getWorkspace();

        String accessKey = config.getAccessKey();
        String secretKey = config.getSecretKey();
        long timestamp = System.currentTimeMillis();
        String signature = generateSignature(secretKey, timestamp, requestTemplate);

        requestTemplate.header("Access-Key", accessKey);
        requestTemplate.header("Signature", signature);
        requestTemplate.header("Timestamp", String.valueOf(timestamp));
    }

    private String generateSignature(String secretKey, long timestamp, RequestTemplate requestTemplate) {
        //dummy
        return MD5Util.md5With32(secretKey + ":" + timestamp + ":" + requestTemplate.path());
    }
}
