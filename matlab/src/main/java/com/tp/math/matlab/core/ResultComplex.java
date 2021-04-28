package com.tp.math.matlab.core;

import lombok.Data;
import lombok.NonNull;
import lombok.experimental.Accessors;


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
    /**
     * 实部有效数字
     */
    private NumericDigits realNumericDigits;
    /**
     * 虚部有效数字
     */
    private NumericDigits imagNumericDigits;

    @Override
    public String toString() {
        final String real = realNumericDigits.toString();
        if (this.getImag() >= 0) {
            return real + " + " + imagNumericDigits.toString() + "i";
        } else {
            return real + " - " + imagNumericDigits.toString().replaceFirst("-", "") + "i";
        }
    }

}
