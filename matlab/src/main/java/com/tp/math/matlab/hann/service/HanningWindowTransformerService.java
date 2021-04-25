package com.tp.math.matlab.hann.service;

import com.tp.math.matlab.hann.transform.HanningWindowTransform;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class HanningWindowTransformerService {

    public String[] transform(@NonNull final Integer length) {
        return HanningWindowTransform.transform(length);
    }
}
