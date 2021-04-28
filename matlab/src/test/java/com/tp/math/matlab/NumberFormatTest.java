package com.tp.math.matlab;

import com.tp.math.matlab.core.ResultComplex;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static com.tp.math.matlab.fft.transform.ComplexConvertUtils.convertToResultComplex;

/**
 * Created by tangpeng on 2021-04-27
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class NumberFormatTest {

    @Test
    public void testScientificNotation2String() {
        Assert.assertEquals(new BigDecimal("1.2315e+05"), new BigDecimal("1.2315E+5"));
//        double d = 123153.10251667173d;
//        String format = NumberFormatUtils.roundToString(d, true);
//        System.out.println(format);

        ResultComplex resultComplex = convertToResultComplex(-1090.033887098925, -53.67663624450831);
        System.out.println(resultComplex);
        Assert.assertEquals(resultComplex.getReal().toString(), new Double("-1090"));
    }
}
