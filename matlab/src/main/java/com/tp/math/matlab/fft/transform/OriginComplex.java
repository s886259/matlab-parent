package com.tp.math.matlab.fft.transform;

import fftManager.Complex;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * Created by tangpeng on 2021-04-25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class OriginComplex extends Complex {
    /**
     * Create a complex number given only the real part.
     *
     * @param real Real part.
     */
    public OriginComplex(double real) {
        this(real, 0.0);
    }

    /**
     * Create a complex number given the real and imag parts.
     *
     * @param real Real part.
     * @param imag Imaginary part.
     */
    public OriginComplex(double real, double imag) {
        super(real, imag);
    }

}
