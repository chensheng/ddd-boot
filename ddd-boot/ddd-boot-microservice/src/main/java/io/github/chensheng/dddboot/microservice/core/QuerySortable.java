package io.github.chensheng.dddboot.microservice.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 带有该注解的字段可参与排序
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface QuerySortable {
    /**
     * 用户未指定排序方式时，是否进行默认排序
     * <p>{@code OrderType.None}：不进行排序 </p>
     * <p>{@code OrderType.ASC}：升序 </p>
     * <p>{@code OrderType.ASC}：降序 </p>
     * @return
     */
    OrderType order() default OrderType.NONE;
}
