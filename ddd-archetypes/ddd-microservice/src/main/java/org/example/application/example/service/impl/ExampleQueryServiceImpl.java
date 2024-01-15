package org.example.application.example.service.impl;

import io.github.chensheng.dddboot.microservice.core.DDDQueryServiceImpl;
import org.example.application.example.dto.result.ExampleResult;
import org.example.application.example.service.ExampleQueryService;
import org.example.domain.example.entity.ExampleEntity;
import org.example.infrastructure.convertor.example.ExampleConvertor;
import org.example.infrastructure.repository.example.database.ExampleMapper;
import org.example.infrastructure.repository.example.database.dataobject.Example;
import org.springframework.stereotype.Service;

@Service
public class ExampleQueryServiceImpl extends DDDQueryServiceImpl<ExampleEntity, Example, ExampleResult, ExampleConvertor, ExampleMapper> implements ExampleQueryService {
}
