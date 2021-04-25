package com.tp.math.matlab.fft.transform;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2021-04-25
 */
public class MyFastFourierTransformer extends FastFourierTransformer {

    public MyFastFourierTransformer(final DftNormalization normalization) {
        super(normalization);
    }

    public List<MyComplex> transform(final MyComplex[] f, final TransformType type) {
        final List<MyComplex> result = new ArrayList<>();
        Complex[] transform = super.transform(f, type);
        for (int i = 0; i < f.length; i++) {
            result.add(new MyComplex(transform[i].getReal(), transform[i].getImaginary()));
        }
        return result;
    }
}
