package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.excel.core.NumericUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public class FloatConverter implements Converter {
    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return Float.class == fieldType || float.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        Class<?> fieldType = field.getType();

        Integer scale = NumericUtil.calculateScale(format);
        String text = NumericUtil.formatNumericInNeed(cellContent, scale);
        if (TextUtil.isEmpty(text)) {
            return Float.class == fieldType ? null : 0f;
        }

        try {
            return Float.parseFloat(text);
        } catch (NumberFormatException e) {
            return Float.class == fieldType ? null : 0f;
        }
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        Float value = (Float) cellValue;
        Integer scale = NumericUtil.calculateScale(format);
        String cellContent = NumericUtil.formatNumericInNeed(String.valueOf(value), scale);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(cellContent);
    }
}
