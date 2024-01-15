package io.github.chensheng.dddboot.openfeign.core;

import io.github.chensheng.dddboot.tools.time.DateFormatUtil;
import feign.Request;
import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.Date;

public class DefaultFormEncoder implements Encoder {
    private static final String FORM_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (object == null) {
            return;
        }

        Field[] fields = object.getClass().getDeclaredFields();
        StringBuilder formBody = new StringBuilder();
        for (Field field : fields) {
            field.setAccessible(true);
            String name = field.getName();
            Object value = null;
            try {
                value = field.get(object);
            } catch (IllegalAccessException e) {
                continue;
            }
            if (value == null) {
                continue;
            }

            String valueStr;
            if (value instanceof Date) {
                valueStr = DateFormatUtil.formatDate(FORM_DATETIME_FORMAT, (Date) value);
            } else {
                valueStr = String.valueOf(value);
            }
            formBody.append(name).append("=").append(valueStr).append("&");
        }
        template.body(Request.Body.bodyTemplate(formBody.toString(), Util.UTF_8));
    }
}
