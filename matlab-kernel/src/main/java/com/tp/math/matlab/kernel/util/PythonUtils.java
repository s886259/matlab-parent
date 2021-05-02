package com.tp.math.matlab.kernel.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.MathArrays;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.sin;

/**
 * Created by tangpeng on 2021-05-02
 */
@UtilityClass
public class PythonUtils {

    /**
     * python apply_along_axis
     */
    public static List<Double> apply_along_axis(
            @NonNull final double[] fir,
            @NonNull final double[] arr
    ) {
        return Arrays.stream(MathArrays.convolve(fir, arr))
                .boxed().collect(Collectors.toList());
    }


    /**
     * python sinc
     */
    public static double sinc(@NonNull final Double x) {
        final double y = Math.PI * (x == 0 ? 1.0e-20 : x);
        return sin(y) / y;
    }
}
