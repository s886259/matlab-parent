package com.tp.matlab.extension.frequency.envolope;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.frequency.envolope.Spectrum.SpectrumResult;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-17
 */
@Slf4j
public class FrequencyDomainOfEnvolope {

    /**
     * @param a 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull List<Double> a) throws JsonProcessingException {
        final double g = 9.8;
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final int fcut = 5; //%低频截止
        final int fmin = 2;          //%fmin：起始频率
        final int fmax = 1000;      //fmax：终止频率
        final int fbegin = 500;
        final int fstop = 10000;
        final double ymax = 0.025;
        //a=a/g;
//        a = a.stream().map(i -> i / g).collect(toList());
        //[a_fir]=hann_filt(fs,a,fbegin,fstop);
        final List<Double> a_fir = new HannFilt(fs, a, (double) fbegin, (double) fstop).execute();   //500~10k的过滤器范围
        //a_fir_2=a_fir.^2;
        final List<Double> a_fir_2 = a_fir.stream().map(i -> Math.pow(i, 2)).collect(toList());
        //[a_fir_3]=hann_filt(fs,a_fir_2,fmin,fmax);
        final List<Double> a_fir_3 = new HannFilt(fs, a_fir_2, (double) fmin, (double) fmax).execute();
        final SpectrumResult spectrumResult = new Spectrum(fs, a_fir_3).execute();    //%ai用于存储频谱幅值数据
        //[p,m]=max(ai);  %寻峰
        //[p,m]=max(ai(2:500));  %寻峰
        final List<Double> ai = spectrumResult.getAi();
        final DoubleMax pm_max = MatlabUtils.getMax(ai.subList(1, 500));
        //mf=f(m);    %峰值对应频率值
        final double mf = spectrumResult.getF().get(pm_max.getIndex() - 1);
        //[TV]=total_value(a_fir,fs,fmin,fmax,16);  %整体频谱 (也是 整体趋势）
        final double TV = new TotalValue(a_fir, fs, fmin, fmax, 16).execute();
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
        final double BPFI = 9.429032;
        final double BPFO = 6.570968;
        final double BSF = 2.645376;
        final double FTF = 0.410686;
        final List<Integer> k = Arrays.asList(12, 24, 36, 48);
        //f_BPFI=k*BPFI;f_BPFO=k*BPFO;f_BSF=k*BSF;f_FTF=k*FTF;
        final List<Double> f_BPFI = k.stream().map(i -> i * BPFI).collect(toList());
        final List<Double> f_BPFO = k.stream().map(i -> i * BPFO).collect(toList());
        final List<Double> f_BSF = k.stream().map(i -> i * BSF).collect(toList());
        final List<Double> f_FTF = k.stream().map(i -> i * FTF).collect(toList());
        //num_BPFI=floor(f_BPFI/df)+1;
        final List<Double> num_BPFI = f_BPFI.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .collect(toList());
        //num_BPFO=floor(f_BPFO/df)+1;
        final List<Double> num_BPFO = f_BPFO.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .collect(toList());
        //num_BSF=floor(f_BSF/df)+1;
        final List<Double> num_BSF = f_BSF.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .collect(toList());
        //num_FTF=floor(f_FTF/df)+1;
        final List<Double> num_FTF = f_FTF.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .collect(toList());
        //valu_BPFI=ai(num_BPFI);
        final List<Double> valu_BPFI = num_BPFI.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BPFO = ai(num_BPFO);
        final List<Double> valu_BPFO = num_BPFO.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BSF = ai(num_BSF);
        final List<Double> valu_BSF = num_BSF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_FTF = ai(num_FTF);
        final List<Double> valu_FTF = num_FTF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%谐波光标计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         */
        final int f0 = 12;  //%基频
        //num_f0=floor(f0/df)+1;
        final int num_f0 = (int) (Math.floor(f0 / df) + 1);
        //f_xiebo=k*12;   %谐波
        final List<Integer> f_xiebo = Stream.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .map(i -> i * 12)
                .collect(toList());
        //num_f=floor(f_xiebo/df)+1;
        final List<Integer> num_f = f_xiebo.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .map(Double::intValue)
                .collect(toList());
        //[valu_xiebo]=screen(num_f,ai);
        final List<Double> valu_xiebo = new Screen(num_f, ai).execute();
        //percent=100*valu_xiebo/valu_xiebo(1); %相对于基频的百分比
        final List<Double> percent = valu_xiebo.stream()
                .map(i -> i / valu_xiebo.get(0))
                .map(i -> i * 100)
                .collect(toList());
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%边带计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //position=[-5 -4 -3 -2 -1 0 1 2 3 4 5];  %位置
        final List<Integer> position = Arrays.asList(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5);
        //f1=500+12*position; %频率
        final List<Integer> f1 = position.stream().map(i -> i * 12 + 500).collect(toList());
        //num_f1=floor(f1/df)+1;
        final List<Integer> num_f1 = f1.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .map(Double::intValue)
                .collect(toList());
        //[valu_biandai]=screen(num_f1,ai);
        final List<Double> valu_biandai = new Screen(num_f1, ai).execute();
        //k1=41.67+position;  %阶次
        final List<Double> k1 = position.stream()
                .mapToDouble(i -> i + 41.67)
                .boxed()
                .collect(toList());
        final List<Double> dB = f1.stream()
                .map(i -> 20 * Math.log(ai.get(i - 1) / ai.get(500 - 1)))
                .collect(toList());
        //to result
        final List<BigDecimal> x = spectrumResult.getF().stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());
        final List<BigDecimal> y = spectrumResult.getAi().stream()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());
        final FrequencyDomainOfEnvolopeResult result = FrequencyDomainOfEnvolopeResult.builder()
                .x(x)
                .y(y)
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class FrequencyDomainOfEnvolopeResult {
        private List<BigDecimal> x;
        private List<BigDecimal> y;
    }
}
