package io.github.chensheng.dddboot.excel.reader.xlsx;

import io.github.chensheng.dddboot.excel.core.CellValueType;
import io.github.chensheng.dddboot.excel.core.ExcelXmlConstants;
import io.github.chensheng.dddboot.excel.core.SheetConfig;
import io.github.chensheng.dddboot.excel.reader.RowDataAssembler;
import io.github.chensheng.dddboot.excel.reader.RowReadingListener;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Arrays;

public class XlsxSheetHandler extends DefaultHandler {
    private static final Logger logger = LoggerFactory.getLogger(XlsxSheetHandler.class);

    private SheetConfig sheetConfig;

    private SharedStringsTable sharedStringsTable;

    private RowReadingListener rowReadingListener;

    private boolean use1904DateWindowing;

    private int totalRowCount;

    private int currentRowIndex;

    private int currentCellIndex;

    private CellValueType currentCellValueType;

    private String currentCellContent;

    private String[] currentRowContent;

    public XlsxSheetHandler(SheetConfig sheetConfig, SharedStringsTable sharedStringsTable, RowReadingListener rowReadingListener, boolean use1904DateWindowing) {
        if (sheetConfig == null) {
            throw new IllegalArgumentException("sheetConfig must not be null");
        }

        if (sharedStringsTable == null) {
            throw new IllegalArgumentException("sharedStringsTable must not be null");
        }

        if (rowReadingListener == null) {
            throw new IllegalArgumentException("rowReadingListener must not be null");
        }

        this.sheetConfig = sheetConfig;
        this.sharedStringsTable = sharedStringsTable;
        this.rowReadingListener = rowReadingListener;
        this.use1904DateWindowing = use1904DateWindowing;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        doStartDimension(qName, attributes);
        doStartRow(qName);
        doStartCell(qName, attributes);
        doStartCellValue(qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        doEndCellValue(qName);
        doEndRow(qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        currentCellContent += new String(ch, start, length);
    }

    private void doStartDimension(String qName, Attributes attributes) {
        if (!ExcelXmlConstants.DIMENSION_TAG.equals(qName)) {
            return;
        }

        String ref = attributes.getValue(ExcelXmlConstants.DIMENSION_ATTR_REF);
        String totalRowsStr = ref.substring(ref.indexOf(":") + 1)
                .replaceAll("[a-zA-Z]", "");
        try {
            totalRowCount = Integer.parseInt(totalRowsStr);
        } catch (NumberFormatException e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        }
    }

    private void doStartRow(String qName) {
        if (!ExcelXmlConstants.ROW_TAG.equals(qName)) {
            return;
        }

        currentRowContent = new String[20];
    }

    private void doStartCell(String qName, Attributes attributes) {
        if (!ExcelXmlConstants.CELL_TAG.equals(qName)) {
            return;
        }

        String processingPositionInfo = attributes.getValue(ExcelXmlConstants.CELL_ATTR_POSITION);
        int processingRowIndex = doResolveRowIndex(processingPositionInfo);
        if (processingRowIndex > currentRowIndex) {
            currentRowIndex = processingRowIndex;
        }

        currentCellIndex = doResolveCellIndex(processingPositionInfo);

        String cellType = attributes.getValue(ExcelXmlConstants.CELL_ATTR_TYPE);
        if (ExcelXmlConstants.CELL_ATTR_TYPE_STRING.equals(cellType)) {
            currentCellValueType = CellValueType.STRING;
        } else {
            currentCellValueType = CellValueType.AUTO;
        }
    }

    private void doStartCellValue(String qName, Attributes attributes) {
        if (ExcelXmlConstants.CELL_VALUE_TAG.equals(qName) || ExcelXmlConstants.CELL_VALUE_TAG_1.equals(qName)) {
            currentCellContent = "";
        }
    }

    private void doEndRow(String qName) {
        if (!ExcelXmlConstants.ROW_TAG.equals(qName)) {
            return;
        }

        if (currentRowIndex < sheetConfig.getDataRowStartIndex()) {
            return;
        }

        try {
            Object currentRowData = RowDataAssembler.assemble(sheetConfig.getRowType(), sheetConfig.getDataRowConfig(), currentRowContent, use1904DateWindowing);
            rowReadingListener.onFinish(sheetConfig, currentRowData, currentRowIndex);
        } catch (Exception e) {
            logger.warn(ExceptionUtil.stackTraceText(e));
        }
    }

    private void doEndCellValue(String qName) {
        if (!ExcelXmlConstants.CELL_VALUE_TAG.equals(qName) && !ExcelXmlConstants.CELL_VALUE_TAG_1.equals(qName)) {
            return;
        }

        if (currentCellIndex >= currentRowContent.length) {
            currentRowContent = Arrays.copyOf(currentRowContent, (int)(currentCellIndex * 1.5));
        }

        if (ExcelXmlConstants.CELL_VALUE_TAG_1.equals(qName)) {
            currentRowContent[currentCellIndex] = currentCellContent;
            return;
        }

        if (CellValueType.STRING == currentCellValueType) {
            try {
                int valueIndex = Integer.parseInt(currentCellContent);
                currentCellContent = sharedStringsTable.getItemAt(valueIndex).toString();
            } catch (NumberFormatException e) {
                logger.error(ExceptionUtil.stackTraceText(e));
            }
        }
        currentRowContent[currentCellIndex] = currentCellContent;
    }

    private int doResolveRowIndex(String positionInfo) {
        if (TextUtil.isEmpty(positionInfo)) {
            return 0;
        }

        String rowIndexStr = positionInfo.replaceAll("[a-zA-Z]", "");
        try {
            return Integer.parseInt(rowIndexStr) - 1;
        } catch (NumberFormatException e) {
            logger.error(ExceptionUtil.stackTraceText(e));
        }
        return 0;
    }

    private int doResolveCellIndex(String positionInfo) {
        if (TextUtil.isEmpty(positionInfo)) {
            return 0;
        }

        char[] indexChars = positionInfo.replaceAll("[0-9]", "").toCharArray();
        if (indexChars == null || indexChars.length == 0) {
            return 0;
        }

        int cellIndex = 0;
        for (int i = 0; i < indexChars.length; i++) {
            cellIndex += (indexChars[i] - '@') * Math.pow(26, (indexChars.length - i - 1));
        }

        return cellIndex - 1;
    }
}
