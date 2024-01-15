package io.github.chensheng.dddboot.excel.converter;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Workbook;

import java.lang.reflect.Field;

public interface Converter {
    boolean support(Field field, CellValueType type);

    Object fromCellContent(String cellContent, Field field, String format, boolean use1904DateWindowing);

    void setCellContent(Workbook workbook, Cell cell, Object cellValue, String format);
}
