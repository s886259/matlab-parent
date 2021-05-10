package com.tp.matlab.extension.acceleration.core;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
class ValueOfKurtosis {

    /**
     * 源数据
     */
    private final List<Double> a;
    private final Double vmean;
    private final Double sigma;

    public double execute() {
        final int n = this.a.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow((this.a.get(i) - vmean) / sigma, 4);
        }
        final double kur = sum / n;
        return kur;
    }
}
