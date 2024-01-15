package io.github.chensheng.dddboot.excel.annotation;

import io.github.chensheng.dddboot.excel.core.CellValueType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelCell {
    int index();

    String name() default "";

    String format() default "";

    CellValueType type() default CellValueType.AUTO;
}
