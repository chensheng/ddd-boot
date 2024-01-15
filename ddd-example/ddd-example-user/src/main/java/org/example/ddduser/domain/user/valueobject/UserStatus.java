package org.example.ddduser.domain.user.valueobject;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum UserStatus {
    ENABLE(1),
    DISABLE(0);

    @EnumValue
    private int value;

    UserStatus(int value) {
        this.value = value;
    }
}
