package com.tp.math.matlab.fft.transform;

import fftManager.Complex;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.tp.math.matlab.util.MathUtils.roundToString;
import static com.tp.math.matlab.util.NumericDigitsUtils.getNumberDigits;

/**
 * Created by tangpeng on 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MyComplex extends Complex {
    /**
     * matlab最多保留5个有效数字
     */
    private static final int MATLAB_NUMERIC_FORMAT_SHORT_G = 5;

    private int maxIntegerDigits;

    /**
     * Create a complex number given only the real part.
     *
     * @param real Real part.
     */
    public MyComplex(double real) {
        this(real, 0.0);
    }

    /**
     * Create a complex number given the real and imag parts.
     *
     * @param real Real part.
     * @param imag Imaginary part.
     */
    public MyComplex(double real, double imag) {
        super(real, imag);
        this.maxIntegerDigits = Math.max(
                getNumberDigits(real, MATLAB_NUMERIC_FORMAT_SHORT_G),
                getNumberDigits(imag, MATLAB_NUMERIC_FORMAT_SHORT_G)
        );
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String real = roundToString(this.real, false);
        if (this.imag >= 0) {
            return real + " + " + roundToString(this.imag, false) + "i";
        } else {
            return real + " - " + roundToString(-this.imag, false) + "i";
        }
    }
}
