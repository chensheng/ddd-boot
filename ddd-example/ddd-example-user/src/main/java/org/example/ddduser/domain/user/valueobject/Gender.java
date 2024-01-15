package org.example.ddduser.domain.user.valueobject;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum Gender {
    FEMALE(0),
    MALE(1),
    UNKNOWN(-1);

    @EnumValue
    private int value;

    Gender(int value) {
        this.value = value;
    }
}
