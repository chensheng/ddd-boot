package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public class IntegerConverter implements Converter {
    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return Integer.class == fieldType || int.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        try {
            return Integer.parseInt(cellContent);
        } catch (NumberFormatException e) {
            try {
                return (int) Double.parseDouble(cellContent);
            } catch (Exception ex) {
                return Integer.class == field.getType() ? null : 0;
            }
        }
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        Integer value = (Integer) cellValue;
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(value);
    }
}
