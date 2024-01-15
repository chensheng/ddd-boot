package io.github.chensheng.dddboot.openfeign.annotation;

import io.github.chensheng.dddboot.openfeign.core.FeignClientRegistrar;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(FeignClientRegistrar.class)
public @interface EnableFeignClients {
}
