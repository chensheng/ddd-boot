package io.github.chensheng.dddboot.excel.reader.xls;

import io.github.chensheng.dddboot.excel.core.WorkbookConfig;
import io.github.chensheng.dddboot.excel.reader.BaseExcelReader;
import io.github.chensheng.dddboot.excel.reader.RowReadingListener;

import java.io.InputStream;

public class XlsReader extends BaseExcelReader {
    @Override
    protected void doRead(InputStream inputStream, RowReadingListener rowReadingListener, WorkbookConfig workbookConfig) throws Exception {
        XlsSheetProcessor processor = new XlsSheetProcessor(rowReadingListener, workbookConfig, false);
        processor.execute(inputStream);
    }
}
