package com.tp.math.matlab.kernel.transform;

import com.tp.math.matlab.kernel.core.ResultComplex;
import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.util.Arrays;
import java.util.List;

import static com.tp.math.matlab.kernel.core.ComplexConvertUtils.convertToResultComplex;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.math3.transform.TransformType.INVERSE;

/**
 * Created by tangpeng on 2021-04-26
 */
public class IFFTTransformer extends FastFourierTransformer {

    public IFFTTransformer() {
        super(DftNormalization.STANDARD);
    }

    /**
     * @see org.apache.commons.math3.transform.FastFourierTransformer
     */
    public synchronized List<ResultComplex> transform(@NonNull final Complex[] x) {
        final List<ResultComplex> result = Arrays.stream(super.transform(x, INVERSE))
                .map(i -> convertToResultComplex(i.getReal(), i.getImaginary()))
                .collect(toList());
        return result;
    }
}
