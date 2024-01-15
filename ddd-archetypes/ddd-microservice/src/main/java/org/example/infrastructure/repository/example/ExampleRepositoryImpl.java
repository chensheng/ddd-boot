package org.example.infrastructure.repository.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.github.chensheng.dddboot.microservice.core.DDDRepositoryImpl;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.example.domain.example.entity.ExampleEntity;
import org.example.domain.example.repository.ExampleRepository;
import org.example.infrastructure.convertor.example.ExampleConvertor;
import org.example.infrastructure.repository.example.database.ExampleMapper;
import org.example.infrastructure.repository.example.database.dataobject.Example;
import org.springframework.stereotype.Component;

@Component
public class ExampleRepositoryImpl extends DDDRepositoryImpl<ExampleEntity, Example, ExampleConvertor, ExampleMapper> implements ExampleRepository {
    @Override
    public ExampleEntity getByUsername(String username) {
        if(TextUtil.isBlank(username)) {
            return null;
        }

        Example example = mapper.selectOne(new QueryWrapper<Example>().lambda().eq(Example::getUsername, username));
        return convertor.toEntity(example);
    }
}
