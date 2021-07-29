package com.tp.matlab.web.frequency.envolope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.frequency.gear.FrequencyDomainOfV;
import com.tp.matlab.kernel.core.Fam;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-10
 */
@Service
public class FreqGearService {
    /**
     * @param array 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> array) throws JsonProcessingException {
        final double bpfi = 9.429032;
        final double bpfo = 6.570968;
        final double bsf = 2.645376;
        final double ftf = 0.410686;
        final Fam fam = Fam.builder().bpfi(bpfi).bpfo(bpfo).bsf(bsf).ftf(ftf).build();
        final Integer f0 = 12;
        return new FrequencyDomainOfV().execute(array, fam, f0);
    }

}