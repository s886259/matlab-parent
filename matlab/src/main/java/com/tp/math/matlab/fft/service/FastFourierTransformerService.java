package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.math3.transform.DftNormalization.STANDARD;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class FastFourierTransformerService {

    public List<String> transform(final MyComplex[] f, final TransformType type) {
        return new MyFastFourierTransformer(STANDARD).transform(f, type)
                .stream()
                .map(MyComplex::toString)
                .collect(Collectors.toList());
    }
}
