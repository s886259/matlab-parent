package com.tp.matlab.extension.time.envolope;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.kernel.core.HannFilt;
import com.tp.matlab.kernel.domain.TotalValue;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * 包络时域图(检测:真峰峰值)
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfEnvolope {
    /**
     * @param a     需要分析的列值
     * @param fs    采样频率
     * @param fmin  起始频率,也可以作为入参输入实现图表重新计算 (20220323)
     * @param fmax  终止频率,也可以作为入参输入实现图表重新计算 (20220323)
     * @param flcut 低频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @param fhcut 高频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @return 分析后的结果
     */
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs,
            @Nullable final Double fmin,
            @Nullable final Double fmax,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     单位：gE
         *     time：总时间，时间范围：0~time    //该值为输出值，需要存库
         *     A：幅值   //该值为输出值，需要存库
         *     检测值：真峰峰值
         *     m：峰值出现的位置；
         *     p：峰值；tm：时域值（峰值对应的时间点）   //该值为输出值，需要存库
         *     rpp：真峰峰值  //该值为输出值，需要存库
         *     Pp：正峰值；Np：负峰值  //该值为输出值，需要存库
         *     vmean：均值
         *     vrms：均方根值  //该值为输出值，需要存库
         *     sigma：标准偏差  //该值为输出值，需要存库
         *     pf：波峰因素  //该值为输出值，需要存库
         *     ske：偏斜度  //该值为输出值，需要存库
         *     kur：峭度  //该值为输出值，需要存库
         *     TV：振动总值，gE，用于计算整体趋势  //该值为输出值，需要存库
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、采样频率和数据长度为入参；
         * 2、滤波器：
         * 1）每张图表有固定的滤波器设置；
         * 2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
         * 3、使用的是汉宁窗函数；
         */
        final double g = 9.80;
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fmin_1 = Optional.ofNullable(fmin).orElse(2d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(1000d);          //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(500d);         //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(fs / 2.56);    //fhcut：高频截止
        final double time = (double) N / fs;
        //[a_fir]=hann_filt(a,fs,flcut,fhcut);
        final List<Double> a_fir = new HannFilt(fs, a, flcut_1, fhcut_1).execute();
        //[p,m]=findpeaks(a_fir);
        final List<ValueWithIndex> afir_max = MatlabUtils.findPeaks(a_fir);
        //p=p/g;
        final List<Double> p = afir_max.stream().map(ValueWithIndex::getVal).map(i -> i / g).collect(toList());
        //m=m/fs;
        final List<Double> m = afir_max.stream().map(ValueWithIndex::getIndex).mapToDouble(i -> i).map(i -> i / fs).boxed().collect(toList());
        //[pv,tm]=max(p);
        final ValueWithIndex pm_max = MatlabUtils.getMax(p);
        //tm=m(tm)
        final double tm = m.get(pm_max.getIndex() - 1); //对应matlab从1开始,此处数组索引要减1
        //A=pv;
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
        //[TV]=total_value(a,fs,fmin,fmax);
        double TV = new TotalValue(a, fs, fmin_1, fmax_1).execute();
        TV = TV / g;//单位转换

        final TimeDomainOfEnvolopeResult result = new TimeDomainOfEnvolopeResult.TimeDomainOfEnvolopeResultBuilder()
                .rpp(roundToDecimal(rpp))
                .time(roundToDecimal(time))
                .a(roundToDecimal(A))
                .p(roundToDecimal(pm_max.getVal()))
                .tm(roundToDecimal(tm))
                .pp(roundToDecimal(valueOfPeakResult.getPp()))
                .np(roundToDecimal(valueOfPeakResult.getNp()))
                .vrms(roundToDecimal(vrms))
                .sigma(roundToDecimal(sigma))
                .pf(roundToDecimal(pf))
                .ske(roundToDecimal(ske))
                .kur(roundToDecimal(kur))
                .tv(roundToDecimal(TV))
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
        private BigDecimal p;
        private BigDecimal tm;
        private BigDecimal pp;
        private BigDecimal np;
        private BigDecimal vrms;
        private BigDecimal sigma;
        private BigDecimal pf;
        private BigDecimal ske;
        private BigDecimal kur;
        private BigDecimal tv;
    }
}
