package io.github.chensheng.dddboot.excel.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelSheet {
    int index() default -1;

    String name() default "";

    int dataRowStartIndex() default 0;

    boolean writeHeader() default  true;
}
