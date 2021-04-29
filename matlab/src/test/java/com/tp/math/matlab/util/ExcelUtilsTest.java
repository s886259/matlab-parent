package com.tp.math.matlab.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL;
import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL_ROW_SIZE;

/**
 * Created by tangpeng on 2021-04-26
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelUtilsTest {

    @Test
    public void testReadByColumn() throws InvalidFormatException, IOException {
        final String fileName = this.getClass().getResource(TEST_EXCEL).getFile();
        final Map<Integer, List<Double>> result = ExcelUtils.readAllColumns(fileName);
        final List<Double> records = result.get(0);
        System.out.println(records);
        Assert.assertEquals(records.size(), TEST_EXCEL_ROW_SIZE);
    }

}
