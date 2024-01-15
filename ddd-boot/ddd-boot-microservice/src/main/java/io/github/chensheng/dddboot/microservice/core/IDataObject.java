package io.github.chensheng.dddboot.microservice.core;

public interface IDataObject {
    Long getId();

    void beforeUpdate(IDataObject original);
}
