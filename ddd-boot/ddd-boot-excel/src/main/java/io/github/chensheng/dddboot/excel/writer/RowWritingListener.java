package io.github.chensheng.dddboot.excel.writer;

import io.github.chensheng.dddboot.excel.core.SheetConfig;

import java.util.List;

public interface RowWritingListener {
    List<?> getSheetData(SheetConfig sheetConfig);
}
