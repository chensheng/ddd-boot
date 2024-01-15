package io.github.chensheng.dddboot.microservice.core;

import io.github.chensheng.dddboot.web.core.BizException;

public interface DDDRepository<E extends DDDEntity> {
    Long save(E entity) throws BizException;

    E getById(Long id) throws BizException;

    int remove(E entity) throws BizException;
}
