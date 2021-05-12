package com.tp.matlab.web.envolope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.envolope.core.TimeDomainOfEnvolope;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-11
 */
@Service
public class EnvolopeService {

    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        return new TimeDomainOfEnvolope().execute(array);
    }

}