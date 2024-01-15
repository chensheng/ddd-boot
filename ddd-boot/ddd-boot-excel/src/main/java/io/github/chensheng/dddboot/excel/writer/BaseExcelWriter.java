package io.github.chensheng.dddboot.excel.writer;

import io.github.chensheng.dddboot.excel.core.WorkbookConfig;
import io.github.chensheng.dddboot.excel.core.WorkbookConfigResolver;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class BaseExcelWriter implements ExcelWriter {
    private static final Logger logger = LoggerFactory.getLogger(BaseExcelWriter.class);

    @Override
    public void write(OutputStream outputStream, InputStream templateIs, RowWritingListener rowWritingListener, Class<?>... rowTypes) throws Exception {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream must not be null");
        }

        if (rowWritingListener == null) {
            throw new IllegalArgumentException("rowWritingListener must not be null");
        }

        if (rowTypes == null || rowTypes.length == 0) {
            throw new IllegalArgumentException("rowTypes must not be empty");
        }

        try {
            WorkbookConfig workbookConfig = WorkbookConfigResolver.resolveWorkbook(rowTypes);
            doWrite(outputStream, templateIs, rowWritingListener, workbookConfig);
        } finally {
            if (templateIs != null) {
                try {
                    templateIs.close();
                } catch (IOException e) {
                    logger.warn(ExceptionUtil.stackTraceText(e));
                }
            }
        }
    }

    protected abstract void doWrite(OutputStream outputStream, InputStream templateIs, RowWritingListener rowWritingListener, WorkbookConfig workbookConfig) throws Exception;
}
