package org.example.application.example.dto.command;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class ExampleModifyPasswordCommand {
    @NotEmpty(message = "请输入旧密码")
    private String oldPassword;

    @NotEmpty(message = "请输入新密码")
    private String newPassword;
}
