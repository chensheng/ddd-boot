package io.github.chensheng.dddboot.web.core;

import org.springframework.core.convert.converter.Converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

public class SmartStringToDateConverter implements Converter<String, Date> {
    private static final String REG_DATETIME_FORMAT = "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2}) ([0-9]{1,2}):([0-9]{1,2}):([0-9]{1,2})";

    private static final String REG_DATE_FORMAT = "([0-9]{4})-([0-9]{1,2})-([0-9]{1,2})";

    private ThreadLocal<SimpleDateFormat> threadLocalDatetimeFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    private ThreadLocal<SimpleDateFormat> threadLocalDateFormat = new ThreadLocal<SimpleDateFormat>() {
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    @Override
    public Date convert(String source) {
        if (source == null) {
            return null;
        }

        try {
            if (Pattern.compile(REG_DATE_FORMAT).matcher(source).matches()) {
                return threadLocalDateFormat.get().parse(source);
            } else {
                return threadLocalDatetimeFormat.get().parse(source);
            }
        } catch (ParseException e) {
            return null;
        }
    }
}
