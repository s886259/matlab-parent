package com.tp.matlab.extension.acceleration.core;

import com.tp.matlab.extension.acceleration.core.ValueOfPeak.ValueOfPeakResult;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.PythonUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfA {

    /**
     * @param a 需要分析的列值
     * @param c 需要分析的通道序号
     * @return 分析后的结果
     */
    public TimeDomainOfAResult execute(
            @NonNull final List<Double> a,
            @NonNull final Integer c
    ) {
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = fs / N;
        final double fcut = 5;          //%低频截止
        final double fmin = 5;          //%fmin：起始频率
        final double fmax = 10000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.25);
        final Filt.FiltResult filtResult = new Filt(a, fs, fcut, fs / 2.25).execute();
        final double RPM = filtResult.get_mf() * 60;
        //[p,m]=max(a_fir);
        final DoubleMax pm_max = PythonUtils.getMax(filtResult.get_Afir());
        //tm=m/fs;
        final double tm = pm_max.getIndex() / fs;
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(a_fir);
        final ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(filtResult.get_Afir()).execute();
        //[vmean]=Mean_Value(a_fir);
        final double vmean = new MeanValue(filtResult.get_Afir()).execute();
        //[sigma]=Value_of_Sigma(a_fir,vmean);
        final double sigma = new ValueOfSigma(filtResult.get_Afir(), vmean).execute();
        //[vrms]=Value_of_RMS(a_fir);
        final double vrms = new ValueOfRMS(filtResult.get_Afir()).execute();
        //pf=p/vrms;
        final double pf = pm_max.getVal() / vrms;
        //[ske]=Value_of_Skewness(a_fir,vmean);
        final double ske = new ValueOfSkeness(filtResult.get_Afir(), vmean).execute();
        //[ske]=Value_of_Kurtosis(a_fir,vmean,sigma);
        final double kur = new ValueOfKurtosis(filtResult.get_Afir(), vmean, sigma).execute();
        //[TV]=total_value(a,fs,5,10000,16);
        final double TV = new TotalValue(a, fs, 5, 10000, 16).execute();
        return TimeDomainOfAResult.of(filtResult.get_Afir(), c, tm, pm_max.getVal());
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class TimeDomainOfAResult {
        private final List<Double> _Afir;
        private final int c;
        private final double tm;
        private final double p;
    }
}
