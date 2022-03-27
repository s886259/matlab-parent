package com.tp.matlab.web.displacement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.displacement.Displacement;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-07-31
 */
@Service
public class DisplacementService {
    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        return new Displacement().execute(array, 25600, null);
    }

}