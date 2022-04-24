package com.tp.matlab.web.frequency.velocity.vibrator;

import com.tp.matlab.kernel.util.FileUtils;
import com.tp.matlab.web.frequency.envolope.service.FreqVibratorService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.tp.matlab.kernel.util.ExcelUtils.xlsRead;
import static com.tp.matlab.web.base.AbstractTransformTest.*;

/**
 * Created by tangpeng on 2021-05-11
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FreqVibratorServiceTest {

    @Autowired
    private FreqVibratorService freqVibratorService;


    @Test
    public void testOpen() throws IOException, InvalidFormatException {
        test(TEST_POWER_ON_EXCEL);
    }

    @Test
    public void testShutdown() throws IOException, InvalidFormatException {
        test(TEST_POWER_OFF_EXCEL);
    }

    private void test(String file) throws IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(file).getFile();
        final List<Double> records = xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX);
        //save source input file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, TEST_EXCEL_COLUMN_INDEX), records);
        freqVibratorService.execute(records);
    }
}
