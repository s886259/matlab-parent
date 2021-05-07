package com.tp.matlab.web.acceleration.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.acceleration.core.TimeDomainOfA;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

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
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer c
    ) throws JsonProcessingException {
        return new TimeDomainOfA().execute(a, c);
    }

}