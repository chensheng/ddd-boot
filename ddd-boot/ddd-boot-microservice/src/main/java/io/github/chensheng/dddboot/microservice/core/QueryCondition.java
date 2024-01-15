package io.github.chensheng.dddboot.microservice.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 查询条件
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QueryCondition {
    /**
     * 条件列名；默认使用属性名
     * @return
     */
    String column() default "";

    /**
     * 条件匹配规则；默认为“等于”
     * @return
     */
    ConditionOperator operator() default ConditionOperator.eq;

    /**
     * 是否忽略该查询条件；默认为false
     * @return
     */
    boolean ignore() default false;

    /**
     * 是否允许空值；默认为false，当条件值为空时（空字符串、null），该查询条件不生效。
     * @return
     */
    boolean allowEmpty() default false;
}
