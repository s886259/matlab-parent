package com.tp.math.matlab.kernel.timedomain.acceleration;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@Accessors(chain = true)
public class ValueOfPeak {

    /**
     * 源数据
     */
    private List<Double> a;
    /**
     * result
     */
    @Getter
    private ValueOfPeakResult result;

    public ValueOfPeak(@NonNull final List<Double> a) {
        this.a = a;
        this.result = transform();
    }

    private ValueOfPeakResult transform() {
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
        final double _Pp = Arrays.stream(x).max().getAsDouble();
        final double _Np = Arrays.stream(y).min().getAsDouble();
        return ValueOfPeakResult.of(_Pp, _Np);
    }


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class ValueOfPeakResult {
        private final double _Pp;
        private final double _Np;
    }
}
