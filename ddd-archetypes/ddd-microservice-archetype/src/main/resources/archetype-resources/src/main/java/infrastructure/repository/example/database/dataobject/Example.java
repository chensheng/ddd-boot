#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infrastructure.repository.example.database.dataobject;

import io.github.chensheng.dddboot.microservice.core.DataObject;
import lombok.Data;
import ${package}.domain.example.valueobject.ExampleStatus;

@Data
public class Example extends DataObject {
    private String username;

    private String password;

    private ExampleStatus status;
}
