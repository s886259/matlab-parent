package com.tp.math.matlab.base;

import com.tp.math.matlab.util.FileUtils;
import lombok.NonNull;
import org.hamcrest.Matchers;
import org.junit.Assert;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by tangpeng on 2021-04-29
 */
public abstract class AbstractTransformTest {

    public static final String TEST_EXCEL = "/1414.xlsx";
    public static final int TEST_EXCEL_ROW_SIZE = 8192;
    public static final int TEST_EXCEL_COLUMN_INDEX = 8;
    protected static final String FFT_TEST_METHOD = "fft";
    protected static final String IFFT_TEST_METHOD = "ifft";
    protected static final String MATLAB_RESULT_FILE_FORMAT = TEST_EXCEL + "_%s_column" + TEST_EXCEL_COLUMN_INDEX + "_matlab_output.txt";
    protected static final String MY_RESULT_FILE_FORMAT = TEST_EXCEL.replace("/", "")
            + "_%s_column" + TEST_EXCEL_COLUMN_INDEX + "_my_output.txt";

    protected void testFFT(
            @NonNull final List<String> actuals,
            @NonNull final String matlabResultFile
    ) throws IOException, URISyntaxException {
        //matlab output
        final List<String> expects = Files.lines(Paths.get(this.getClass().getResource(matlabResultFile).toURI()))
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
            /**
             * compare to BigDecimal
             * https://stackoverflow.com/questions/35573187/junit-assert-with-bigdecimal
             */
            //compare with real
            assertThat(new BigDecimal(expectArr[0].trim()),
                    Matchers.comparesEqualTo(new BigDecimal(actualArr[0].trim())));
            //compare with imag
            final String expectImag = expectArr[1].trim();
            final String actualImag = actualArr[1].trim();
            Assert.assertTrue(expectImag.contains("i"));
            Assert.assertTrue(actualImag.contains("i"));
            assertThat(new BigDecimal(expectImag.replace("i", "")),
                    Matchers.comparesEqualTo(new BigDecimal(actualImag.replace("i", ""))));
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
