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
public class ValueOfKurtosis {

    /**
     * 源数据
     */
    private List<Double> inputArray;
    private Double vmean;
    private Double sigma;
    /**
     * result
     */
    @Getter
    private double result;

    public ValueOfKurtosis(
            @NonNull final List<Double> inputArray,
            @NonNull final Double vmean,
            @NonNull final Double sigma
    ) {
        this.inputArray = inputArray;
        this.vmean = vmean;
        this.sigma = sigma;
        this.result = transform();
    }

    private double transform() {
        final int n = this.inputArray.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow((this.inputArray.get(i) - vmean) / sigma, 4);
        }
        final double kur = sum / n;
        return kur;
    }
}
