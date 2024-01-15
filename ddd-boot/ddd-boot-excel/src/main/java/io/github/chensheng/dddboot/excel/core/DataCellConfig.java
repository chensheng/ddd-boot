package io.github.chensheng.dddboot.excel.core;

import java.lang.reflect.Field;

public class DataCellConfig {
    private int index;

    private Field field;

    private String format;

    private CellValueType type;

    private CellStyleConfig style;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public Field getField() {
        return field;
    }

    public void setField(Field field) {
        this.field = field;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public CellValueType getType() {
        return type;
    }

    public void setType(CellValueType type) {
        this.type = type;
    }

    public CellStyleConfig getStyle() {
        return style;
    }

    public void setStyle(CellStyleConfig style) {
        this.style = style;
    }
}
