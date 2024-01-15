#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.dto.result;

import lombok.Data;

@Data
public class ExampleResult {
    private Long id;

    private String username;
}
