package com.tp.math.matlab.fft.transform;

import com.tp.math.matlab.core.ResultComplex;
import fftManager.Complex;
import fftManager.FastFFT;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

import static com.tp.math.matlab.core.ComplexConvertUtils.convertToResultComplex;
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
        final List<ResultComplex> result = Arrays.stream(x)
                .map(i -> convertToResultComplex(i.real, i.imag))
                .collect(toList());
        return result;
    }
}
