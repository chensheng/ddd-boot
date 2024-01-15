package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public class LongConverter implements Converter {
    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return Long.class == fieldType || long.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        Class<?> fieldType = field.getType();
        try {
            return Long.parseLong(cellContent);
        } catch (NumberFormatException e) {
            return Long.class == fieldType ? null : 0l;
        }
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        Long value = (Long) cellValue;
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
    }
}
