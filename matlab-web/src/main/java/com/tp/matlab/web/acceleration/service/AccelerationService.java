package com.tp.matlab.web.acceleration.service;

import com.tp.matlab.extension.acceleration.core.TimeDomainOfA;
import com.tp.matlab.extension.acceleration.core.TimeDomainOfA.TimeDomainOfAResult;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-06
 */
@Service
public class AccelerationService {

    /**
     * @param a 需要分析的列值
     * @param c 需要分析的通道序号
     * @return 分析后的结果
     */
    public TimeDomainOfAResult execute(
            @NonNull final List<Double> a,
            @NonNull final Integer c
    ) {
        return new TimeDomainOfA().execute(a, c);
    }

}