package io.github.chensheng.dddboot.excel;

import io.github.chensheng.dddboot.excel.reader.ExcelReaderFactory;
import io.github.chensheng.dddboot.excel.writer.ExcelWriterFactory;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ExcelUtil {
    /**
     * Read excel data from input stream
     * @param inputStream required. excel data input stream
     * @param rowType required. row data type
     * @param <T>
     * @return row data list, return null if any argument is null
     */
    public static <T> List<T> read(InputStream inputStream, Class<T> rowType) {
        if (inputStream == null) {
            throw new IllegalArgumentException("inputStream must not be null");
        }
        if (rowType == null) {
            throw new IllegalArgumentException("rowType must not be null");
        }

        return ExcelReaderFactory.read(inputStream, rowType);
    }

    /**
     * Write data to excel
     * @param outputStream required
     * @param rowDataList required, not empty
     */
    public static void write(OutputStream outputStream, List<?> rowDataList) {
        write(outputStream, rowDataList, null);
    }

    /**
     * Write data to excel
     * @param outputStream required
     * @param rowDataList required, not empty
     * @param templateIs optional
     */
    public static void write(OutputStream outputStream, List<?> rowDataList, InputStream templateIs) {
        if (outputStream == null) {
            throw new IllegalArgumentException("outputStream must not be null");
        }
        if (CollectionUtil.isEmpty(rowDataList)) {
            throw new IllegalArgumentException("rowDataList must not be empty");
        }

        ExcelWriterFactory.write(outputStream, templateIs, rowDataList);
    }
}
