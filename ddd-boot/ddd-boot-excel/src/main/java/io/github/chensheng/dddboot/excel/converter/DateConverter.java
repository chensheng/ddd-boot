package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.tools.time.DateFormatUtil;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.Date;
import java.util.regex.Pattern;

public class DateConverter implements Converter {
    private static final Logger logger = LoggerFactory.getLogger(DateConverter.class);

    private static final Pattern NUMERIC_PATTERN = Pattern.compile("[\\+\\-]?[\\d]+([\\.][\\d]*)?([Ee][+-]?[\\d]+)?$");

    private static final String[] DATE_FORMATS = {"yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyyMMdd HH:mm:ss"};

    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return Date.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        if (!isNumeric(cellContent)) {
            Date simpleDate = getSimpleDate(cellContent, format);
            if (simpleDate != null) {
                return simpleDate;
            }
        }

        try {
            Double time = Double.parseDouble(cellContent);
            return HSSFDateUtil.getJavaDate(time, use1904DateWindowing);
        } catch (NumberFormatException e) {
        }
        return null;
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        Date value = (Date) cellValue;
        String cellContent = null;
        if (TextUtil.isNotEmpty(format)) {
            try {
                cellContent = DateFormatUtil.formatDate(format, value);
            } catch (Exception e) {
            }
        }
        if (cellContent == null) {
            cellContent = DateFormatUtil.formatDate(DATE_FORMATS[0], value);
        }
        if (cellContent != null) {
            cell.setCellValue(cellContent);
        }
    }

    private Date getSimpleDate(String value, String format) {
        if (TextUtil.isNotEmpty(format)) {
            try {
                return DateFormatUtil.parseDate(format, value);
            } catch (ParseException e) {
                logger.warn(ExceptionUtil.stackTraceText(e));
                return null;
            }
        }

        for (String dateFormat : DATE_FORMATS) {
            try {
                return DateFormatUtil.parseDate(dateFormat, value);
            } catch (ParseException e) {
            }
        }
        return null;
    }

    private static boolean isNumeric(String str) {
        return NUMERIC_PATTERN.matcher(str).matches();
    }
}
