package com.tp.matlab.extension.velocity.gear.core;

import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-10
 */
@RequiredArgsConstructor
class ValueOfRMS {

    /**
     * 源数据
     */
    private final List<Double> a;

    public double execute() {
        final int n = this.a.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow(this.a.get(i), 2);
        }
        return Math.sqrt(sum / n);
    }
}
