package com.tp.matlab.extension.velocity.common.core;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
public class ValueOfSkeness {

    /**
     * 源数据
     */
    private final List<Double> a;
    private final Double vmean;

    public double execute() {
        final int n = this.a.size();
        double m = 0;
        double s = 0;
        for (int i = 0; i < n; i++) {
            m = m + Math.pow(this.a.get(i) - vmean, 3);
            s = s + Math.pow(this.a.get(i) - vmean, 2);
        }
        final double ske = (m / n) / Math.pow(s / (n - 1), 1.5);
        return ske;
    }
}
