package com.tp.matlab.kernel.transform;

import com.tp.matlab.kernel.domain.ResultComplex;
import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.Arrays;
import java.util.List;

import static com.tp.matlab.kernel.domain.ComplexConvertUtils.convertToResultComplex;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-26
 */
public class FFTTransformer extends FastFourierTransformer {

    public FFTTransformer() {
        super(DftNormalization.STANDARD);
    }

    /**
     * @see FastFourierTransformer
     */
    public synchronized List<ResultComplex> transform(@NonNull final Complex[] records) {
        return Arrays.stream(super.transform(records, TransformType.FORWARD))
                .map(i -> convertToResultComplex(i.getReal(), i.getImaginary()))
                .collect(toList());
    }

    public synchronized List<ResultComplex> transform(@NonNull final List<Double> records) {
        final Complex[] myComplexes = records.stream()
                .map(i -> new Complex(i, 0))
                .toArray(Complex[]::new);
        return transform(myComplexes);
    }

}
