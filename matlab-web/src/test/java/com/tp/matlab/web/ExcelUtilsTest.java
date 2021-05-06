package com.tp.matlab.web;

import com.tp.matlab.kernel.util.ExcelUtils;
import com.tp.matlab.web.base.AbstractTransformTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.tp.matlab.web.base.AbstractTransformTest.TEST_EXCEL;
import static com.tp.matlab.web.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;

/**
 * Created by tangpeng on 2021-04-26
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelUtilsTest {

    @Test
    public void testXlsRead() throws InvalidFormatException, IOException {
        final String fileName = this.getClass().getResource(TEST_EXCEL).getFile();
        final List<Double> records = ExcelUtils.xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX);
        log.info(records.toString());
        Assert.assertEquals(records.size(), AbstractTransformTest.TEST_EXCEL_ROW_SIZE);
    }

}
