package io.github.chensheng.dddboot.excel.reader;

import io.github.chensheng.dddboot.excel.core.ExcelType;
import io.github.chensheng.dddboot.excel.reader.xls.XlsReader;
import io.github.chensheng.dddboot.excel.reader.xlsx.XlsxReader;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelReaderFactory {
    private static final Logger logger = LoggerFactory.getLogger(ExcelReaderFactory.class);

    public static <T> List<T> read(InputStream inputStream, Class<T> rowType) {
        List<T> rowDataList = new ArrayList<T>();
        read(inputStream, (sheetConfig, rowData, rowIndex) -> {
            if (rowData != null) {
                rowDataList.add((T) rowData);
            }
        }, rowType);
        return rowDataList;
    }

    public static boolean read(InputStream inputStream, RowReadingListener rowReadingListener, Class<?>... rowTypes) {
        ExcelType excelType = resolveExcelType(inputStream);

        try {
            if (excelType == null) {
                readUnknownType(inputStream, rowReadingListener, rowTypes);
            } else if (excelType == ExcelType.XLS) {
                new XlsReader().read(inputStream, rowReadingListener, rowTypes);
            } else if (excelType == ExcelType.XLSX) {
                new XlsxReader().read(inputStream, rowReadingListener, rowTypes);
            }
            return true;
        } catch (Exception e) {
            logger.error(ExceptionUtil.stackTraceText(e));
            return false;
        }
    }

    private static void readUnknownType(InputStream inputStream, RowReadingListener rowReadingListener, Class<?>... rowTypes) throws Exception {
        try {
            new XlsxReader().read(inputStream, rowReadingListener, rowTypes);
        } catch (Exception e) {
            new XlsxReader().read(inputStream, rowReadingListener, rowTypes);
        }
    }

    private static ExcelType resolveExcelType(InputStream inputStream) {
        if (inputStream == null || !inputStream.markSupported()) {
            return null;
        }

        try {
            FileMagic fileMagic =  FileMagic.valueOf(inputStream);

            if(FileMagic.OLE2.equals(fileMagic)){
                return ExcelType.XLS;
            }

            if(FileMagic.OOXML.equals(fileMagic)){
                return ExcelType.XLSX;
            }
        } catch (IOException e) {
            logger.warn(ExceptionUtil.stackTraceText(e));
        }
        return null;
    }
}
