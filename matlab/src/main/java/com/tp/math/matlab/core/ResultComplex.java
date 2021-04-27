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
    @NonNull
    private Double real;
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
        final String real = realNumericDigits.formatShortG();
        if (this.getImag() >= 0) {
            return real + " + " + imagNumericDigits.formatShortG() + "i";
        } else {
            return real + " - " + imagNumericDigits.formatShortG().replace("-", "") + "i";
        }
    }

}
