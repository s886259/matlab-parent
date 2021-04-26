package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class FastFourierTransformerService {

    public List<String> transform(final MyComplex[] f, final TransformType type) {
        MyFastFourierTransformer myFastFourierTransformer = new MyFastFourierTransformer();
        if(type == TransformType.FORWARD){
            return myFastFourierTransformer.fft(f).stream().map(Object::toString).collect(Collectors.toList());
        }
        else {
            return myFastFourierTransformer.ifft(f).stream().map(Object::toString).collect(Collectors.toList());
        }
    }
}
