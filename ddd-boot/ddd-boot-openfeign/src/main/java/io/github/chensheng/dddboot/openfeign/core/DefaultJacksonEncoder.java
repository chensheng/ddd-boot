package io.github.chensheng.dddboot.openfeign.core;

import io.github.chensheng.dddboot.tools.mapper.JsonMapper;
import io.github.chensheng.dddboot.tools.time.DateFormatUtil;
import feign.Request;
import feign.RequestTemplate;
import feign.Util;
import feign.codec.EncodeException;
import feign.codec.Encoder;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Map;

public class DefaultJacksonEncoder implements Encoder {
    private static final String FORM_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Override
    public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
        if (bodyType == MAP_STRING_WILDCARD) {
            this.encodeForm(object, bodyType, template);
            return;
        }

        String bodyTemplate = JsonMapper.nonNullMapper().toJson(object);
        template.body(Request.Body.bodyTemplate(bodyTemplate, Util.UTF_8));
    }

    private void encodeForm(Object object, Type bodyType, RequestTemplate template) {
        if (object == null || !(object instanceof Map)) {
            return;
        }

        Map<String, Object> form = (Map<String, Object>) object;
        StringBuilder formBody = new StringBuilder();
        for (Map.Entry<String, Object> entry : form.entrySet()) {
            String name = entry.getKey();
            Object value = entry.getValue();
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
