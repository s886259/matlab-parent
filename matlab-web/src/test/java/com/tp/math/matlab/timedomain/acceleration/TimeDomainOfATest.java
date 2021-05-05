package com.tp.math.matlab.timedomain.acceleration;

import com.tp.math.matlab.extension.acceleration.core.TimeDomainOfA;
import com.tp.math.matlab.kernel.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;
import static com.tp.math.matlab.base.AbstractTransformTest.TIME_DOMAIN_TEST_EXCEL;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeDomainOfATest {

    @Test
    public void testMain() throws IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(TIME_DOMAIN_TEST_EXCEL).getFile();
        //a=xlsRead('1414.xlsx',2);
        //inputArray=a(:,8);
        final List<Double> a = ExcelUtils.xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX - 1);
        final TimeDomainOfA main = new TimeDomainOfA();
        main.run(a);
    }
}
