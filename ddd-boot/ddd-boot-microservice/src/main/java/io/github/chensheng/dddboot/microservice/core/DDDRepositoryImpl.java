package io.github.chensheng.dddboot.microservice.core;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import io.github.chensheng.dddboot.web.core.BizException;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class DDDRepositoryImpl<E extends DDDEntity, D extends IDataObject, C extends DDDConvertor<E, D, ?>, M extends BaseMapper<D>> implements DDDRepository<E> {
    @Autowired
    protected C convertor;

    @Autowired
    protected M mapper;

    @Override
    public Long save(E entity) throws BizException {
        D dataObject = convertor.toDataObject(entity);
        if(dataObject.getId() == null) {
            mapper.insert(dataObject);
            return dataObject.getId();
        }

        D existingDataObject = mapper.selectById(dataObject.getId());
        if(existingDataObject == null) {
            throw new BizException("记录不存在");
        }

        dataObject.beforeUpdate(existingDataObject);
        mapper.updateById(dataObject);
        return dataObject.getId();
    }

    @Override
    public E getById(Long id) throws BizException {
        if(id == null) {
            throw new BizException("记录不存在");
        }

        D dataObject = mapper.selectById(id);
        if(dataObject == null) {
            throw new BizException("记录不存在");
        }

        E entity = convertor.toEntity(dataObject);
        if(entity == null) {
            throw new BizException("记录不存在");
        }

        return entity;
    }

    @Override
    public int remove(E entity) throws BizException {
        if(entity.getId() == null) {
            throw new BizException("记录不存在");
        }

        return mapper.deleteById(entity.getId());
    }
}
