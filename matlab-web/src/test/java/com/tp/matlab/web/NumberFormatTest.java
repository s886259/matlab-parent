package com.tp.matlab.web;

import cn.hutool.core.util.NumberUtil;
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

        //https://www.bookstack.cn/read/hutool-5.6.0-zh/6e53489888a8802d.md
        System.out.println(NumberUtil.decimalFormat("0.####E0", new BigDecimal("7.075069696771408e-15").doubleValue()));
        System.out.println(new BigDecimal(NumberUtil.decimalFormat("0.####E0", 3.1250d)));

    }
}
