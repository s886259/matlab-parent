package com.tp.math.matlab.kernel.transform;

import com.tp.math.matlab.kernel.util.NumberFormatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-24
 */
@Accessors(chain = true)
public class HannWindow {
    @NonNull
    private Integer length;
    @Getter
    private List<Double> result;

    public HannWindow(@NonNull final Integer length) {
        this.length = length;
        this.result = transform();
    }

    /**
     * hanning windows algorithm
     */
    private List<Double> transform() {
        final double[] window = new double[length];
        for (int i = 0; i < length; i++) {
            window[i] = (0.5 - 0.5 * Math.cos(2.0 * Math.PI
                    * ((double) (i) / (double) (length - 1))));
        }
        return DoubleStream.of(window)
                .boxed()
                .collect(toList());
    }

    /**
     * 格式化结果
     */
    public List<String> format() {
        return this.getResult().stream().map(NumberFormatUtils::roundToString).collect(toList());
    }
}
