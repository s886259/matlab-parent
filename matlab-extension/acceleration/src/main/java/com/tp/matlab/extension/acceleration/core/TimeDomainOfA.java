package com.tp.matlab.extension.acceleration.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.acceleration.core.ValueOfPeak.ValueOfPeakResult;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.PythonUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToFourDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;

/**
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfA {

    /**
     * @param a 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> a) throws JsonProcessingException {
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = fs / N;
        final double fcut = 5;          //%低频截止
        final double fmin = 5;          //%fmin：起始频率
        final double fmax = 10000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.56);
        final Filt.FiltResult filtResult = new Filt(a, fs, fcut, fs / 2.56).execute();
        final double RPM = filtResult.getMf() * 60;
        //[p,m]=max(a_fir);
        final DoubleMax pm_max = PythonUtils.getMax(filtResult.getAfir());
        //tm=m/fs;
        final double tm = (double) pm_max.getIndex() / fs;
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(a_fir);
        final ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(filtResult.getAfir()).execute();
        //[vmean]=Mean_Value(a_fir);
        final double vmean = new MeanValue(filtResult.getAfir()).execute();
        //[sigma]=Value_of_Sigma(a_fir,vmean);
        final double sigma = new ValueOfSigma(filtResult.getAfir(), vmean).execute();
        //[vrms]=Value_of_RMS(a_fir);
        final double vrms = new ValueOfRMS(filtResult.getAfir()).execute();
        //pf=p/vrms;
        final double pf = pm_max.getVal() / vrms;
        //[ske]=Value_of_Skewness(a_fir,vmean);
        final double ske = new ValueOfSkeness(filtResult.getAfir(), vmean).execute();
        //[ske]=Value_of_Kurtosis(a_fir,vmean,sigma);
        final double kur = new ValueOfKurtosis(filtResult.getAfir(), vmean, sigma).execute();
        //[TV]=total_value(a,fs,5,10000,16);
        final double TV = new TotalValue(a, fs, 5, 10000, 16).execute();
        final TimeDomainOfAResult result = new TimeDomainOfAResult.TimeDomainOfAResultBuilder()
                //保留小数点后4位 e.g 54.0373
                .rpm(roundToFourDecimal(RPM))
                .time(roundToFourDecimal(time))
                .a(roundToFourDecimal(A))
                .m(pm_max.getIndex())
                .p(roundToFourDecimal(pm_max.getVal()))
                .tm(roundToFourDecimal(tm))
                .pp(roundToFourDecimal(valueOfPeakResult.getPp()))
                .np(roundToFourDecimal(valueOfPeakResult.getNp()))
                .vmean(roundToFourDecimal(vmean))
                .vrms(roundToFourDecimal(vrms))
                .sigma(roundToFourDecimal(sigma))
                .pf(roundToFourDecimal(pf))
                .ske(roundToFourDecimal(ske))
                .kur(roundToFourDecimal(kur))
                .tv(roundToFourDecimal(TV))
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class TimeDomainOfAResult {
        private BigDecimal rpm;
        private BigDecimal time;
        private BigDecimal a;
        private int m;
        private BigDecimal p;
        private BigDecimal tm;
        private BigDecimal pp;
        private BigDecimal np;
        private BigDecimal vmean;
        private BigDecimal vrms;
        private BigDecimal sigma;
        private BigDecimal pf;
        private BigDecimal ske;
        private BigDecimal kur;
        private BigDecimal tv;

    }
}
