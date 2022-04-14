package com.tp.matlab.kernel.util;

import cn.hutool.core.util.NumberUtil;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.NumberFormat;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class NumberFormatUtils {

    private static final String DECIMAL_FORMAT = "0.####E0";

    public static String roundToString(final double d) {
        if (d == 0) {
            return String.valueOf(0);
        }
        return NumberUtil.decimalFormat(DECIMAL_FORMAT, d);
    }

    private static BigDecimal roundToDecimal(final double d) {
        if (d == 0) {
            return new BigDecimal(0);
        }
        return new BigDecimal(NumberUtil.decimalFormat(DECIMAL_FORMAT, d));
    }

    public static Double roundToDouble(final double d) {
       return roundToDecimal(d).doubleValue();
    }

}
