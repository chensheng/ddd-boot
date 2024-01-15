#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.dto.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ExampleCreateCommand {
    @NotEmpty(message = "请输入用户名")
    private String username;

    @NotEmpty(message = "请输入密码")
    private String password;
}
