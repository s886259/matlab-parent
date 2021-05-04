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
public class ValueOfRMS {

    /**
     * 源数据
     */
    private List<Double> inputArray;
    /**
     * result
     */
    @Getter
    private double result;

    public ValueOfRMS(@NonNull final List<Double> inputArray) {
        this.inputArray = inputArray;
        this.result = transform();
    }

    private double transform() {
        final int n = this.inputArray.size();
        double sum = 0;
        for (int i = 0; i < n; i++) {
            sum = sum + Math.pow(this.inputArray.get(i), 2);
        }
        return Math.sqrt(sum / n);
    }
}