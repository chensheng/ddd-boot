package org.example.domain.example.valueobject;

import com.baomidou.mybatisplus.annotation.EnumValue;

public enum ExampleStatus {
    ENABLE(1),
    DISABLE(0);

    @EnumValue
    private int value;

    ExampleStatus(int value) {
        this.value = value;
    }
}
