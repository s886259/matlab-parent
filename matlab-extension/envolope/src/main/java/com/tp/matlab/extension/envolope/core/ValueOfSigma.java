package com.tp.matlab.extension.envolope.core;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-10
 */
@RequiredArgsConstructor
class ValueOfSigma {

    /**
     * 源数据
     */
    private final List<Double> a;
    private final Double vmean;

    public double execute() {
        final int n = this.a.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow((this.a.get(i) - vmean), 2);
        }
        return Math.sqrt(sum / n);
    }
}
