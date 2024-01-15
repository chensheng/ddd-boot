package io.github.chensheng.dddboot.excel.core;

import java.util.ArrayList;
import java.util.List;

public class SheetConfig {
    private Class<?> rowType;

    private int sheetIndex;

    private String sheetName;

    private int dataRowStartIndex;

    private boolean writeHeader;

    public boolean isWriteHeader() {
        return writeHeader;
    }

    public void setWriteHeader(boolean writeHeader) {
        this.writeHeader = writeHeader;
    }

    private List<HeaderCellConfig> headerRowConfig = new ArrayList<HeaderCellConfig>();

    private List<DataCellConfig> dataRowConfig = new ArrayList<DataCellConfig>();

    public Class<?> getRowType() {
        return rowType;
    }

    public void setRowType(Class<?> rowType) {
        this.rowType = rowType;
    }

    public int getSheetIndex() {
        return sheetIndex;
    }

    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getDataRowStartIndex() {
        return dataRowStartIndex;
    }

    public void setDataRowStartIndex(int dataRowStartIndex) {
        this.dataRowStartIndex = dataRowStartIndex;
    }

    public List<HeaderCellConfig> getHeaderRowConfig() {
        return headerRowConfig;
    }

    public void addHeaderCellConfig(HeaderCellConfig headerCellConfig) {
        if (headerCellConfig != null) {
            headerRowConfig.add(headerCellConfig);
        }
    }

    public List<DataCellConfig> getDataRowConfig() {
        return dataRowConfig;
    }

    public void addDataCellConfig(DataCellConfig dataCellConfig) {
        if (dataCellConfig != null) {
            dataRowConfig.add(dataCellConfig);
        }
    }
}
