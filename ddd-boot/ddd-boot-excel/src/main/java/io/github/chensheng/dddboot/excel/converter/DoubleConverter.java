package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.excel.core.NumericUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public class DoubleConverter implements Converter {

    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return Double.class == fieldType || double.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        Class<?> fieldType = field.getType();

        Integer scale = NumericUtil.calculateScale(format);
        String text = NumericUtil.formatNumericInNeed(cellContent, scale);
        if (TextUtil.isEmpty(text)) {
            return Double.class == fieldType ? null : 0d;
        }

        try {
            return Double.parseDouble(text);
        } catch (NumberFormatException e) {
            return Double.class == fieldType ? null : 0d;
        }
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        Double value = (Double) cellValue;
        Integer scale = NumericUtil.calculateScale(format);
        String cellContent = NumericUtil.formatNumericInNeed(String.valueOf(value), scale);
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(cellContent);
    }

}
