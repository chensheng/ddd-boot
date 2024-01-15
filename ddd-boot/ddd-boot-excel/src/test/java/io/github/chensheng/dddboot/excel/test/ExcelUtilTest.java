package io.github.chensheng.dddboot.excel.test;

import io.github.chensheng.dddboot.excel.ExcelUtil;
import io.github.chensheng.dddboot.excel.core.NumericUtil;
import io.github.chensheng.dddboot.tools.io.FileUtil;
import io.github.chensheng.dddboot.tools.number.RandomUtil;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtilTest {
    @Test
    public void testExport() throws IOException {
        List<ExcelDto> excelDtoList = new ArrayList<ExcelDto>();
        for(int i = 0; i < 100; i++) {
            ExcelDto excelDto = new ExcelDto();
            excelDto.setFieldString("字符串" + RandomUtil.randomStringFixLength(10));
            excelDto.setFieldInteger(RandomUtil.nextInt(1, 100000));
            Double doubleValue = RandomUtil.nextDouble(0, 100000);
            String doubleText = NumericUtil.formatNumericInNeed(String.valueOf(doubleValue), 4);
            excelDto.setFieldDouble(Double.parseDouble(doubleText));
            excelDto.setFieldDate(new Date(System.currentTimeMillis() + i * 60 * 1000));
            excelDtoList.add(excelDto);
        }

        Path path = FileUtil.createTempFile();
        File file = path.toFile();
        ExcelUtil.write(new FileOutputStream(file), excelDtoList);

        List<ExcelDto> readExcelDtoList = ExcelUtil.read(new FileInputStream(file), ExcelDto.class);
        Assert.assertNotNull(readExcelDtoList);
        Assert.assertEquals(excelDtoList.size(), readExcelDtoList.size());
        for(int i = 0; i < excelDtoList.size(); i++) {
            ExcelDto originalDto = excelDtoList.get(i);
            ExcelDto readDto = readExcelDtoList.get(i);
            Assert.assertEquals(originalDto.getFieldString(), readDto.getFieldString());
            Assert.assertEquals(originalDto.getFieldInteger(), readDto.getFieldInteger());
            Assert.assertEquals(originalDto.getFieldDouble(), readDto.getFieldDouble());
            Assert.assertEquals(originalDto.getFieldDate().getTime(), readDto.getFieldDate().getTime());
        }
    }
}
