package com.tp.matlab.web.frequency.envolope.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.frequency.vibrator.FrequencyDomainOfV;
import com.tp.matlab.kernel.domain.request.BiandaiRequest;
import com.tp.matlab.kernel.domain.request.FamRequest;
import com.tp.matlab.kernel.domain.request.XieboRequest;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * 速度频谱(激振器)
 * Created by tangpeng on 2021-05-11
 */
@Service
public class FreqVibratorService {
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
        final XieboRequest xiebo = XieboRequest.builder().build();
        final BiandaiRequest biandai = BiandaiRequest.builder().build();
        return new FrequencyDomainOfV().execute(array, 25600, 0, fam, xiebo, biandai, null, null, null, null);
    }

}