package com.tp.matlab.extension.time.velocity.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
public class ValueOfPeak {

    /**
     * 源数据
     */
    private final List<Double> a;

    public ValueOfPeakResult execute() {
        final int n = this.a.size();
        final double[] x = new double[n];
        final double[] y = new double[n];
        for (int i = 0; i < this.a.size(); i++) {
            final double cur = this.a.get(i);
            if (cur > 0) {
                x[i] = cur;
            } else {
                y[i] = cur;
            }
        }
        final double pp = Arrays.stream(x).max().getAsDouble();
        final double np = Arrays.stream(y).min().getAsDouble();
        return ValueOfPeakResult.of(pp, np);
    }


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class ValueOfPeakResult {
        private final double pp;
        private final double np;
    }
}
