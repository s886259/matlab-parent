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

    public static String scientificNotation2String(@NonNull final Double d) {
        return new BigDecimal(df.format(d)).toString();
    }

    public static String roundToString(final double d, final boolean formatZero) {
        if (d == 0 && formatZero) {
            return String.valueOf(0);
        }
        return df.format(d);
    }

    public static double round(final double d, int scale) {
        BigDecimal b = new BigDecimal(d);
        return b.setScale(scale, RoundingMode.HALF_UP).doubleValue();
    }

}
