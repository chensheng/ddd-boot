#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example.repository;

import io.github.chensheng.dddboot.web.core.BizException;

public interface ExampleSecurityRepository {
    Long getLoginUser() throws BizException;

    Long getLoginUserQuietly();
}
