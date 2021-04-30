package com.tp.math.matlab.kernel.transform;

import com.tp.math.matlab.kernel.core.ComplexConvertUtils;
import com.tp.math.matlab.kernel.core.ResultComplex;
import fftManager.Complex;
import fftManager.FastFFT;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-26
 */
public class FFTTransformer extends FastFFT {
    /**
     * In place fft of complex data.
     *
     * @param x complex array
     */
    public synchronized List<ResultComplex> transform(@NonNull final Complex[] x) {
        super.fft(x);
        return Arrays.stream(x)
                .map(i -> ComplexConvertUtils.convertToResultComplex(i.real, i.imag))
                .collect(toList());
    }
}
