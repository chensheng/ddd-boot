package io.github.chensheng.dddboot.excel.reader;

import io.github.chensheng.dddboot.excel.core.SheetConfig;

public interface RowReadingListener {
    void onFinish(SheetConfig sheetConfig, Object rowData, int rowIndex);
}
