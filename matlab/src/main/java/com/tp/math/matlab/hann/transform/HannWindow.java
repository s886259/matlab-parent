package com.tp.math.matlab.hann.transform;

import Spectrogram.WindowFunction;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

import static Spectrogram.WindowFunction.HANNING;

/**
 * Created by tangpeng on 2021-04-24
 */
@UtilityClass
public class HannWindow {

    /**
     * hanning windows algorithm
     *
     * @param length
     * @return
     */
    public static List<Double> transform(final int length) {
        return DoubleStream.of(WindowFunction.getWindowFunc(HANNING, length))
                .boxed()
                .collect(Collectors.toList());
    }
}
