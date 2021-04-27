package com.tp.math.matlab;

import com.tp.math.matlab.ifft.service.IFFTService;
import com.tp.math.matlab.util.FileUtils;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class IFFTTransformerTest {

    @Autowired
    private IFFTService service;

    @Test
    public void testIFFT() {
        String str = "-14.66493271,-20.38126362,-1.795706046,-15.44307199,-16.61028092,-0.329212775,-18.28627323,-21.81782846";

        List<String> result = service.transform(
                Arrays.stream(str.split(","))
                        .map(i -> new Complex(Double.valueOf(i)))
                        .toArray(Complex[]::new));
        result.forEach(System.out::println);
    }

    @Test
    public void testTransformFromFile() throws IOException, InvalidFormatException {
        String fileName = "/Users/tangpeng/Documents/matlab/excels/1414.xlsx";
        int columnIndex = 8;
        List<String> result = service.transformFromFile(fileName, columnIndex);

        FileUtils.string2File(String.format("%s_ifft_column%s_my_output.txt", fileName, columnIndex), result);
        result.forEach(System.out::println);
    }
}
