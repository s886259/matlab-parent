package com.tp.matlab.kernel.util;

import com.github.psambit9791.jdsp.signal.Detrend;
import com.tp.matlab.kernel.core.DoubleMax;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.MathArrays;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static java.lang.Math.sin;
import static java.util.stream.Collectors.toList;

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

    /**
     * 去除趋势项
     * <p>
     * scipy.signal.detrend
     */
    public static List<Double> detrend(@NonNull final List<Double> signal) {
        final Detrend d1 = new Detrend(signal.stream().mapToDouble(i -> i).toArray(), "linear");
        final double[] out = d1.detrendSignal();
        return DoubleStream.of(out).boxed().collect(toList());
//        Assertions.assertArrayEquals(result, out, 0.001);
    }


    /**
     * python max
     */
    public static DoubleMax getMax(
            @NonNull final List<Double> k,
            @Nullable final Integer limit
    ) {
        final Integer newLimit = Optional.ofNullable(limit).orElse(k.size());
        final double val = k.stream().limit(newLimit)
                .mapToDouble(i -> i)
                .max()
                .getAsDouble();
        final int index = IntStream.range(0, newLimit).reduce((a, b) -> k.get(a) < k.get(b) ? b : a).getAsInt();
        return DoubleMax.of(val, index);
    }

    public static DoubleMax getMax(
            @NonNull final List<Double> k) {
        return getMax(k, null);
    }

}
