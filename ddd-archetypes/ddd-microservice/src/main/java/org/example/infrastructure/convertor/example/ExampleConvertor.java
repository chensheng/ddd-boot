package org.example.infrastructure.convertor.example;

import io.github.chensheng.dddboot.microservice.core.DDDConvertor;
import org.example.application.example.dto.result.ExampleResult;
import org.example.domain.example.entity.ExampleEntity;
import org.example.infrastructure.repository.example.database.dataobject.Example;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExampleConvertor extends DDDConvertor<ExampleEntity, Example, ExampleResult> {
}
