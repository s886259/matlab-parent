package com.tp.math.matlab.ifft.transform;

import com.tp.math.matlab.core.ResultComplex;
import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.tp.math.matlab.ifft.transform.ComplexConvertUtils.convertToResultComplex;

/**
 * Created by tangpeng on 2021-04-26
 */
public class IFFTTransformer extends FastFourierTransformer {

    /**
     * Creates a new instance of this class, with various normalization
     * conventions.
     *
     * @param normalization the type of normalization to be applied to the
     *                      transformed data
     */
    public IFFTTransformer(DftNormalization normalization) {
        super(normalization);
    }

    public synchronized List<ResultComplex> transform(@NonNull final Complex[] x) {
        return Arrays.stream(super.transform(x, TransformType.INVERSE))
                .map(i -> convertToResultComplex(i.getReal(), i.getImaginary()))
                .collect(Collectors.toList());
//        List<ResultComplex> collect = Arrays.stream(super.transform(x, TransformType.INVERSE))
//                .map(i -> convertToResultComplex(i.getReal(), i.getImaginary()))
//                .collect(Collectors.toList());
//
//        fftManager.Complex[] complexes = collect.stream()
//                .map(i -> new fftManager.Complex(i.getReal(), i.getImag()))
//                .toArray((fftManager.Complex[]::new));
//        return new FFTTransformer().transform(complexes);
    }
}
