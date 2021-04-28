package com.tp.math.matlab;

import com.tp.math.matlab.ifft.service.IFFTService;
import com.tp.math.matlab.util.FileUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IFFTTransformerTest {

    private static final String TEST_METHOD = "ifft";
    private static final String TEST_EXCEL = "/1414.xlsx";
    private static final int TEST_EXCEL_COLUMN_INDEX = 8;
    private static final String MATLAB_RESULT_FILE = String.format(
            "%s_%s_column%s_matlab_output.txt", TEST_EXCEL, TEST_METHOD, TEST_EXCEL_COLUMN_INDEX);

    @Autowired
    private IFFTService service;

    @Test
    public void testIFFT() throws IOException, InvalidFormatException, URISyntaxException {
        //my output
        final List<String> actuals = service.transformFromFile(
                this.getClass().getResource(TEST_EXCEL).getFile(),
                TEST_EXCEL_COLUMN_INDEX
        );
        //matlab output
        final List<String> expects = Files.lines(Paths.get(this.getClass().getResource(MATLAB_RESULT_FILE).toURI()))
                .map(String::trim)
                .collect(toList());
        //compare my output with matlab output
        Assert.assertEquals(actuals.size(), expects.size());
        for (int i = 0; i < expects.size(); i++) {
            final String expect = expects.get(i);
            final String actual = actuals.get(i);
            System.out.println(String.format("index=%s, expect=%s, actual=%s", i, expect, actual));
            final String splitFlag = expect.contains(" + ") ? "\\s+\\+\\s+" : "\\s+-\\s+";
            //split
            final String[] expectArr = expect.split(splitFlag);
            final String[] actualArr = actual.split(splitFlag);
            //compare with real
            Assert.assertEquals(new BigDecimal(expectArr[0].trim()), new BigDecimal(actualArr[0].trim()));
            //compare with imag
            final String expectImag = expectArr[1].trim();
            final String actualImag = actualArr[1].trim();
            Assert.assertTrue(expectImag.contains("i"));
            Assert.assertTrue(actualImag.contains("i"));
            Assert.assertEquals(new BigDecimal(expectImag.replace("i", "")), new BigDecimal(actualImag.replace("i", "")));
        }
    }

    @Test
    public void testTransformFromFile() throws IOException, InvalidFormatException {
        String fileName = "~/Documents/matlab/excels" + TEST_EXCEL;
        int columnIndex = 8;
        List<String> result = service.transformFromFile(fileName, columnIndex);

        FileUtils.string2File(String.format("%s_ifft_column%s_my_output.txt", fileName, columnIndex), result);
        result.forEach(System.out::println);
    }
}
