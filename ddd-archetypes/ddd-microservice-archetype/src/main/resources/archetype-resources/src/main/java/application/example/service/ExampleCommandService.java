#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.service;

import ${package}.application.example.dto.command.ExampleUpdatePasswordCommand;
import ${package}.application.example.dto.command.ExampleCreateCommand;

public interface ExampleCommandService {
    void create(ExampleCreateCommand command);

    void update(ExampleUpdatePasswordCommand command);

    void enable(Long id);

    void disable(Long id);

    void delete(Long id);
}
