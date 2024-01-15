#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.repository.example;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import ${package}.domain.example.repository.ExampleSecurityRepository;
import ${package}.infrastructure.common.constants.ExampleBizCode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Component
public class ExampleSecurityRepositoryImpl implements ExampleSecurityRepository {
    //dummy
    @Override
    public Long getLoginUser() throws BizException {
        HttpServletRequest request = getRequest();
        if(request == null) {
            throw new BizException(ExampleBizCode.AUTH_ERROR.name(), "用户未登录");
        }

        String userId = request.getHeader("User-ID");
        if(TextUtil.isBlank(userId)) {
            throw new BizException(ExampleBizCode.AUTH_ERROR.name(), "用户未登录");
        }

        try {
            return Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new BizException(ExampleBizCode.AUTH_ERROR.name(), "用户未登录");
        }
    }

    @Override
    public Long getLoginUserQuietly() {
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

    private HttpServletRequest getRequest() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if(servletRequestAttributes == null) {
            return null;
        }
        return servletRequestAttributes.getRequest();
    }
}
