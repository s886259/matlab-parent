package com.tp.math.matlab.core;

import com.tp.math.matlab.util.NumberFormatUtils;
import com.tp.math.matlab.util.NumericDigitsUtils;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

/**
 * Created by tangpeng on 2021-04-27
 */
@Data
@Builder
public class NumericDigits {
    /**
     * matlab最多保留5个有效数字
     */
    private static final int MATLAB_NUMERIC_FORMAT_SHORT_G = 5;

    /**
     * 原始值
     */
    @NonNull
    private Double originNumber;
    /**
     * 总共有多少位
     */
    @NonNull
    private Integer totalDigits;
    /**
     * 整数有多少位
     */
    @NonNull
    private Integer intDigits;
    /**
     * 小数有多少位
     */
    @NonNull
    private Integer decimalDigits;

    public static NumericDigits of(@NonNull final double d) {
        final int intDigits = NumericDigitsUtils.getIntDigits((long) d);
        final int decimalDigits = NumericDigitsUtils.getDecimalDigits(d);
        return new NumericDigitsBuilder()
                .originNumber(d)
                .intDigits(intDigits)
                .decimalDigits(decimalDigits)
                .totalDigits(intDigits + decimalDigits)
                .build();
    }

    @Override
    public String toString() {
        return NumberFormatUtils.roundToString(this.originNumber, true);
    }
}
