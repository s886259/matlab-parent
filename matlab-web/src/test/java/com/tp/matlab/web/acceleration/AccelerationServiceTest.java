package com.tp.matlab.web.acceleration;

import com.tp.math.matlab.kernel.util.ExcelUtils;
import com.tp.matlab.web.acceleration.service.AccelerationService;
import com.tp.matlab.web.base.AbstractTransformTest;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class AccelerationServiceTest {

    @Autowired
    private AccelerationService accelerationService;

    @Test
    public void testMain() throws IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(AbstractTransformTest.TIME_DOMAIN_TEST_EXCEL).getFile();
        final List<Double> a = ExcelUtils.xlsRead(fileName, AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX - 1);
        accelerationService.run(a);
    }
}
