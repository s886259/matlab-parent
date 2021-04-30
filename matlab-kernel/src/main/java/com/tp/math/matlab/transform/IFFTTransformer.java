package com.tp.math.matlab.transform;

import com.tp.math.matlab.core.ResultComplex;
import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.util.Arrays;
import java.util.List;

import static com.tp.math.matlab.core.ComplexConvertUtils.convertToResultComplex;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.math3.transform.TransformType.INVERSE;

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
        final List<ResultComplex> result = Arrays.stream(super.transform(x, INVERSE))
                .map(i -> convertToResultComplex(i.getReal(), i.getImaginary()))
                .collect(toList());
        return result;
    }
}
