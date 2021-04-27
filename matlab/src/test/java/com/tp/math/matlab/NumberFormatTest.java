package com.tp.math.matlab;

import com.tp.math.matlab.util.NumberFormatUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

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
        double d = 123153.10251667173d;
        String format = NumberFormatUtils.scientificNotation2String(d);
        System.out.println(format);
    }
}
