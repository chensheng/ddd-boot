#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.application.example.dto.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class ExampleRegisterCommand {
    @NotEmpty(message = "请输入用户名")
    private String username;

    @NotEmpty(message = "请输入密码")
    @Pattern(regexp = "^(?![A-Za-z0-9]+${symbol_dollar})(?![a-z0-9${symbol_escape}${symbol_escape}W]+${symbol_dollar})(?![A-Za-z${symbol_escape}${symbol_escape}W]+${symbol_dollar})(?![A-Z0-9${symbol_escape}${symbol_escape}W]+${symbol_dollar})[a-zA-Z0-9${symbol_escape}${symbol_escape}W]{8,}${symbol_dollar}", message = "密码必须由数字、字母、特殊字符_${symbol_pound}@!组成，且不能少于8位！")
    private String password;
}
