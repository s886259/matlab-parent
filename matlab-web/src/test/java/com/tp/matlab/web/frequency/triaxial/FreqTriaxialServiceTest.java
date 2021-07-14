package com.tp.matlab.web.frequency.triaxial;

import com.tp.matlab.kernel.util.FileUtils;
import com.tp.matlab.web.frequency.envolope.service.FreqTriaxialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.tp.matlab.kernel.util.ExcelUtils.xlsRead;
import static com.tp.matlab.web.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;
import static com.tp.matlab.web.base.AbstractTransformTest.TIME_DOMAIN_TEST_EXCEL;

/**
 * Created by tangpeng on 2021-07-13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FreqTriaxialServiceTest {

    @Autowired
    private FreqTriaxialService freqTriaxialService;

    @Test
    public void testMain() throws IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(TIME_DOMAIN_TEST_EXCEL).getFile();
        final List<Double> records = xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX);
        //save source input file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, TEST_EXCEL_COLUMN_INDEX), records);
        final List<List<Double>> lists = new ArrayList<>();
        lists.add(xlsRead(fileName, 1));
        lists.add(xlsRead(fileName, 2));
        lists.add(xlsRead(fileName, 3));
        lists.add(xlsRead(fileName, 4));
        lists.add(xlsRead(fileName, 5));
        lists.add(xlsRead(fileName, 6));
        freqTriaxialService.execute(lists);
    }
}
