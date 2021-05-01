package com.tp.math.matlab.kernel.transform;

import Spectrogram.WindowFunction;
import com.tp.math.matlab.kernel.util.NumberFormatUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.stream.DoubleStream;

import static Spectrogram.WindowFunction.HANNING;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-24
 */
@Data
@Accessors(chain = true)
public class HannWindow {
    @NonNull
    private Integer length;
    private List<Double> result;

    public HannWindow(@NonNull final Integer length) {
        this.length = length;
        this.result = transform();
    }

    /**
     * hanning windows algorithm
     */
    private List<Double> transform() {
        return DoubleStream.of(WindowFunction.getWindowFunc(HANNING, this.length))
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
