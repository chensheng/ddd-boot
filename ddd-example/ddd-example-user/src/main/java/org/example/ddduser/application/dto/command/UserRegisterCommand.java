package org.example.ddduser.application.dto.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class UserRegisterCommand {
    @NotEmpty(message = "请输入用户名")
    private String username;

    @NotEmpty(message = "请输入密码")
    @Pattern(regexp = "^(?![A-Za-z0-9]+$)(?![a-z0-9\\W]+$)(?![A-Za-z\\W]+$)(?![A-Z0-9\\W]+$)[a-zA-Z0-9\\W]{8,}$", message = "密码必须由数字、字母、特殊字符_#@!组成，且不能少于8位！")
    private String password;
}
