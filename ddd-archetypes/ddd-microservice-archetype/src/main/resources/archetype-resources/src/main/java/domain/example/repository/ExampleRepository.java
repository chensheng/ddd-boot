#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example.repository;

import io.github.chensheng.dddboot.microservice.core.DDDRepository;
import ${package}.domain.example.entity.ExampleEntity;

public interface ExampleRepository extends DDDRepository<ExampleEntity> {
    ExampleEntity getByUsername(String username);
}
