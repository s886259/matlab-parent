package com.tp.matlab.web.time.velocity.gear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.time.velocity.gear.TimeDomainOfV;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-10
 */
@Service
public class TimeGearService {

    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        return new TimeDomainOfV().execute(array, 25600);
    }

}