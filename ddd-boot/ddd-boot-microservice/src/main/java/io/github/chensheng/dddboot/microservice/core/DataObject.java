package io.github.chensheng.dddboot.microservice.core;

import com.baomidou.mybatisplus.annotation.*;

import java.util.Date;

public abstract class DataObject implements IDataObject {
    @TableId(type = IdType.AUTO)
    private Long id;

    @Version
    private Long version;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.UPDATE)
    private Long updateUser;

    @TableField(fill = FieldFill.UPDATE)
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public Long getCreateUser() {
        return createUser;
    }

    public void setCreateUser(Long createUser) {
        this.createUser = createUser;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(Long updateUser) {
        this.updateUser = updateUser;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public void beforeUpdate(IDataObject original) {
        if(original == null || !(original instanceof DataObject)) {
            return;
        }

        DataObject old = (DataObject) original;

        this.createTime = old.getCreateTime();
        this.createUser = old.getCreateUser();
        this.version = old.getVersion();
    }
}
