package io.github.chensheng.dddboot.excel.writer.xlsx;

import io.github.chensheng.dddboot.excel.converter.CellContentConverterFactory;
import io.github.chensheng.dddboot.excel.writer.BaseExcelWriter;
import io.github.chensheng.dddboot.excel.writer.RowWritingListener;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.excel.core.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxWriter extends BaseExcelWriter {
    private static final Logger logger = LoggerFactory.getLogger(XlsxWriter.class);

    private Map<CellStyleConfig, CellStyle> cellStyleMap = new HashMap<CellStyleConfig, CellStyle>();

    @Override
    protected void doWrite(OutputStream outputStream, InputStream templateIs, RowWritingListener rowWritingListener, WorkbookConfig workbookConfig) throws Exception {
        Workbook workbook = null;
        try {
            workbook = WorkbookUtil.createWorkBook(templateIs, ExcelType.XLSX);
            for (SheetConfig sheetConfig : workbookConfig.getSheets()) {
                Sheet sheet = WorkbookUtil.createOrGetSheet(workbook, sheetConfig);
                createSheetHeader(workbook, sheet, sheetConfig);
                writeSheetData(workbook, sheet, sheetConfig, rowWritingListener);
            }
            workbook.write(outputStream);
        } finally {
            cellStyleMap.clear();
            destroy(workbook);
        }
    }

    private void destroy(Workbook workbook) {
        if (workbook == null) {
            return;
        }

        if (workbook instanceof SXSSFWorkbook) {
            SXSSFWorkbook sxssfWorkbook = (SXSSFWorkbook) workbook;
            sxssfWorkbook.dispose();
            return;
        }

        try {
            workbook.close();
        } catch (IOException e) {
            logger.warn(ExceptionUtil.stackTraceText(e));
        }
    }

    private void createSheetHeader(Workbook workbook, Sheet sheet, SheetConfig sheetConfig) {
        if (!sheetConfig.isWriteHeader()) {
            return;
        }

        List<HeaderCellConfig> headerRowConfig = sheetConfig.getHeaderRowConfig();
        if (CollectionUtil.isEmpty(headerRowConfig)) {
            return;
        }

        int headerRowIndex = sheetConfig.getDataRowStartIndex() - 1;
        if (headerRowIndex < 0) {
            return;
        }

        Row row = WorkbookUtil.createOrGetRow(sheet, headerRowIndex);
        for (HeaderCellConfig cellConfig : headerRowConfig) {
            Cell cell = WorkbookUtil.createOrGetCell(row, cellConfig.getIndex());
            cell.setCellValue(cellConfig.getName());
            if (cellConfig.getStyle() != null) {
                CellStyle cellStyle = createOrGetCellStyle(workbook, cellConfig.getStyle());
                cell.setCellStyle(cellStyle);
            }
        }
    }

    private void writeSheetData(Workbook workbook, Sheet sheet, SheetConfig sheetConfig, RowWritingListener rowWritingListener) {
        List<?> sheetData = rowWritingListener.getSheetData(sheetConfig);
        if (CollectionUtil.isEmpty(sheetData)) {
            return;
        }

        int dataRowStartIndex = sheetConfig.getDataRowStartIndex();
        for (int i = 0; i < sheetData.size(); i++) {
            Object rowData = sheetData.get(i);
            Row row = WorkbookUtil.createOrGetRow(sheet, dataRowStartIndex + i);
            for (DataCellConfig cellConfig : sheetConfig.getDataRowConfig()) {
                try {
                    Cell cell = WorkbookUtil.createOrGetCell(row, cellConfig.getIndex());
                    CellContentConverterFactory.setCellContent(workbook, cell, rowData, cellConfig.getField(), cellConfig.getType(), cellConfig.getFormat());
                    if (cellConfig.getStyle() != null) {
                        CellStyle cellStyle = createOrGetCellStyle(workbook, cellConfig.getStyle());
                        cell.setCellStyle(cellStyle);
                    }
                } catch (Exception e) {
                    logger.warn(ExceptionUtil.stackTraceText(e));
                }
            }
        }
    }

    private CellStyle createOrGetCellStyle(Workbook workbook, CellStyleConfig config) {
        if (cellStyleMap.containsKey(config)) {
            return cellStyleMap.get(config);
        }

        CellStyle cellStyle = WorkbookUtil.createCellStyle(workbook, config);
        cellStyleMap.put(config, cellStyle);
        return cellStyle;
    }
}
