package io.github.chensheng.dddboot.excel.writer;

import io.github.chensheng.dddboot.excel.core.ExcelType;
import io.github.chensheng.dddboot.excel.writer.xlsx.XlsxWriter;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ExcelWriterFactory {
    private static final Logger logger = LoggerFactory.getLogger(ExcelWriterFactory.class);

    public static void write(OutputStream outputStream, List<?> sheetData) {
        write(outputStream, null, sheetData);
    }

    public static void write(OutputStream outputStream, InputStream templateIs, List<?> sheetData) {
        if (CollectionUtil.isEmpty(sheetData)) {
            return;
        }

        write(outputStream, templateIs, (sheetConfig) -> sheetData, ExcelType.XLSX, sheetData.get(0).getClass());
    }

    public static boolean write(OutputStream outputStream, InputStream templateIs, RowWritingListener rowWritingListener, ExcelType excelType, Class<?>... rowTypes) {
        try {
            if (ExcelType.XLSX.equals(excelType)) {
                new XlsxWriter().write(outputStream, templateIs, rowWritingListener, rowTypes);
                return true;
            }
        } catch (Exception e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        }
        return false;
    }
}
