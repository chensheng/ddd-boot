package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.excel.core.NumericUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class BigDecimalConverter implements Converter {
    @Override
    public boolean support(Field field, CellValueType type) {
        Class<?> fieldType = field.getType();
        return BigDecimal.class == fieldType;
    }

    @Override
    public Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing) {
        try {
            Integer scale = NumericUtil.calculateScale(format);
            if (scale == null) {
                return new BigDecimal(cellContent);
            } else {
                return new BigDecimal(cellContent).setScale(scale, RoundingMode.HALF_UP);
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }

    @Override
    public void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format) {
        BigDecimal value = (BigDecimal) cellValue;

        Integer scale = NumericUtil.calculateScale(format);
        String cellContent;
        if (scale != null) {
            cellContent = new BigDecimal(value.toPlainString()).setScale(scale, RoundingMode.HALF_UP).toPlainString();
        } else {
            cellContent = value.toPlainString();
        }
        cell.setCellType(CellType.NUMERIC);
        cell.setCellValue(cellContent);
    }

}
