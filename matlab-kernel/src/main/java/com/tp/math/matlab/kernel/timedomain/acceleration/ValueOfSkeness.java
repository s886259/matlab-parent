package com.tp.math.matlab.kernel.timedomain.acceleration;

import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@Accessors(chain = true)
public class ValueOfSkeness {

    /**
     * 源数据
     */
    private List<Double> inputArray;
    private Double vmean;
    /**
     * result
     */
    @Getter
    private double result;

    public ValueOfSkeness(
            @NonNull final List<Double> inputArray,
            @NonNull final Double vmean
    ) {
        this.inputArray = inputArray;
        this.vmean = vmean;
        this.result = transform();
    }

    private double transform() {
        final int n = this.inputArray.size();
        double m = 0;
        double s = 0;
        for (int i = 0; i < n; i++) {
            m = m + Math.pow(this.inputArray.get(i) - vmean, 3);
            s = s + Math.pow(this.inputArray.get(i) - vmean, 2);
        }
        final double ske = (m / n) / Math.pow(s / (n - 1), 1.5);
        return ske;
    }
}
