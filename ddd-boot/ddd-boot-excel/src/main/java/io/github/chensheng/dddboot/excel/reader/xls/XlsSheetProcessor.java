package io.github.chensheng.dddboot.excel.reader.xls;

import io.github.chensheng.dddboot.excel.core.SheetConfig;
import io.github.chensheng.dddboot.excel.core.WorkbookConfig;
import io.github.chensheng.dddboot.excel.reader.RowDataAssembler;
import io.github.chensheng.dddboot.excel.reader.RowReadingListener;
import io.github.chensheng.dddboot.tools.base.ExceptionUtil;
import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import org.apache.poi.hssf.eventusermodel.*;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class XlsSheetProcessor implements HSSFListener {
    private static final Logger logger = LoggerFactory.getLogger(XlsSheetProcessor.class);

    private RowReadingListener rowReadingListener;

    private WorkbookConfig workbookConfig;

    private boolean use1904DateWindowing;

    private EventWorkbookBuilder.SheetRecordCollectingListener workbookBuildingListener;

    private HSSFWorkbook stubWorkbook;

    private SSTRecord sstRecord;

    private FormatTrackingHSSFListener formatListener;

    private int lastRowNumber;

    private int lastColumnNumber;

    private int nextRow;

    private int nextColumn;

    private boolean outputNextStringRecord;

    private int sheetIndex = -1;

    private List<String> records;

    private boolean notAllEmpty = false;

    private BoundSheetRecord[] orderedBSRs;

    private List<BoundSheetRecord> boundSheetRecords = new ArrayList<BoundSheetRecord>();

    private boolean outputFormulaValues = true;

    private boolean trimContent;

    private HSSFEventFactory hssfEventFactory;

    public XlsSheetProcessor(RowReadingListener rowReadingListener, WorkbookConfig workbookConfig, boolean use1904DateWindowing) {
        if (rowReadingListener == null) {
            throw new IllegalArgumentException("rowReadingListener must not be null");
        }

        if (workbookConfig == null) {
            throw new IllegalArgumentException("workbookConfig must not be null");
        }

        if (CollectionUtil.isEmpty(workbookConfig.getSheets())) {
            throw new IllegalArgumentException("workbookConfig.getSheets() must not return empty");
        }

        this.rowReadingListener = rowReadingListener;
        this.workbookConfig = workbookConfig;
        this.use1904DateWindowing = use1904DateWindowing;

        init();

        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);
        hssfEventFactory = new HSSFEventFactory();
    }

    public void execute(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }

        POIFSFileSystem fs = new POIFSFileSystem(inputStream);
        HSSFRequest request = new HSSFRequest();
        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new EventWorkbookBuilder.SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }
        hssfEventFactory.processWorkbookEvents(request, fs);
    }

    @Override
    public void processRecord(Record record) {
        int processingRowIndex = -1;
        int processingColumnIndex = -1;
        String processingContent = null;

        switch (record.getSid()) {
            case BoundSheetRecord.sid:
                boundSheetRecords.add((BoundSheetRecord)record);
                break;
            case BOFRecord.sid:
                doProcessBOFRecord((BOFRecord)record);
                break;
            case SSTRecord.sid:
                sstRecord = (SSTRecord)record;
                break;
            case BlankRecord.sid:
                BlankRecord blankRecord = (BlankRecord)record;
                processingRowIndex = blankRecord.getRow();
                processingColumnIndex = blankRecord.getColumn();
                processingContent = "";
                break;
            case BoolErrRecord.sid:
                BoolErrRecord boolErrRecord = (BoolErrRecord)record;
                processingRowIndex = boolErrRecord.getRow();
                processingColumnIndex = boolErrRecord.getColumn();
                processingContent = "";
                break;
            case FormulaRecord.sid:
                FormulaRecord formulaRecord = (FormulaRecord)record;
                processingRowIndex = formulaRecord.getRow();
                processingColumnIndex = formulaRecord.getColumn();
                processingContent = doResolveContentForFormulaRecord(formulaRecord);
                break;
            case StringRecord.sid:
                if (outputNextStringRecord) {
                    StringRecord srec = (StringRecord)record;
                    processingContent = srec.getString();
                    processingRowIndex = nextRow;
                    processingColumnIndex = nextColumn;
                    outputNextStringRecord = false;
                }
                break;
            case LabelRecord.sid:
                LabelRecord lrec = (LabelRecord)record;
                processingRowIndex = lrec.getRow();
                processingColumnIndex = lrec.getColumn();
                processingContent = lrec.getValue();
                break;
            case LabelSSTRecord.sid:
                LabelSSTRecord lsrec = (LabelSSTRecord)record;
                processingRowIndex = lsrec.getRow();
                processingColumnIndex = lsrec.getColumn();
                if (sstRecord == null) {
                    processingContent = "";
                } else {
                    processingContent = sstRecord.getString(lsrec.getSSTIndex()).toString();
                }
                break;
            case NoteRecord.sid:
                NoteRecord nrec = (NoteRecord)record;
                processingRowIndex = nrec.getRow();
                processingColumnIndex = nrec.getColumn();
                // TODO: Find object to match nrec.getShapeId()
                processingContent = "(TODO)";
                break;
            case NumberRecord.sid:
                NumberRecord numrec = (NumberRecord)record;
                processingRowIndex = numrec.getRow();
                processingRowIndex = numrec.getColumn();
                processingContent = formatListener.formatNumberDateCell(numrec);
                break;
            case RKRecord.sid:
                RKRecord rkrec = (RKRecord)record;
                processingRowIndex = rkrec.getRow();
                processingColumnIndex = rkrec.getColumn();
                processingContent = "";
                break;
            default:
                break;
        }

        // Handle new row
        if (processingRowIndex != -1 && processingRowIndex != lastRowNumber) {
            lastColumnNumber = -1;
        }

        // Handle missing column
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord)record;
            processingRowIndex = mc.getRow();
            processingColumnIndex = mc.getColumn();
            processingContent = "";
        }

        // If we got something to print out, do so
        if (processingContent != null) {
            if (trimContent) {
                processingContent = processingContent.trim();
            }
            if (!"".equals(processingContent)) {
                notAllEmpty = true;
            }
            records.add(processingContent);
        }

        if (processingRowIndex > -1) {
            lastRowNumber = processingRowIndex;
        }
        if (processingColumnIndex > -1) {
            lastColumnNumber = processingColumnIndex;
        }

        // Handle end of row
        if (record instanceof LastCellOfRowDummyRecord) {
            int rowIndex = ((LastCellOfRowDummyRecord)record).getRow();
            doEndRow(rowIndex);
        }
    }

    private void init() {
        lastRowNumber = 0;
        lastColumnNumber = 0;
        nextRow = 0;
        nextColumn = 0;
        sheetIndex = 0;
        records = new ArrayList<String>();
        notAllEmpty = false;
        orderedBSRs = null;
        boundSheetRecords = new ArrayList<BoundSheetRecord>();
    }

    private void doProcessBOFRecord(BOFRecord record) {
        if (record.getType() != BOFRecord.TYPE_WORKSHEET) {
            return;
        }

        if (workbookBuildingListener != null && stubWorkbook == null) {
            stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
        }

        if (orderedBSRs == null) {
            orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
        }

        sheetIndex++;
        init();
    }

    private String doResolveContentForFormulaRecord(FormulaRecord record) {
        if (!outputFormulaValues) {
            return HSSFFormulaParser.toFormulaString(stubWorkbook, record.getParsedExpression());
        }

        if (Double.isNaN(record.getValue())) {
            outputNextStringRecord = true;
            nextRow = record.getRow();
            nextColumn = record.getColumn();
            return null;
        } else {
            return formatListener.formatNumberDateCell(record);
        }
    }

    private void doEndRow(int rowIndex) {
        if (sheetIndex >= 0  && sheetIndex < workbookConfig.getSheets().size()) {
            try {
                String[] rowContent = new String[records.size()];
                records.toArray(rowContent);
                SheetConfig sheetConfig = workbookConfig.getSheets().get(sheetIndex);
                Object rowData = RowDataAssembler.assemble(sheetConfig.getRowType(), sheetConfig.getDataRowConfig(), rowContent, use1904DateWindowing);
                rowReadingListener.onFinish(sheetConfig, rowData, rowIndex);
            } catch (Exception e) {
                logger.error(ExceptionUtil.stackTraceText(e));
            }
        }
        records.clear();
        lastColumnNumber = -1;
        notAllEmpty = false;
    }
}
