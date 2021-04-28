package com.tp.math.matlab;

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
        //new BigDecimal(-1.09E+3) 内部stringCache="-1090"
        Assert.assertEquals(new BigDecimal(-1090), new BigDecimal(-1.09E+3));
        //new BigDecimal("-1.09E+3") 内部stringCache="-1.09E+3"
        Assert.assertNotEquals(new BigDecimal("-1090"), new BigDecimal("-1.09E+3"));
        //new BigDecimal(Double.valueOf("-1.09E+3")) 内部stringCache="1090" 等同于new BigDecimal(1090)
        Assert.assertEquals(new BigDecimal("-1090"), new BigDecimal(Double.valueOf("-1.09E+3")));
        Assert.assertEquals(new BigDecimal(Double.valueOf("-1090")), new BigDecimal(Double.valueOf("-1.09E+3")));
    }
}
