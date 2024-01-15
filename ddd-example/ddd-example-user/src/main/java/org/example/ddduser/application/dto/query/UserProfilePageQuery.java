package org.example.ddduser.application.dto.query;

import io.github.chensheng.dddboot.microservice.core.PageQuery;
import lombok.Data;
import org.example.ddduser.domain.user.valueobject.Gender;
import org.example.ddduser.domain.user.valueobject.UserStatus;

@Data
public class UserProfilePageQuery extends PageQuery {
    private String username;

    private UserStatus status;

    private Gender gender;

    private String nickNameLike;

    private Integer ageFrom;

    private Integer ageTo;
}
