package io.github.chensheng.dddboot.web.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ResponseBodyDecorateCenter {
    private Log logger = LogFactory.getLog(getClass());

    private ApplicationContext applicationContext;

    public ResponseBodyDecorateCenter(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Object doDecorate(Object body) {
        if (body == null) {
            return null;
        }

        Collection<ResponseBodyDecorator> decorators = getDecorators();
        if (CollectionUtils.isEmpty(decorators)) {
            return body;
        }

        for (ResponseBodyDecorator decorator : decorators) {
            try {
                body = decorator.decorate(body);
            } catch (Exception e) {
                logger.error("Fail to decorate response body", e);
            }
        }
        return body;
    }

    private Collection<ResponseBodyDecorator> getDecorators() {
        try {
            Map<String, ResponseBodyDecorator> decoratorMap = applicationContext.getBeansOfType(ResponseBodyDecorator.class);
            if(decoratorMap == null || decoratorMap.size() == 0) {
                return null;
            }

            Collection<ResponseBodyDecorator> decorators = decoratorMap.values();
            List<ResponseBodyDecorator> sortedDecorators = new ArrayList<ResponseBodyDecorator>(decorators);
            sortedDecorators.sort((o1, o2) -> {
                if (o1.getOrder() < o2.getOrder()) {
                    return -1;
                } else if (o1.getOrder() > o2.getOrder()) {
                    return 1;
                } else {
                    return 0;
                }
            });
            return sortedDecorators;
        } catch (BeansException e) {
            logger.error("Fail to get response body decorator", e);
        }
        return null;
    }
}
