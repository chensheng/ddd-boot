package org.example.ddduser.infrastructure.repository;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import org.example.ddduser.domain.repository.SecurityRepository;
import org.example.ddduser.infrastructure.common.constants.BizCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class SecurityRepositoryImpl implements SecurityRepository {
    @Override
    public Long findLoginUser() {
        HttpServletRequest request = getRequest();
        if(request == null) {
            throw new BizException(BizCode.AUTH_ERROR.name(), "用户未登录");
        }

        String userId = request.getHeader("User-ID");
        if(TextUtil.isBlank(userId)) {
            throw new BizException(BizCode.AUTH_ERROR.name(), "用户未登录");
        }

        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new BizException(BizCode.AUTH_ERROR.name(), "用户未登录");
        }
    }

    @Override
    public Long findLoginUserQuietly() {
        HttpServletRequest request = getRequest();
        if(request == null) {
            return null;
        }

        String userId = request.getHeader("User-ID");
        if(TextUtil.isBlank(userId)) {
            return null;
        }

        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }
}
