package com.tp.math.matlab.fft.transform;

import com.tp.math.matlab.core.NumericDigits;
import com.tp.math.matlab.core.ResultComplex;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class ComplexConvertUtils {

    public static ResultComplex convertToResultComplex(
            @NonNull final Double real,
            @NonNull final Double imag
    ) {
        return ResultComplex.of(real, imag)
                .setRealNumericDigits(NumericDigits.of(real))
                .setImagNumericDigits(NumericDigits.of(imag));
    }
}
