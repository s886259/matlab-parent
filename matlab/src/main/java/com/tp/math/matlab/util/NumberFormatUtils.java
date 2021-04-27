package com.tp.math.matlab.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class NumberFormatUtils {
    //科学技术计数法保留5位有效数字
    private static final DecimalFormat decimalFormat = new DecimalFormat("0.####E0");

    public static String scientificNotation2String(@NonNull final Double d) {
        return new BigDecimal(decimalFormat.format(d)).toString();
    }

}
