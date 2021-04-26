package com.tp.math.matlab;

import com.tp.math.matlab.fft.service.FastFourierTransformerService;
import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.util.ExcelUtils;
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
import java.util.Map;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MyFastFourierTransformerTest {

    @Autowired
    private FastFourierTransformerService service;

    @Test
    public void testFFT() {
//        String str = "-14.66493271,-20.38126362,-1.795706046,-15.44307199,-16.61028092,-0.329212775,-18.28627323,-21.81782846";
        String str = "-14.66493271,-20.38126362,-1.795706046,-15.44307199,-16.61028092,-0.329212775,-18.28627323,-21.81782846,0.209499039,1.047495193,0.748210852";

        List<String> result = service.transform(
                Arrays.stream(str.split(","))
                        .map(i -> new MyComplex(Double.valueOf(i)))
                        .toArray(MyComplex[]::new), TransformType.FORWARD);
        result.forEach(System.out::println);
    }

    @Test
    public void testTransformFromFile() throws IOException, InvalidFormatException {
        String fileName = "/Users/tangpeng/Documents/matlab/excels/1414.xlsx";
        List<String> result = service.transformFromFile(fileName, TransformType.FORWARD);
        result.forEach(System.out::println);
    }
}
