package com.tp.matlab.extension.frequency.acceleration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.frequency.acceleration.Spectrum.SpectrumResult;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.domain.request.FamRequest;
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

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class FrequencyDomainOfA {
    /**
     * @param a   需要分析的列值
     * @param fs  采样频率
     * @param fam FAM [bpfi,bpfo,bsf,ftf]
     * @param f0  基频
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs,
            @NonNull final FamRequest fam,
            @NonNull final Integer f0
    ) throws JsonProcessingException {
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fcut = 5;          //%低频截止
        final int fmin = 5;          //%fmin：起始频率
        final int fmax = 10000;      //famx：终止频率
        final int ymax = 50;
        //[a_fir]=hann_filt(fs,a,fcut,fs/2.56);
        final List<Double> a_fir = new HannFilt(fs, a, (double) fcut, (double) fs / 2.56).execute();
        //[f,ai]=spectrum(fs,a_fir);    %ai用于存储频谱幅值数据
        final SpectrumResult spectrumResult = new Spectrum(fs, a_fir).execute();    //%ai用于存储频谱幅值数据
        //[p,m]=max(ai);  %寻峰
        final List<Double> ai = spectrumResult.getAi();
        final ValueWithIndex pm_max = MatlabUtils.getMax(ai);
        //mf=f(m);    %峰值对应频率值
        final double mf = spectrumResult.getF().get(pm_max.getIndex() - 1);
        //[TV]=total_value(a_fir,fs,fmin,fmax,32);  %整体频谱 (也是 整体趋势）
        final double TV = new TotalValue(a, fs, fmin, fmax, 32).execute();
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
        final List<Integer> f_k = Arrays.asList(12, 24, 36, 48);
        //f_BPFI=k*BPFI;f_BPFO=k*BPFO;f_BSF=k*BSF;f_FTF=k*FTF;
        final List<Double> f_BPFI = f_k.stream().map(i -> i * fam.getBpfi()).collect(toList());
        final List<Double> f_BPFO = f_k.stream().map(i -> i * fam.getBpfo()).collect(toList());
        final List<Double> f_BSF = f_k.stream().map(i -> i * fam.getBsf()).collect(toList());
        final List<Double> f_FTF = f_k.stream().map(i -> i * fam.getFtf()).collect(toList());
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
        //num_f0=floor(f0/df)+1;
        final int num_f0 = (int) (Math.floor(f0 / df) + 1);
        //k=[1 2 3 4 5 6 7 8 9 10];   %阶次
        final List<Integer> k = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
        //f_xiebo=k*12;   %谐波
        final List<Integer> f_xiebo = k.stream()
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
        //TODO:边带计算
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
        final FrequencyDomainOfAResult result = FrequencyDomainOfAResult.builder()
                .ymax(ymax)
                .p(roundToDecimal(pm_max.getVal()))
                .mf(roundToDecimal(mf))
                .tv(roundToDecimal(TV))
                /**
                 * FAM
                 */
                .num_BPFI(num_BPFI.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .valu_BPFI(valu_BPFI.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .num_BPFO(num_BPFO.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .valu_BPFO(valu_BPFO.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .num_BSF(num_BSF.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .valu_BSF(valu_BSF.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .num_FTF(num_FTF.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .valu_FTF(valu_FTF.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                /**
                 * 谐波光标
                 */
                .f0(f0)
                .f_xiebo(f_xiebo)
                .k(k)
                .ai(spectrumResult.getAi().stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .percent(percent.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .x(x)
                .y(y)
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class FrequencyDomainOfAResult {
        /**
         * 满刻度
         */
        private int ymax;
        /**
         * 峰值；mf：峰值对应的频率
         */
        private BigDecimal p;
        /**
         * 峰值对应频率值
         */
        private BigDecimal mf;
        /**
         * 振动总值=整体频谱=整体趋势，m/s^2
         */
        private BigDecimal tv;

        /**
         * FAM栏-BPFI
         */
        private List<BigDecimal> num_BPFI;
        private List<BigDecimal> valu_BPFI;
        /**
         * FAM栏-BPFO
         */
        private List<BigDecimal> num_BPFO;
        private List<BigDecimal> valu_BPFO;
        /**
         * FAM栏-BSF
         */
        private List<BigDecimal> num_BSF;
        private List<BigDecimal> valu_BSF;
        /**
         * FAM栏-FTF
         */
        private List<BigDecimal> num_FTF;
        private List<BigDecimal> valu_FTF;

        /**
         * 谐波光标-基频
         */
        private Integer f0;
        /**
         * 谐波光标-谐波频率
         */
        private List<Integer> f_xiebo;
        /**
         * 谐波光标-阶次
         */
        private List<Integer> k;
        /**
         * 谐波光标-ai用于存储频谱幅值数据
         */
        private List<BigDecimal> ai;
        /**
         * 谐波光标-%相对于基频的百分比
         */
        private List<BigDecimal> percent;
        private List<BigDecimal> x;
        private List<BigDecimal> y;
    }
}
