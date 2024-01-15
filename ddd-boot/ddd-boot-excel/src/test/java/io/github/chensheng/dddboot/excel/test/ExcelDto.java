package io.github.chensheng.dddboot.excel.test;

import io.github.chensheng.dddboot.excel.annotation.ExcelCell;

import java.util.Date;

public class ExcelDto {
    @ExcelCell(index = 0, name = "字符串")
    private String fieldString;

    @ExcelCell(index = 1, name = "整型数字")
    private Integer fieldInteger;

    @ExcelCell(index = 2, name = "浮点型数字", format = "0.0000")
    private Double fieldDouble;

    @ExcelCell(index = 3, name = "日期", format = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date fieldDate;

    public String getFieldString() {
        return fieldString;
    }

    public void setFieldString(String fieldString) {
        this.fieldString = fieldString;
    }

    public Integer getFieldInteger() {
        return fieldInteger;
    }

    public void setFieldInteger(Integer fieldInteger) {
        this.fieldInteger = fieldInteger;
    }

    public Double getFieldDouble() {
        return fieldDouble;
    }

    public void setFieldDouble(Double fieldDouble) {
        this.fieldDouble = fieldDouble;
    }

    public Date getFieldDate() {
        return fieldDate;
    }

    public void setFieldDate(Date fieldDate) {
        this.fieldDate = fieldDate;
    }
}
