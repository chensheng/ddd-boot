package org.example.ddduser.infrastructure.repository.database.condition;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;
import org.example.ddduser.application.dto.result.UserProfile;
import org.example.ddduser.domain.user.valueobject.Gender;
import org.example.ddduser.domain.user.valueobject.UserStatus;

@Data
public class UserProfilePageCondition extends Page<UserProfile> {
    private String username;

    private UserStatus status;

    private Gender gender;

    private String nickNameLike;

    private Integer ageFrom;

    private Integer ageTo;
}
