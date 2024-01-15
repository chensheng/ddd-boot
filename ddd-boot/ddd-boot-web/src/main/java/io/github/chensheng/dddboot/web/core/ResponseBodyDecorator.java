package io.github.chensheng.dddboot.web.core;

import org.springframework.core.Ordered;

public interface ResponseBodyDecorator extends Ordered {
    /**
     *
     * @param body response body to decorate
     * @return new response body
     */
    Object decorate(Object body);
}
