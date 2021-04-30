package com.tp.math.matlab.core;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;

import static com.tp.math.matlab.util.NumberFormatUtils.roundToString;


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
        final String real = roundToString(this.real);
        if (this.getImag() >= 0) {
            return real + " + " + roundToString(this.imag) + "i";
        } else {
            return real + " - " + roundToString(this.imag).replaceFirst("-", "") + "i";
        }
    }

}
