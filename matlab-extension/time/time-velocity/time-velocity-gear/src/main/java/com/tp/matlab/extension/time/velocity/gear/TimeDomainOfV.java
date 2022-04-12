package com.tp.matlab.extension.time.velocity.gear;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.time.velocity.common.*;
import com.tp.matlab.extension.time.velocity.common.ValueOfPeak.ValueOfPeakResult;
import com.tp.matlab.kernel.core.HannFilt;
import com.tp.matlab.kernel.domain.TotalValue;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.domain.result.TimeResult;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.DoubleStream;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * 速度时域【齿轮】(检测:峰值)
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class TimeDomainOfV {
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
         *     单位：mm/s
         *     time：总时间，时间范围：0~time  //该值为输出值，需要存库
         *     RPM：实际转速
         *     A：幅值  //该值为输出值，需要存库
         *     检测值：峰值
         *     m：峰值出现的位置；
         *     p：峰值；tm：时域值（峰值对应的时间点）  //该值为输出值，需要存库
         *     Pp：正峰值；Np：负峰值  //该值为输出值，需要存库
         *     vmean：均值
         *     vrms：均方根值  //该值为输出值，需要存库
         *     sigma：标准偏差  //该值为输出值，需要存库
         *     pf：波峰因素  //该值为输出值，需要存库
         *     ske：偏斜度  //该值为输出值，需要存库
         *     kur：峭度  //该值为输出值，需要存库
         *     TV：振动总值,mm/s,（用于计算整体趋势）  //该值为输出值，需要存库
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、采样频率和数据长度为入参；
         * 2、滤波器：
         * 1）每张图表有固定的滤波器设置；
         * 2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
         * 3、使用的是汉宁窗函数；
         */
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fmin_1 = Optional.ofNullable(fmin).orElse(5d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(5000d);          //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(4d);           //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(fs / 2.56);    //fhcut：高频截止
        final double time = (double) N / fs;                                          //总时间
        //[v]=a2v(a,fs,flcut,fhcut);
        final List<Double> v = new A2v(a, fs, flcut_1, fhcut_1).execute();
        //[a_fir]=hann_filt(a,fs,flcut,fhcut);
        final List<Double> a_fir = new HannFilt(fs, a, flcut_1, fhcut_1).execute();
        //[p,m]=max(v);
        final ValueWithIndex pm_max = MatlabUtils.getMax(v);
        //tm=m/fs;
        final double tm = (double) pm_max.getIndex() / fs;
        //A=p;
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(v);
        final ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(v).execute();
        //rpp=abs(Pp)+abs(Np);
        final double rpp = Math.abs(valueOfPeakResult.getNp()) + Math.abs(valueOfPeakResult.getPp());
        //[vmean]=Mean_Value(v);
        final double vmean = new MeanValue(v).execute();
        //[sigma]=Value_of_Sigma(v,vmean);
        final double sigma = new ValueOfSigma(v, vmean).execute();
        //[vrms]=Value_of_RMS(v);
        final double vrms = new ValueOfRMS(v).execute();
        //pf=p/vrms;
        final double pf = pm_max.getVal() / vrms;
        //[ske]=Value_of_Skewness(v,vmean);
        final double ske = new ValueOfSkeness(v, vmean).execute();
        //[ske]=Value_of_Kurtosis(v,vmean,sigma);
        final double kur = new ValueOfKurtosis(v, vmean, sigma).execute();
        //[TV]=total_value(v,fs,fmin,fmax);
        final double TV = new TotalValue(v, fs, fmin_1, fmax_1).execute();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%用于输出图像的数据%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //t=(1:N)/fs; %图像的横轴
        final List<BigDecimal> t = DoubleStream.iterate(1, i -> i + 1)
                .limit(N)
                .map(i -> i / fs)
                .boxed()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());
        //v_plot=v;   %图像的纵轴
        final List<BigDecimal> v_plot = v.stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());

        final TimeResult result = TimeResult.builder()
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
                .x(t)
                .y(v_plot)
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }
}
