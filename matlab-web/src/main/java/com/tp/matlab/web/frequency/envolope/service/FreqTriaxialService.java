package com.tp.matlab.web.frequency.envolope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.frequency.triaxial.Trails;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-07-13
 */
@Service
public class FreqTriaxialService {

    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<List<Double>> array) throws JsonProcessingException {
        return new Trails().execute(array);
    }

}