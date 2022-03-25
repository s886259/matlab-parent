package com.tp.matlab.extension.time.acceleration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.time.acceleration.ValueOfPeak.ValueOfPeakResult;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfA {
    /**
     * @param a 需要分析的列值
     * @param fs 采样频率
     * @return 分析后的结果
     */
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs
    ) throws JsonProcessingException {
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double)fs / N;
        final double fcut = 5;          //%低频截止
        final int fmin = 5;          //%fmin：起始频率
        final int fmax = 10000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.56);
        final Filt.FiltResult filtResult = new Filt(a, fs, fcut, fs / 2.56).execute();
        final double RPM = filtResult.getMf() * 60;
        //[p,m]=max(a_fir);
        final ValueWithIndex pm_max = MatlabUtils.getMax(filtResult.getAfir());
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
        final double TV = new TotalValue(a, fs, fmin, fmax, 16).execute();

        //t=(0:N-1)/fs;
        final List<BigDecimal> t = DoubleStream.iterate(0, i -> i + 1)
                .limit(N)
                .map(i -> i / fs)
                .boxed()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());
        final List<BigDecimal> y = filtResult.getAfir()
                .stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());

        final TimeDomainOfAResult result = new TimeDomainOfAResult.TimeDomainOfAResultBuilder()
                .rpm(roundToDecimal(RPM))
                .time(roundToDecimal(time))
                .a(roundToDecimal(A))
                .m(pm_max.getIndex())
                .p(roundToDecimal(pm_max.getVal()))
                .tm(roundToDecimal(tm))
                .pp(roundToDecimal(valueOfPeakResult.getPp()))
                .np(roundToDecimal(valueOfPeakResult.getNp()))
                .vmean(roundToDecimal(vmean))
                .vrms(roundToDecimal(vrms))
                .sigma(roundToDecimal(sigma))
                .pf(roundToDecimal(pf))
                .ske(roundToDecimal(ske))
                .kur(roundToDecimal(kur))
                .tv(roundToDecimal(TV))
                .x(t)
                .y(y)
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
        private List<BigDecimal> x;
        private List<BigDecimal> y;
    }
}
