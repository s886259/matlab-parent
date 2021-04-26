package com.tp.math.matlab.fft.transform;

import fftManager.FastFFT;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-26
 */
public class MyFastFourierTransformer extends FastFFT {


    /**
     * In place fft of complex data.
     *
     * @param x complex array
     */
    public synchronized List<ResultComplex> fft(@NonNull final OriginComplex[] x) {
        super.fft(x);
        return Arrays.stream(x)
                .map(ComplexConvertUtils::convertToResultComplex)
                .collect(Collectors.toList());
    }

    public synchronized List<ResultComplex> ifft(@NonNull final OriginComplex[] x) {
        super.ifft(x, 3);
        return Arrays.stream(x)
                .map(ComplexConvertUtils::convertToResultComplex)
                .collect(Collectors.toList());
    }
}
