package io.github.chensheng.dddboot.excel;

import io.github.chensheng.dddboot.tools.collection.CollectionUtil;
import io.github.chensheng.dddboot.tools.text.EscapeUtil;
import io.github.chensheng.dddboot.tools.text.TextUtil;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class ServletExcelUtil {

    /**
     * Write data to excel by {@link HttpServletResponse}
     * @param response required
     * @param models required, not empty
     * @param fileName required, not empty
     * @throws IOException if network errors occur
     */
    public static void write(HttpServletResponse response, List<?> models, String fileName) throws IOException {
        write(response, models, fileName, null);
    }

    /**
     * Write data to template excel by {@link HttpServletResponse}
     * @param response required
     * @param models required, not empty
     * @param fileName required, not empty
     * @param templateFile optional
     * @throws IOException if network errors occur
     */
    public static void write(HttpServletResponse response, List<?> models, String fileName, File templateFile) throws IOException {
        if (response == null) {
            throw new IllegalArgumentException("response must not be null");
        }
        if (CollectionUtil.isEmpty(models)) {
            throw new IllegalArgumentException("models must not be empty");
        }
        if (TextUtil.isEmpty(fileName)) {
            throw new IllegalArgumentException("fileName must not be empty");
        }

        ServletOutputStream outputStream = response.getOutputStream();
        response.setContentType("multipart/form-data");
        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-disposition", "attachment;filename=" + EscapeUtil.urlEncode(fileName) + ".xlsx");

        InputStream templateIs = null;
        if (templateFile != null) {
            templateIs = new FileInputStream(templateFile);
        }
        ExcelUtil.write(outputStream, models, templateIs);
        outputStream.flush();
    }
}
