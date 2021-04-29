package com.tp.math.matlab.hann.transform;

import com.tp.math.matlab.util.NumberFormatUtils;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by tangpeng on 2021-04-24
 */
@UtilityClass
public class HannWindow {

    /**
     * hanning windows algorithm
     *
     * @param length
     */
    public static List<String> transform(final int length) {

        return IntStream.range(0, length)
                .mapToDouble(i -> 0.5 * (1 - Math.cos((2 * Math.PI * i) / (length - 1))))
                .boxed()
                .map(NumberFormatUtils::roundToString)
                .collect(Collectors.toList());

    }
}
