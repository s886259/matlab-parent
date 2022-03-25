package com.tp.matlab.web.frequency.envolope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.frequency.envolope.FrequencyDomainOfEnvolope;
import com.tp.matlab.kernel.domain.request.FamRequest;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-11
 */
@Service
public class FreqEnvolopeService {
    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        final double bpfi = 9.429032;
        final double bpfo = 6.570968;
        final double bsf = 2.645376;
        final double ftf = 0.410686;
        final FamRequest fam = FamRequest.builder().bpfi(bpfi).bpfo(bpfo).bsf(bsf).ftf(ftf).build();
        final Integer f0 = 12;
        return new FrequencyDomainOfEnvolope().execute(array, 25600, fam, f0);
    }

}