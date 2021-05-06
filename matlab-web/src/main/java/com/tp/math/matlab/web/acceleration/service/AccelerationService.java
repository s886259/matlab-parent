package com.tp.math.matlab.web.acceleration.service;

import com.tp.math.matlab.extension.acceleration.core.TimeDomainOfA;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-06
 */
@Service
public class AccelerationService {

    public void run(@NonNull final List<Double> a) {
        new TimeDomainOfA().run(a);
    }

}