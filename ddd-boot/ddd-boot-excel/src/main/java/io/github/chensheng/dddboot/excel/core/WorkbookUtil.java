package io.github.chensheng.dddboot.excel.core;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class WorkbookUtil {
    public static Workbook createWorkBook(InputStream templateInputStream, ExcelType excelType) throws IOException {
        Workbook workbook;
        if (ExcelType.XLS.equals(excelType)) {
            workbook = (templateInputStream == null) ? new HSSFWorkbook() : new HSSFWorkbook(new POIFSFileSystem(templateInputStream));
        } else {
            workbook = (templateInputStream == null) ? new SXSSFWorkbook(500) : new SXSSFWorkbook(new XSSFWorkbook(templateInputStream), 500);
        }
        return workbook;
    }

    public static Sheet createOrGetSheet(Workbook workbook, SheetConfig sheetConfig) {
        Sheet sheet = null;
        try {
            try {
                sheet = workbook.getSheetAt(sheetConfig.getSheetIndex());
            } catch (Exception e) {
                sheet = createSheet(workbook, sheetConfig);
            }
        } catch (Exception e) {
            throw new RuntimeException("fail to create sheet", e);
        }
        return sheet;
    }

    public static Sheet createSheet(Workbook workbook, SheetConfig sheetConfig) {
        return workbook.createSheet(TextUtil.isNotEmpty(sheetConfig.getSheetName()) ? sheetConfig.getSheetName() : "sheet" + (sheetConfig.getSheetIndex() + 1));
    }

    public static Row createOrGetRow(Sheet sheet, int rowIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row != null) {
            return row;
        }

        return sheet.createRow(rowIndex);
    }

    public static Cell createOrGetCell(Row row, int cellIndex) {
        Cell cell = row.getCell(cellIndex);
        if (cell != null) {
            return cell;
        }

        return row.createCell(cellIndex);
    }

    public static CellStyle createCellStyle(Workbook workbook, CellStyleConfig config) {
        if (workbook == null || config == null) {
            return null;
        }

        CellStyle newCellStyle = workbook.createCellStyle();

        if (config.getFont() != null) {
            Font font = workbook.createFont();
            if (config.getFont().getFontName() != null) {
                font.setFontName(config.getFont().getFontName());
            }
            if (config.getFont().getFontHeightInPoints() > 0) {
                font.setFontHeightInPoints(config.getFont().getFontHeightInPoints());
            }
            font.setBold(config.getFont().isBold());
            newCellStyle.setFont(font);
        }

        if (config.getWrapText() != null) {
            newCellStyle.setWrapText(config.getWrapText());
        }
        if (config.getVerticalAlignment() != null) {
            newCellStyle.setVerticalAlignment(config.getVerticalAlignment());
        }
        if (config.getAlignment() != null) {
            newCellStyle.setAlignment(config.getAlignment());
        }
        if (config.getLocked() != null) {
            newCellStyle.setLocked(config.getLocked());
        }
        if (config.getFillPattern() != null) {
            newCellStyle.setFillPattern(config.getFillPattern());
        }
        if (config.getFillForegroundColor() != null) {
            newCellStyle.setFillForegroundColor(config.getFillForegroundColor());
        }
        if (config.getBorderBottom() != null) {
            newCellStyle.setBorderBottom(config.getBorderBottom());
        }
        if (config.getBorderLeft() != null) {
            newCellStyle.setBorderLeft(config.getBorderLeft());
        }
        return newCellStyle;
    }
}
