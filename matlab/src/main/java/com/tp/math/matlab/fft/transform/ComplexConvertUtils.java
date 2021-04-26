package com.tp.math.matlab.fft.transform;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class ComplexConvertUtils {

    public static ResultComplex convertToResultComplex(@NonNull final OriginComplex originComplex) {
        return new ResultComplex(originComplex.real, originComplex.imag)
                .setRealNumericDigits(NumericDigits.of(originComplex.real))
                .setImagNumericDigits(NumericDigits.of(originComplex.imag));
    }
}
