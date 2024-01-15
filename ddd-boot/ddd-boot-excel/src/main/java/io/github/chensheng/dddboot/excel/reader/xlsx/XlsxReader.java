package io.github.chensheng.dddboot.excel.reader.xlsx;

import io.github.chensheng.dddboot.excel.core.SheetConfig;
import io.github.chensheng.dddboot.excel.core.WorkbookConfig;
import io.github.chensheng.dddboot.excel.reader.BaseExcelReader;
import io.github.chensheng.dddboot.excel.reader.RowReadingListener;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbook;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTWorkbookPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.WorkbookDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XlsxReader extends BaseExcelReader {
    private static final Logger logger = LoggerFactory.getLogger(XlsxReader.class);

    @Override
    protected void doRead(InputStream inputStream, RowReadingListener rowReadingListener, WorkbookConfig workbookConfig) throws Exception {
        if (CollectionUtil.isEmpty(workbookConfig.getSheets())) {
            return;
        }

        OPCPackage opcPackage = OPCPackage.open(inputStream);
        XSSFReader xssfReader = new XSSFReader(opcPackage);
        SharedStringsTable sharedStringsTable = xssfReader.getSharedStringsTable();
        boolean use1904DateWindowing = use1904DateWindowing(xssfReader);

        List<InputStream> sheetDataList = new ArrayList<InputStream>();
        Map<String, InputStream> sheetDataMap = new HashMap<String, InputStream>();
        XSSFReader.SheetIterator sheetDataIt = (XSSFReader.SheetIterator) xssfReader.getSheetsData();
        while (sheetDataIt.hasNext()) {
            InputStream sheetData = sheetDataIt.next();
            String sheetName = sheetDataIt.getSheetName();
            sheetDataList.add(sheetData);
            sheetDataMap.put(sheetName, sheetData);
        }

        XMLReader xmlReader = initXMLReader();
        for (SheetConfig sheetConfig : workbookConfig.getSheets()) {
            InputStream sheetData = null;
            if (TextUtil.isNotEmpty(sheetConfig.getSheetName())) {
                sheetData = sheetDataMap.get(sheetConfig.getSheetName());
            } else if (sheetConfig.getSheetIndex() < sheetDataList.size()) {
                sheetData = sheetDataList.get(sheetConfig.getSheetIndex());
            }
            if (sheetData == null) {
                logger.error("Could not find sheet with index[{}], name[{}]", sheetConfig.getSheetIndex(), sheetConfig.getSheetName());
                continue;
            }

            try {
                XlsxSheetHandler sheetHandler = new XlsxSheetHandler(sheetConfig, sharedStringsTable, rowReadingListener, use1904DateWindowing);
                xmlReader.setContentHandler(sheetHandler);
                InputSource sheetSource = new InputSource(sheetData);
                xmlReader.parse(sheetSource);
            } catch (Exception e) {
                logger.error(ExceptionUtil.stackTraceText(e));
            } finally {
                if (sheetData != null) {
                    try {
                        sheetData.close();
                    } catch (IOException e) {
                        logger.error(ExceptionUtil.stackTraceText(e));
                    }
                }
            }
        }
    }

    private XMLReader initXMLReader () throws Exception {
        SAXParserFactory saxFactory = SAXParserFactory.newInstance();
        saxFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        saxFactory.setFeature("http://xml.org/sax/features/external-general-entities", false);
        saxFactory.setFeature("http://xml.org/sax/features/external-parameter-entities", false);
        SAXParser saxParser = saxFactory.newSAXParser();
        XMLReader xmlReader = saxParser.getXMLReader();
        return xmlReader;
    }

    private boolean use1904DateWindowing(XSSFReader xssfReader) throws Exception {
        InputStream workbookXml = xssfReader.getWorkbookData();
        WorkbookDocument ctWorkbook = WorkbookDocument.Factory.parse(workbookXml);
        CTWorkbook wb = ctWorkbook.getWorkbook();
        CTWorkbookPr prefix = wb.getWorkbookPr();
        if (prefix != null) {
            return prefix.getDate1904();
        }
        return false;
    }
}
