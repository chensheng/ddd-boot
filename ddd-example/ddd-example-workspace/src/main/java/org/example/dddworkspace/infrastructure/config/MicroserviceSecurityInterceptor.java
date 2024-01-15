package org.example.dddworkspace.infrastructure.config;

import io.github.chensheng.dddboot.tools.number.NumberUtil;
import io.github.chensheng.dddboot.tools.text.MD5Util;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MicroserviceSecurityInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestUri = request.getRequestURI();
        if(!requestUri.startsWith("/microservice/")) {
            return true;
        }

        String accessKey = request.getHeader("Access-Key");
        long timestamp = NumberUtil.toLong(request.getHeader("Timestamp"), 0l);
        String signature = request.getHeader("Signature");

        if(!"123".equals(accessKey)) {
            return false;
        }
        if(System.currentTimeMillis() - timestamp > 3 * 60 * 1000) {
            return false;
        }
        if(TextUtil.isBlank(signature)) {
            return false;
        }

        String secretKey = "456";
        String calculatedSignature = MD5Util.md5With32(secretKey + ":" + timestamp + ":" + requestUri);
        if(!signature.equals(calculatedSignature)) {
            return false;
        }

        return true;
    }
}
