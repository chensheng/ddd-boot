package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.reflect.ReflectionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class CellContentConverterFactory {
    private static final Logger logger = LoggerFactory.getLogger(CellContentConverterFactory.class);

    private static final List<Converter> converters = new ArrayList<Converter>();

    static {
        converters.add(new HyperLinkConverter());
        converters.add(new BooleanConverter());
        converters.add(new ShortConverter());
        converters.add(new IntegerConverter());
        converters.add(new LongConverter());
        converters.add(new FloatConverter());
        converters.add(new DoubleConverter());
        converters.add(new ShortConverter());
        converters.add(new BigDecimalConverter());
        converters.add(new DoubleConverter());
        converters.add(new DateConverter());
        converters.add(new StringConverter());
    }

    public static Object fromCellContent(String cellContent, Field field, CellValueType type, String format, boolean use1904DateWindowing) {
        if (TextUtil.isEmpty(cellContent) || field == null) {
            return null;
        }

        for (Converter converter : converters) {
            if (converter.support(field, type)) {
                return converter.fromCellContent(cellContent, field, format, use1904DateWindowing);
            }
        }

        return null;
    }

    public static void setCellContent(Workbook workbook, Cell cell, Object rowData, Field field, CellValueType type, String format) {
        if (workbook == null || cell == null || rowData == null || field == null) {
            return;
        }

        Object cellValue;
        try {

            ReflectionUtil.makeAccessible(field);
            cellValue = ReflectionUtil.getFieldValue(rowData, field);
        } catch (Exception e) {
            logger.warn(ExceptionUtil.stackTraceText(e));
            return;
        }
        if (cellValue == null) {
            return;
        }

        for (Converter converter : converters) {
            if (converter.support(field, type)) {
                converter.setCellContent(workbook, cell, cellValue, format);
                return;
            }
        }
    }
}
