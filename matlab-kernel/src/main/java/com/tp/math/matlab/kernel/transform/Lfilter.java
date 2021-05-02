package com.tp.math.matlab.kernel.transform;

import com.tp.math.matlab.kernel.util.NumberFormatUtils;
import com.tp.math.matlab.kernel.util.PythonUtils;
import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-02
 */
@Slf4j
@Data
@Accessors(chain = true)
public class Lfilter {

    /**
     * The numerator coefficient vector in a 1-D sequence.
     */
    private List<Double> firArray;
    /**
     * An N-dimensional input array.
     */
    private List<Double> inputArray;
    /**
     * result
     */
    private List<Double> result;

    public Lfilter(
            @NonNull final List<Double> firArray,
            @Nullable final List<Double> inputArray
    ) {
        this.firArray = firArray;
        this.inputArray = inputArray;
        this.result = transform();
    }

    /**
     * 格式化结果
     */
    public List<String> format() {
        return this.getResult().stream()
                .map(NumberFormatUtils::roundToString)
                .collect(Collectors.toList());
    }

    /**
     * python scipy.signal.lfilter
     */
    private List<Double> transform() {
        final double[] b = this.firArray.stream().mapToDouble(i -> i).toArray();
        final List<Double> out_full = PythonUtils.apply_along_axis(b, this.inputArray.stream().mapToDouble(i -> i).toArray());
        final List<Double> out = out_full.stream().limit(this.inputArray.size()).collect(toList());
        return out;
    }
}
