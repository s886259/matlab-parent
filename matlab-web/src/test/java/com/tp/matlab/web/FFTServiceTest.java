package com.tp.matlab.web;

import com.tp.matlab.web.base.AbstractTransformTest;
import com.tp.matlab.web.service.FFTService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class FFTServiceTest extends AbstractTransformTest {

    private static final String MATLAB_RESULT_FILE = String.format(MATLAB_RESULT_FILE_FORMAT, FFT_TEST_METHOD);

    @Autowired
    private FFTService service;

    @Test
    public void testFFT() throws IOException, InvalidFormatException, URISyntaxException {
        //my output
        final List<String> actuals = service.transformFromFile(
                this.getClass().getResource(TEST_EXCEL).getFile(),
                TEST_EXCEL_COLUMN_INDEX
        );
        super.testFFT(actuals, MATLAB_RESULT_FILE);
    }


    @Test
    public void testTransformFromFile() throws IOException, InvalidFormatException {
        final String fileName = this.getClass().getResource(TIME_DOMAIN_TEST_EXCEL).getFile();
        final List<String> result = service.transformFromFile(fileName, TEST_EXCEL_COLUMN_INDEX);

        super.testTransformFromFile(String.format(MY_RESULT_FILE_FORMAT, FFT_TEST_METHOD), result);
    }
}
