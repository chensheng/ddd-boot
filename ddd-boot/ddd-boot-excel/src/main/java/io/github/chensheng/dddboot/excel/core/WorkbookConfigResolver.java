package io.github.chensheng.dddboot.excel.core;

import io.github.chensheng.dddboot.excel.annotation.ExcelCell;
import io.github.chensheng.dddboot.excel.annotation.ExcelSheet;
import io.github.chensheng.dddboot.tools.text.TextUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.concurrent.ConcurrentHashMap;

public class WorkbookConfigResolver {
    private static final ConcurrentHashMap<Class<?>, SheetConfig> SHEET_CONFIG_CACHE = new ConcurrentHashMap<Class<?>, SheetConfig>();

    public static WorkbookConfig resolveWorkbook(Class<?>... rowTypes) {
        WorkbookConfig workbookConfig = new WorkbookConfig();
        if (rowTypes == null || rowTypes.length == 0) {
            return workbookConfig;
        }

        for (int i = 0; i < rowTypes.length; i++) {
            Class<?> rowType = rowTypes[i];
            SheetConfig sheetConfig = resolveSheet(rowType);
            if (sheetConfig.getSheetIndex() < 0) {
                sheetConfig.setSheetIndex(i);
            }
            workbookConfig.add(sheetConfig);
        }
        return workbookConfig;
    }

    public static SheetConfig resolveSheet(Class<?> rowType) {
        if (rowType == null) {
            return null;
        }

        if (SHEET_CONFIG_CACHE.contains(rowType)) {
            return SHEET_CONFIG_CACHE.get(rowType);
        }

        SheetConfig sheetConfig = new SheetConfig();
        sheetConfig.setRowType(rowType);
        ExcelSheet excelSheet = rowType.getDeclaredAnnotation(ExcelSheet.class);
        if (excelSheet != null) {
            sheetConfig.setSheetIndex(excelSheet.index());
            sheetConfig.setSheetName(excelSheet.name());
            sheetConfig.setDataRowStartIndex(excelSheet.dataRowStartIndex());
            sheetConfig.setWriteHeader(excelSheet.writeHeader());
        }

        Field[] fields = rowType.getDeclaredFields();
        for (Field field : fields) {
            if (Modifier.isStatic(field.getModifiers())) {
                continue;
            }

            ExcelCell excelCell = field.getDeclaredAnnotation(ExcelCell.class);
            if (excelCell == null) {
                continue;
            }

            String headerName = excelCell.name();
            if (TextUtil.isEmpty(headerName)) {
                headerName = field.getName();
            }

            HeaderCellConfig headerCellConfig = new HeaderCellConfig();
            headerCellConfig.setIndex(excelCell.index());
            headerCellConfig.setName(headerName);
            sheetConfig.addHeaderCellConfig(headerCellConfig);

            DataCellConfig dataCellConfig = new DataCellConfig();
            dataCellConfig.setIndex(excelCell.index());
            dataCellConfig.setField(field);
            dataCellConfig.setFormat(excelCell.format());
            dataCellConfig.setType(excelCell.type());
            sheetConfig.addDataCellConfig(dataCellConfig);
        }
        SHEET_CONFIG_CACHE.putIfAbsent(rowType, sheetConfig);
        return sheetConfig;
    }
}
