package com.tp.matlab.web.vectoramplitude;

import com.tp.matlab.kernel.util.FileUtils;
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
import static com.tp.matlab.web.base.AbstractTransformTest.*;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Created by tangpeng on 2021-08-01
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class VectorAmplitudeServiceTest {

    @Autowired
    private VectorAmplitudeService vectorAmplitudeService;

    @Test
    public void testMain() throws IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(TEST_VECTOR_AMPLITUDE_EXCEL).getFile();
        final List<Double> records = xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX);
        //save source input file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, TEST_EXCEL_COLUMN_INDEX), records);
        final List<double[]> lists = new ArrayList<>();
        lists.add(toPrimitive(xlsRead(fileName, 1).toArray(new Double[0])));
        lists.add(toPrimitive(xlsRead(fileName, 2).toArray(new Double[0])));
        lists.add(toPrimitive(xlsRead(fileName, 3).toArray(new Double[0])));
        lists.add(toPrimitive(xlsRead(fileName, 4).toArray(new Double[0])));
        lists.add(toPrimitive(xlsRead(fileName, 5).toArray(new Double[0])));
        lists.add(toPrimitive(xlsRead(fileName, 6).toArray(new Double[0])));
        vectorAmplitudeService.execute(lists);
    }
}
