package com.tp.math.matlab.kernel.transform;

import com.tp.math.matlab.kernel.core.ResultComplex;
import fftManager.Complex;
import fftManager.FastFFT;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

import static com.tp.math.matlab.kernel.core.ComplexConvertUtils.convertToResultComplex;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-26
 */
public class FFTTransformer extends FastFFT {

    /**
     * @see fftManager.FastFFT
     */
    public synchronized List<ResultComplex> transform(@NonNull final Complex[] records) {
        super.fft(records);
        return Arrays.stream(records)
                .map(i -> convertToResultComplex(i.real, i.imag))
                .collect(toList());
    }

    public synchronized List<ResultComplex> transform(@NonNull final List<Double> records) {
        final Complex[] myComplexes = records.stream()
                .map(i -> new Complex(i, 0))
                .toArray(Complex[]::new);
        return transform(myComplexes);
    }

}
