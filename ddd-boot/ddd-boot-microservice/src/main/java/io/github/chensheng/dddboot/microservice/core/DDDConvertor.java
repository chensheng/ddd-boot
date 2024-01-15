package io.github.chensheng.dddboot.microservice.core;

public interface DDDConvertor<E extends DDDEntity, D extends IDataObject, R> {
    E toEntity(D dataObject);

    D toDataObject(E entity);

    R toResult(E entity);

    R toResult(D dataObject);
}
