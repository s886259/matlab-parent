package com.tp.matlab.web.vectoramplitude;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.vectoramplitude.VectorAmplitude;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-08-01
 */
@Service
public class VectorAmplitudeService {
    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<double[]> array) throws JsonProcessingException {
        return new VectorAmplitude().execute(array, 25600, 12, null, null);
    }

}