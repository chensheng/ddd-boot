#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.service.impl;

import io.github.chensheng.dddboot.microservice.core.DDDQueryServiceImpl;
import ${package}.application.example.dto.result.ExampleResult;
import ${package}.application.example.service.ExampleQueryService;
import ${package}.domain.example.entity.ExampleEntity;
import ${package}.infrastructure.convertor.example.ExampleConvertor;
import ${package}.infrastructure.repository.example.database.ExampleMapper;
import ${package}.infrastructure.repository.example.database.dataobject.Example;
import org.springframework.stereotype.Service;

@Service
public class ExampleQueryServiceImpl extends DDDQueryServiceImpl<ExampleEntity, Example, ExampleResult, ExampleConvertor, ExampleMapper> implements ExampleQueryService {
}
