package com.tp.matlab.extension.time.envolope;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.time.envolope.Filt.FiltResult;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfEnvolope {

    /**
     * @param a 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<Double> a) throws JsonProcessingException {
        final double g = 9.80;
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = fs / N;
        final int fmin = 2;          //%fmin：起始频率
        final int fmax = 1000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.56);
        final FiltResult filtResult = new Filt(a, fs, 500d, 10000d).execute();   //500~10k的过滤器范围
        //[p,m]=findpeaks(a_fir);
        final List<DoubleMax> afir_max = MatlabUtils.findPeaks(filtResult.getAfir());
        //p=p/g;
        final List<Double> p = afir_max.stream().map(DoubleMax::getVal).map(i -> i / g).collect(toList());
        //m=m/fs;
        final List<Double> m = afir_max.stream().map(DoubleMax::getIndex).mapToDouble(i -> i).map(i -> i / fs).boxed().collect(toList());
        //[pv,tm]=max(p);
        final DoubleMax pm_max = MatlabUtils.getMax(p);
        //tm=m(tm)
        final double tm = m.get(pm_max.getIndex() - 1); //对应matlab从1开始,此处数组索引要减1
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(p);
        final ValueOfPeak.ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(p).execute();
        //rpp=abs(Pp)+abs(Np);
        final double rpp = Math.abs(valueOfPeakResult.getNp()) + Math.abs(valueOfPeakResult.getPp());
        //[vmean]=Mean_Value(p);
        final double vmean = new MeanValue(p).execute();
        //[sigma]=Value_of_Sigma(p,vmean);
        final double sigma = new ValueOfSigma(p, vmean).execute();
        //[vrms]=Value_of_RMS(p);
        final double vrms = new ValueOfRMS(p).execute();
        //pf=pv/vrms;
        final double pf = pm_max.getVal() / vrms;
        //[ske]=Value_of_Skewness(p,vmean);
        final double ske = new ValueOfSkeness(p, vmean).execute();
        //[ske]=Value_of_Kurtosis(p,vmean,sigma);
        final double kur = new ValueOfKurtosis(p, vmean, sigma).execute();
        //[TV]=total_value(a,fs,500,10000,16);
        double TV = new TotalValue(a, fs, 500, 10000, 16).execute();
        TV = TV / g;//单位转换

        final List<BigDecimal> x = m.stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());
        final List<BigDecimal> y = p.stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());

        final TimeDomainOfEnvolopeResult result = new TimeDomainOfEnvolopeResult.TimeDomainOfEnvolopeResultBuilder()
                .rpp(roundToDecimal(rpp))
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
                .x(x)
                .y(y)
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class TimeDomainOfEnvolopeResult {
        private BigDecimal rpp;
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
