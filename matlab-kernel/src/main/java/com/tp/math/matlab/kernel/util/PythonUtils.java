package com.tp.math.matlab.kernel.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import static java.lang.Math.sin;

/**
 * Created by tangpeng on 2021-05-02
 */
@UtilityClass
public class PythonUtils {
    /**
     * python sinc
     */
    public static double sinc(@NonNull final Double x) {
        final double y = Math.PI * (x == 0 ? 1.0e-20 : x);
        return sin(y) / y;
    }
}
