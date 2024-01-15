package io.github.chensheng.dddboot.excel.core;

import java.util.ArrayList;
import java.util.List;

public class WorkbookConfig {
    private List<SheetConfig> sheets = new ArrayList<SheetConfig>();

    public void add(SheetConfig sheet) {
        if (sheet != null) {
            sheets.add(sheet);
        }
    }

    public List<SheetConfig> getSheets() {
        return sheets;
    }
}
