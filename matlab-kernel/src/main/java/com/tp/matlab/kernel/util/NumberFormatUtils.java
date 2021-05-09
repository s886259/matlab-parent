package com.tp.matlab.kernel.util;

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
    //科学技术计数法保留4位小数
    private static final DecimalFormat FOUR_DECIMAL_DF = new DecimalFormat("#.0000");
    //科学技术计数法保留5位有效数字
    private static final DecimalFormat FIVE_DIGITS_DF = new DecimalFormat("0.####E0");

    static {
        FIVE_DIGITS_DF.setRoundingMode(RoundingMode.HALF_UP);
        FOUR_DECIMAL_DF.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static String roundToFiveDigits(final double d) {
        if (d == 0) {
            return String.valueOf(0);
        }
        return scientificNotation2String(d, FIVE_DIGITS_DF);
    }

    /**
     * 保留小数点后4位 e.g 54.0373
     */
    public static BigDecimal roundToFourDecimal(final double d) {
        if (d == 0) {
            return new BigDecimal(0);
        }
        return new BigDecimal(scientificNotation2String(d, FOUR_DECIMAL_DF));
    }

    private static String scientificNotation2String(@NonNull final Double d, @NonNull DecimalFormat df) {
        return new BigDecimal(df.format(d)).toString();
    }

}
