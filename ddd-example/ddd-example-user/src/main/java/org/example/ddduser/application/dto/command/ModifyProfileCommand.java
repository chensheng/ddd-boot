package org.example.ddduser.application.dto.command;

import lombok.Data;
import org.example.ddduser.domain.user.valueobject.Gender;

import javax.validation.constraints.NotEmpty;

@Data
public class ModifyProfileCommand {
    @NotEmpty(message = "请输入昵称")
    private String nickName;

    private String avatar;

    private Gender gender;

    private Integer age;
}
