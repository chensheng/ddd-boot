package io.github.chensheng.dddboot.web.core;

import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class CustomResponseBodyAdvice implements ResponseBodyAdvice<Object> {
    private ResponseBodyDecorateCenter responseBodyDecorateCenter;

    public CustomResponseBodyAdvice(ResponseBodyDecorateCenter responseBodyDecorateCenter) {
        this.responseBodyDecorateCenter = responseBodyDecorateCenter;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        Class<?> containingClass = returnType.getContainingClass();
        if(containingClass != null && containingClass == BasicErrorController.class) {
            return body;
        }

        IgnoreResponseWrapper ignoreResponseWrapper = returnType.getMethodAnnotation(IgnoreResponseWrapper.class);
        if (ignoreResponseWrapper != null) {
            return body;
        }

        if (selectedConverterType.isAssignableFrom(StringHttpMessageConverter.class)) {
            return body;
        }

        body = responseBodyDecorateCenter.doDecorate(body);

        if (body != null && Response.class.isAssignableFrom(body.getClass())) {
            return body;
        }

        CommonResponse bodyWrapper = CommonResponse.bizSuccess(body);
        return bodyWrapper;
    }
}
