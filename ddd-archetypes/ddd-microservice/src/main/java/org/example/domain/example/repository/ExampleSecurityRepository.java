package org.example.domain.example.repository;

import io.github.chensheng.dddboot.web.core.BizException;

public interface ExampleSecurityRepository {
    Long getLoginUser() throws BizException;

    Long getLoginUserQuietly();
}
