package com.tp.matlab.kernel.util;

import com.github.psambit9791.jdsp.signal.Detrend;
import com.tp.matlab.kernel.core.ValueWithIndex;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import org.apache.commons.math3.util.MathArrays;
import org.springframework.lang.Nullable;

import java.util.Arrays;
import java.util.LinkedList;
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
public class MatlabUtils {

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
    public static ValueWithIndex getMax(
            @NonNull final List<Double> k,
            @Nullable final Integer limit
    ) {
        final Integer newLimit = Optional.ofNullable(limit).orElse(k.size());
        final double val = k.stream().limit(newLimit)
                .mapToDouble(i -> i)
                .max()
                .getAsDouble();
        final int index = IntStream.range(0, newLimit).reduce((a, b) -> k.get(a) < k.get(b) ? b : a).getAsInt();
        return ValueWithIndex.of(val, index + 1);
    }

    public static ValueWithIndex getMin(@NonNull final List<Double> k) {
        final double val = k.stream().limit(k.size())
                .mapToDouble(i -> i)
                .min()
                .getAsDouble();
        final int index = IntStream.range(0, k.size()).reduce((a, b) -> k.get(a) < k.get(b) ? a : b).getAsInt();
        return ValueWithIndex.of(val, index + 1);
    }

    /**
     * https://www.sanfoundry.com/java-program-find-peak-element-array-binary-search-approach/
     */
    public static ValueWithIndex getMax(@NonNull final List<Double> k) {
        return getMax(k, null);
    }

    /**
     * https://codereview.stackexchange.com/questions/223301/find-all-local-maxima-in-a-one-dimensional-array
     */
    public static List<ValueWithIndex> findPeaks(@NonNull final List<Double> list) {
        final List<ValueWithIndex> peaks = new LinkedList<>();
        for (int i = 1; i < list.size() - 1; i++) {
            if (list.get(i - 1) < list.get(i) && list.get(i) > list.get(i + 1)) {
                peaks.add(ValueWithIndex.of(list.get(i), i + 1));
            }
        }
        return peaks;
    }

}
