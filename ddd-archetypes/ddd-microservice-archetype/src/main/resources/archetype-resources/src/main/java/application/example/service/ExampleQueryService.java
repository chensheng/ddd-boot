#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.service;

import io.github.chensheng.dddboot.microservice.core.DDDQueryService;
import ${package}.application.example.dto.result.ExampleResult;

public interface ExampleQueryService extends DDDQueryService<ExampleResult> {
}
