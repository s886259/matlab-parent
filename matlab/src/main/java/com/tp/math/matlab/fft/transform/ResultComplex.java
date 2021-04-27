package com.tp.math.matlab.fft.transform;

import fftManager.Complex;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;


/**
 * Created by tangpeng on 2021-04-25
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
public class ResultComplex extends Complex {
    /**
     * 实部有效数字
     */
    private NumericDigits realNumericDigits;
    /**
     * 虚部有效数字
     */
    private NumericDigits imagNumericDigits;

    /**
     * Create a complex originNumber given only the real part.
     *
     * @param real Real part.
     */
    public ResultComplex(double real) {
        this(real, 0.0);
    }

    /**
     * Create a complex originNumber given the real and imag parts.
     *
     * @param real Real part.
     * @param imag Imaginary part.
     */
    public ResultComplex(double real, double imag) {
        super(real, imag);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        final String real = realNumericDigits.formatShortG();
        if (this.imag >= 0) {
            return real + " + " + imagNumericDigits.formatShortG() + "i";
        } else {
            return real + " - " + imagNumericDigits.formatShortG().replace("-", "") + "i";
        }
    }

}
