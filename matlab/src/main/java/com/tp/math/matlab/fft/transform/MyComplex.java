package com.tp.math.matlab.fft.transform;

import com.tp.math.matlab.util.MathUtils;
import org.apache.commons.math3.complex.Complex;

/**
 * Created by tangpeng on 2021-04-25
 */
public class MyComplex extends Complex {

    /**
     * Create a complex number given only the real part.
     *
     * @param real Real part.
     */
    public MyComplex(double real) {
        this(real, 0.0);
    }

    /**
     * Create a complex number given the real and imaginary parts.
     *
     * @param real      Real part.
     * @param imaginary Imaginary part.
     */
    public MyComplex(double real, double imaginary) {
        super(real, imaginary);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        String real = MathUtils.roundToString(this.getReal(), false);
        if (this.getImaginary() >= 0) {
            return real + " + " + MathUtils.roundToString(this.getImaginary(), false) + "i";
        } else {
            return real + " - " + MathUtils.roundToString(-this.getImaginary(), false) + "i";
        }

    }

    public String toString(boolean realImaginaryForamt) {
        if (realImaginaryForamt) {
            return toString();
        }
        return "(" + MathUtils.roundToString(this.getReal(), false)
                + ", " + MathUtils.roundToString(this.getImaginary(), false) + ")";
    }
}
