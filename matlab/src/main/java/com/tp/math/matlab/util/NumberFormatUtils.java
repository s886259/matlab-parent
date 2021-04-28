package com.tp.math.matlab.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class NumberFormatUtils {
    //科学技术计数法保留5位有效数字
    private static final DecimalFormat df = new DecimalFormat("0.####E0");

    static {
        df.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static String roundToString(final double d) {
        if (d == 0) {
            return String.valueOf(0);
        }
        return scientificNotation2String(d);
    }

    private static String scientificNotation2String(@NonNull final Double d) {
        return new BigDecimal(df.format(d)).toString();
    }

}
