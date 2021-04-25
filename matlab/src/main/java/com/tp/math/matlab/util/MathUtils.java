package com.tp.math.matlab.util;

import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by tangpeng on 2021-04-25
 */
@UtilityClass
public class MathUtils {

    public static String roundToString(double d, boolean formatZero) {
        double round = round(d);
        if (round == 0 && formatZero) {
            return String.valueOf(0);
        }
        return new DecimalFormat("#0.0000").format(round);
    }

    private static double round(double d) {
        BigDecimal b = new BigDecimal(d);
        d = b.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue();
        return d;
    }
}
