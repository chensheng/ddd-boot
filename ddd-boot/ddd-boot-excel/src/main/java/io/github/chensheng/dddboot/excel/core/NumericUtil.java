package io.github.chensheng.dddboot.excel.core;

import io.github.chensheng.dddboot.tools.text.TextUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.regex.Pattern;

public class NumericUtil {
    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?$");

    public static boolean isNumeric(String str) {
        if (TextUtil.isEmpty(str)) {
            return false;
        }

        return NUMERIC_PATTERN.matcher(str).matches();
    }

    public static Integer calculateScale(String format) {
        if (TextUtil.isEmpty(format)) {
            return null;
        }

        String scaleStr;
        int dotIndex = format.indexOf(".");
        if (dotIndex < 0) {
            scaleStr = format;
        } else if (dotIndex + 1 >= format.length()) {
            scaleStr = "";
        } else {
            scaleStr = format.substring(dotIndex + 1);
        }

        return scaleStr.length();
    }

    public static int countChar(String value, char targetChar) {
        int count = 0;
        if (value == null) {
            return count;
        }

        char[] chars = value.toCharArray();
        for (char cc : chars) {
            if (cc == targetChar) {
                count++;
            }
        }
        return count;
    }

    public static String formatNumericInNeed(String value, Integer scale) {
        if (value == null) {
            return null;
        }

        if (!value.contains(".") || !isNumeric(value)) {
            return value;
        }

        try {
            BigDecimal bigDecimal = new BigDecimal(value);
            if (scale == null) {
                return bigDecimal.setScale(10, RoundingMode.HALF_UP).toPlainString();
            } else {
                return bigDecimal.setScale(scale, RoundingMode.HALF_UP).toPlainString();
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
