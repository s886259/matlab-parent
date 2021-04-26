package com.tp.math.matlab.fft.transform;

import com.tp.math.matlab.util.MathUtils;
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

    @NonNull
    private Double number;

//    /**
//     * 整数数字
//     */
//    @NonNull
//    private Long intNumeric;
//    /**
//     * 小数数字
//     */
//    @NonNull
//    private Long decimalNumeric;
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

    static NumericDigits of(@NonNull final double d) {
        final int intDigits = NumericDigitsUtils.getIntDigits((long) d);
        if (intDigits > MATLAB_NUMERIC_FORMAT_SHORT_G) {
            throw new IllegalArgumentException("intDigits must less than 5");
        }
        final int decimalDigits = NumericDigitsUtils.getDecimalDigits(d);
        return new NumericDigitsBuilder()
                .number(d)
                .intDigits(intDigits)
                .decimalDigits(decimalDigits)
                .totalDigits(intDigits + decimalDigits)
                .build();
    }

    public Double formatShortG() {
        if (this.totalDigits <= MATLAB_NUMERIC_FORMAT_SHORT_G) {
            return this.number;
        }
        final int scale = Math.min(this.totalDigits - this.intDigits, MATLAB_NUMERIC_FORMAT_SHORT_G - this.intDigits);
        return MathUtils.round(this.number, scale);
    }
}
