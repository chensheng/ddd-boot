#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.convertor.example;

import io.github.chensheng.dddboot.microservice.core.DDDConvertor;
import ${package}.application.example.dto.result.ExampleResult;
import ${package}.domain.example.entity.ExampleEntity;
import ${package}.infrastructure.repository.example.database.dataobject.Example;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ExampleConvertor extends DDDConvertor<ExampleEntity, Example, ExampleResult> {
}
