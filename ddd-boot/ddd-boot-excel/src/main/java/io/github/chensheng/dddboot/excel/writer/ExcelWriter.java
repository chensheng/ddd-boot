package io.github.chensheng.dddboot.excel.writer;

import java.io.InputStream;
import java.io.OutputStream;

public interface ExcelWriter {
    void write(OutputStream outputStream, InputStream templateIs, RowWritingListener rowWritingListener, Class<?>... rowTypes) throws Exception;
}
