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
public class ValueOfSigma {

    /**
     * 源数据
     */
    private List<Double> a;
    private Double vmean;
    /**
     * result
     */
    @Getter
    private double result;

    public ValueOfSigma(
            @NonNull final List<Double> a,
            @NonNull final Double vmean
    ) {
        this.a = a;
        this.vmean = vmean;
        this.result = transform();
    }

    private double transform() {
        final int n = this.a.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow((this.a.get(i) - vmean), 2);
        }
        return Math.sqrt(sum / n);
    }
}
