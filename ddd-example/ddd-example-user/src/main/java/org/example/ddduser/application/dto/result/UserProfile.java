package org.example.ddduser.application.dto.result;

import lombok.Data;
import org.example.ddduser.domain.user.valueobject.Gender;

@Data
public class UserProfile {
    private Long id;

    private String username;

    private String nickName;

    private String avatar;

    private Integer age;

    private Gender gender;

    private String address;
}
