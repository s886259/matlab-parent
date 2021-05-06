package com.tp.matlab.kernel.core;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Created by tangpeng on 2021-04-27
 */
@UtilityClass
public class ComplexConvertUtils {

    /**
     * 转换结果值
     *
     * @param real 实部值
     * @param imag 虚部值
     */
    public static ResultComplex convertToResultComplex(
            @NonNull final Double real,
            @NonNull final Double imag
    ) {
        return ResultComplex.of(real, imag);
    }
}
