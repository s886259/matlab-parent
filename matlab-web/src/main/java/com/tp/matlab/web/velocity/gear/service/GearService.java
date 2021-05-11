package com.tp.matlab.web.velocity.gear.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.velocity.gear.core.TimeDomainOfV;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-10
 */
@Service
public class GearService {

    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        return new TimeDomainOfV().execute(array);
    }

}