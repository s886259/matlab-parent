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
        if (intDigits > MATLAB_NUMERIC_FORMAT_SHORT_G) {
//            throw new IllegalArgumentException("intDigits must less than 5");
            //整数位超过5位后,转化为幂次显示
        }
        final int decimalDigits = NumericDigitsUtils.getDecimalDigits(d);
        return new NumericDigitsBuilder()
                .originNumber(d)
                .intDigits(intDigits)
                .decimalDigits(decimalDigits)
                .totalDigits(intDigits + decimalDigits)
                .build();
    }

    String formatShortG() {
//        if (this.totalDigits <= MATLAB_NUMERIC_FORMAT_SHORT_G) {
//            //总有效位小于等于5,不处理
//            return this.originNumber.toString();
//        } else if (this.intDigits > MATLAB_NUMERIC_FORMAT_SHORT_G) {
//            //整数有效位大于5,转换为科学计数法并保留5位有效数字
//            return scientificNotation2String(this.originNumber);
//        } else {
//            final int scale = Math.min(this.totalDigits - this.intDigits, MATLAB_NUMERIC_FORMAT_SHORT_G - this.intDigits);
//            return String.valueOf(NumberFormatUtils.round(this.originNumber, scale));
//        }
        return NumberFormatUtils.roundToString(this.originNumber, true);
    }
}
