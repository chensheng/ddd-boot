#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.example.valueobject;

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
