package com.tp.matlab.extension.time.acceleration;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
class MeanValue {

    /**
     * 源数据
     */
    private final List<Double> a;

    public double execute() {
        final int n = this.a.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum += this.a.get(i);
        }
        return sum / n;
    }
}
