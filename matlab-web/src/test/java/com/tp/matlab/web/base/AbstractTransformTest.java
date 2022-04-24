package com.tp.matlab.web.base;

import com.tp.matlab.kernel.util.FileUtils;
import com.tp.matlab.web.util.AssertUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-29
 */
@Slf4j
public abstract class AbstractTransformTest {

    public static final String TEST_EXCEL = "/1414.xlsx";
    public static final String TEST_VECTOR_AMPLITUDE_EXCEL = "/1.xlsx";
    protected static final String TIME_DOMAIN_TEST_EXCEL = "/1417.xlsx";
    //开机测试文件
    public static final String TEST_POWER_ON_EXCEL = "/testPowerOn.xlsx";
    //关机机测试文件
    public static final String TEST_POWER_OFF_EXCEL = "/testPowerOff.xlsx";

    public static final int TEST_EXCEL_ROW_SIZE = 8192;
    public static final int TEST_EXCEL_COLUMN_INDEX = 8;
    protected static final String FFT_TEST_METHOD = "fft";
    protected static final String IFFT_TEST_METHOD = "ifft";
    protected static final String MATLAB_RESULT_FILE_FORMAT = TEST_EXCEL + "_%s_column" + TEST_EXCEL_COLUMN_INDEX + "_matlab_output.txt";
    protected static final String MY_RESULT_FILE_FORMAT = TEST_EXCEL.replace("/", "")
            + "_%s_column" + TEST_EXCEL_COLUMN_INDEX + "_my_output.txt";
    //开机测试文件

    protected void testFFT(
            @NonNull final List<String> actuals,
            @NonNull final String matlabResultFile
    ) throws IOException, URISyntaxException {
        //matlab output
        final List<String> expectds = Files.lines(Paths.get(this.getClass().getResource(matlabResultFile).toURI()))
                .map(String::trim)
                .collect(toList());
        //compare my output with matlab output
        Assert.assertEquals(actuals.size(), expectds.size());
        for (int i = 0; i < expectds.size(); i++) {
            final String expectd = expectds.get(i);
            final String actual = actuals.get(i);
            log.info(String.format("index=%s, expectd=%s, actual=%s", i, expectd, actual));
            final String splitFlag = expectd.contains(" + ") ? "\\s+\\+\\s+" : "\\s+-\\s+";
            //split
            final String[] expectArr = expectd.split(splitFlag);
            final String[] actualArr = actual.split(splitFlag);
            /**
             * compare to BigDecimal
             * https://stackoverflow.com/questions/35573187/junit-assert-with-bigdecimal
             */
            //compare with real
            AssertUtils.assertEquals(new BigDecimal(expectArr[0].trim()), new BigDecimal(actualArr[0].trim()));
            //compare with imag
            final String expectdImag = expectArr[1].trim();
            final String actualImag = actualArr[1].trim();
            Assert.assertTrue(expectdImag.contains("i"));
            Assert.assertTrue(actualImag.contains("i"));
            AssertUtils.assertEquals(new BigDecimal(expectdImag.replace("i", "")),
                    new BigDecimal(actualImag.replace("i", "")));
        }
    }

    protected void testTransformFromFile(
            @NonNull final String fileName,
            @NonNull final List<String> result
    ) throws IOException {
        FileUtils.string2File(fileName, result);
        result.forEach(System.out::println);
    }

}
