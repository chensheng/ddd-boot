package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.excel.core.NumericUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public class StringConverter implements Converter {
    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return String.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        return NumericUtil.formatNumericInNeed(cellContent, null);
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        String value = (String) cellValue;
        cell.setCellValue(value);
    }
}
