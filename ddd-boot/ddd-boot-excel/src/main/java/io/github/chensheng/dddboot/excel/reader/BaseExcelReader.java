package io.github.chensheng.dddboot.excel.reader;

import io.github.chensheng.dddboot.excel.core.WorkbookConfig;
import io.github.chensheng.dddboot.excel.core.WorkbookConfigResolver;

import java.io.InputStream;

public abstract class BaseExcelReader implements ExcelReader {

    @Override
    public void read(InputStream inputStream, RowReadingListener rowReadingListener, Class<?>... rowTypes) throws Exception{
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null");
        }

        if (rowReadingListener == null) {
            throw new IllegalArgumentException("rowReadingListener must not be null");
        }

        if (rowTypes == null || rowTypes.length == 0) {
            throw new IllegalArgumentException("rowTypes must not be empty");
        }

        WorkbookConfig workbookConfig = WorkbookConfigResolver.resolveWorkbook(rowTypes);
        doRead(inputStream, rowReadingListener, workbookConfig);
    }

    protected abstract void doRead(InputStream inputStream, RowReadingListener rowReadingListener, WorkbookConfig workbookConfig) throws Exception;
}
