package com.tp.math.matlab.util;

import lombok.experimental.UtilityClass;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class NumericDigitsUtils {

    public static int getNumberDigits(final double d, final int limit) {
        return Math.max(getIntegerDigits(d) + getDecimalDigits(d), limit);
    }

    /**
     * 计算double的整数有多少位
     *
     * @param d
     * @return
     */
    private static int getIntegerDigits(final double d) {
        return String.valueOf(d > 0 ? d : -d).length();
    }

    /**
     * 计算double的小数有多少位
     *
     * @param d
     * @return
     */
    private static int getDecimalDigits(final double d) {
        if (d == (long) d) {
            return 0;
        }
        int i = 0;
        while (true) {
            i++;
            if (d * Math.pow(10, i) % 1 == 0) {
                return i;
            }
        }
    }
}
