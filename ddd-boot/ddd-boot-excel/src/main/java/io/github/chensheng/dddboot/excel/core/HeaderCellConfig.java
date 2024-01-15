package io.github.chensheng.dddboot.excel.core;

public class HeaderCellConfig {
    private int index;

    private String name;

    private CellStyleConfig style = CellStyleConfig.DEFAULT_HEADER_CELL_STYLE;

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CellStyleConfig getStyle() {
        return style;
    }

    public void setStyle(CellStyleConfig style) {
        this.style = style;
    }
}
