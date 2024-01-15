package io.github.chensheng.dddboot.microservice.core;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface DDDQueryService<R> {
    Page<R> page(PageQuery query);

    List<R> list(ListQuery query);

    R detail(Long id);
}
