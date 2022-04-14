package com.tp.matlab.extension.frequency.gear;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.extension.frequency.common.AbstractFrequencyDomainOfV;
import com.tp.matlab.kernel.domain.request.BiandaiRequest;
import com.tp.matlab.kernel.domain.request.FamRequest;
import com.tp.matlab.kernel.domain.request.XieboRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * 速度频谱(齿轮)
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class FrequencyDomainOfV extends AbstractFrequencyDomainOfV {
    /**
     * @param a       需要分析的列值
     * @param fs      采样频率
     * @param n       转频      (20220414新增)
     * @param fam     * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算 (20220323)%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     *                1、BPFI、BPFO、BSF、FTF、n为输入变量，入参；
     *                2、默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参；
     * @param xiebo   %%%%%%%%%%%%%%%%%%%%%%%谐波光标计算 (20220323)%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     *                1、n为输入变量，入参；
     *                2、默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
     * @param biandai %%%%%%%%%%%%%%%%%%%%%%%边带计算 (20220323)%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
     *                1、n为输入变量，入参；
     *                2、默认position为-5、-4、-3、-2 -1 0 1 2 3 4 5，但也可以作为输入变量，作为入参；
     * @param fmin    起始频率,也可以作为入参输入实现图表重新计算 (20220323)
     * @param fmax    终止频率,也可以作为入参输入实现图表重新计算 (20220323)
     * @param flcut   低频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @param fhcut   高频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    @Override
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs,
            @NonNull final Integer n,
            @NonNull final FamRequest fam,
            @NonNull final XieboRequest xiebo,
            @NonNull final BiandaiRequest biandai,
            @Nullable final Double fmin,
            @Nullable final Double fmax,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
    ) throws JsonProcessingException {
        final double fmin_1 = Optional.ofNullable(fmin).orElse(0d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(5000d);          //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(4d);           //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(fs / 2.56);      //fhcut：高频截止
        return super.execute(a, fs, n, fam, xiebo, biandai, fmin_1, fmax_1, flcut_1, fhcut_1);
    }
}
