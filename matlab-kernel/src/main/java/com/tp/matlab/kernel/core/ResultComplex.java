package com.tp.matlab.kernel.core;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToFiveDigits;


/**
 * Created by tangpeng on 2021-04-25
 */
@Data(staticConstructor = "of")
@Accessors(chain = true)
public class ResultComplex {
    /**
     * 实部
     */
    @NonNull
    private Double real;
    /**
     * 虚部
     */
    @NonNull
    private Double imag;

    @Override
    public String toString() {
        final String real = roundToFiveDigits(this.real);
        if (this.getImag() >= 0) {
            return real + " + " + roundToFiveDigits(this.imag) + "i";
        } else {
            return real + " - " + roundToFiveDigits(this.imag).replaceFirst("-", "") + "i";
        }
    }

    /**
     * 复数的模（或模数）是在复平面中绘制的向量（从原点到复数值）的长度。
     */
    public double getAbs() {
        return Math.sqrt(this.real * this.real + this.imag * this.imag);
    }

}
