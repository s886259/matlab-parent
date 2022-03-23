package com.tp.matlab.extension.frequency.vibrator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.frequency.vibrator.Spectrum.SpectrumResult;
import com.tp.matlab.kernel.core.Biandai;
import com.tp.matlab.kernel.core.Fam;
import com.tp.matlab.kernel.core.ValueWithIndex;
import com.tp.matlab.kernel.core.Xiebo;
import com.tp.matlab.kernel.util.MatlabUtils;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * 速度频谱(激振器)
 * Created by tangpeng on 2021-05-05
 */
@Slf4j
public class FrequencyDomainOfV {
    /**
     * @param a       需要分析的列值
     * @param fs      采样频率
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
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs,
            @NonNull final Fam fam,
            @NonNull final Xiebo xiebo,
            @NonNull final Biandai biandai,
            @Nullable final Double fmin,
            @Nullable final Double fmax,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     %   单位：mm/s
         *     %   fcut：低频截止
         *     %   fmin：开始频率；fmax：结束频率；fmin~fmax为频率范围
         *     %   ymax：满刻度
         *     %   检测值：均方根值
         *     %   rms：均方根值
         *     %   p：峰值；mf：峰值对应的频率
         *     %   TV：振动总值=整体频谱=整体趋势，m/s^2    //该值为输出值，需要存库
         *     %   光标信息：见下面光标信息后的注释
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * % //1、采样频率和数据长度为入参；
         * % //2、滤波器：
         * % //1）每张图表有固定的滤波器设置；
         * % //2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
         */
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fcut = 5;          //%低频截止
        final double fmin_1 = Optional.ofNullable(fmin).orElse(0d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(500d);           //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(5d);           //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(fs / 2.56);    //fhcut：高频截止
        final int ymax = 300;
        //[a_fir]=hann_filt(a,fs,flcut,fhcut);
        final List<Double> a_fir = new HannFilt(fs, a, flcut_1, fhcut_1).execute();
        //[v]=a2v(a_fir,fs);
        final List<Double> v = new A2v(a_fir, fs).execute();
        //[f,vi]=spectrum(fs,v);    %ai用于存储频谱幅值数据
        final SpectrumResult spectrumResult = new Spectrum(fs, v).execute();    //%ai用于存储频谱幅值数据
        final List<Double> vi = spectrumResult.getVi();
        final List<Double> f = spectrumResult.getF();
        //[p,m]=max(vi);  %寻峰
        final ValueWithIndex pm_max = MatlabUtils.getMax(vi);
        //mf=f(m);    %峰值对应频率值
        final double mf = f.get(pm_max.getIndex() - 1);
        //[vrms]=Value_of_RMS(v);
        final double vrms = new ValueOfRMS(v).execute();
        //[TV]=total_value(v,fs,fmin,fmax,16);  %整体频谱 (也是整体趋势）    //该值为输出值，需要存库
        final double TV = new TotalValue(v, fs, fmin_1, fmax_1).execute();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * % 1、BPFI、BPFO、BSF、FTF、n为输入变量，入参；
         * % 2、默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参；
         */
        //BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
        final List<Integer> f_k = fam.getK1().stream().map(k -> k * fam.getN()).collect(toList());
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
        //r_BPFI=f(num_BPFI);      %BPFI实际频率
        List<Double> r_BPFI = num_BPFI.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BPFO=f(num_BPFO);      %PBF0实际频率
        List<Double> r_BPFO = num_BPFO.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BSF=f(num_BSF);       %BSF实际频率
        List<Double> r_BSF = num_BSF.stream().map(i -> f.get(i.intValue()) - 1).collect(toList());
        //r_FTF=f(num_FTF);       %FTF实际频率
        List<Double> r_FTF = num_FTF.stream().map(i -> f.get(i.intValue()) - 1).collect(toList());
        //valu_BPFI=vi(num_BPFI);  %BPFI幅值
        final List<Double> valu_BPFI = num_BPFI.stream().map(i -> vi.get(i.intValue() - 1)).collect(toList());
        //valu_BPFO=vi(num_BPFO);  %BPF0幅值
        final List<Double> valu_BPFO = num_BPFO.stream().map(i -> vi.get(i.intValue() - 1)).collect(toList());
        //valu_BSF=vi(num_BSF);    %BSF幅值
        final List<Double> valu_BSF = num_BSF.stream().map(i -> vi.get(i.intValue() - 1)).collect(toList());
        //valu_FTF=vi(num_FTF);    %FTF幅值
        final List<Double> valu_FTF = num_FTF.stream().map(i -> vi.get(i.intValue() - 1)).collect(toList());
        //output_BPFI=[r_BPFI',valu_BPFI'];   %输出BPFI的频率和幅值
        final List<FamResult> output_BPFI = new ArrayList<>();
        //output_BPFO=[r_BPFO',valu_BPFO'];   %输出BPFO的频率和幅值
        final List<FamResult> output_BPFO = new ArrayList<>();
        //output_BSF=[r_BSF',valu_BSF'];      %输出BSF的频率和幅值
        final List<FamResult> output_BSF = new ArrayList<>();
        //output_FTF=[r_FTF',valu_FTF'];      %输出FTF的频率和幅值
        final List<FamResult> output_FTF = new ArrayList<>();
        for (int i = 0; i < r_BPFI.size(); i++) {
            output_BPFI.add(FamResult.of(roundToDecimal(r_BPFI.get(i)), roundToDecimal(valu_BPFI.get(i))));
            output_BPFO.add(FamResult.of(roundToDecimal(r_BPFO.get(i)), roundToDecimal(valu_BPFO.get(i))));
            output_BSF.add(FamResult.of(roundToDecimal(r_BSF.get(i)), roundToDecimal(valu_BSF.get(i))));
            output_FTF.add(FamResult.of(roundToDecimal(r_FTF.get(i)), roundToDecimal(valu_FTF.get(i))));
        }

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%谐波光标计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、n为输入变量，入参；
         * 2、默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
         */
        //num_n=floor(n/df)+1;
        final int num_n = (int) (Math.floor(xiebo.getN() / df) + 1);
        //f_xiebo=k2*n;   %谐波
        final List<Integer> f_xiebo = xiebo.getK2().stream()
                .map(i -> i * 12)
                .collect(toList());
        //num_f=floor(f_xiebo/df)+1;
        final List<Integer> num_f = f_xiebo.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .map(Double::intValue)
                .collect(toList());
        //[xiebo_pinlv]=f(num_f);        %谐波频率
        final List<Double> valu_xiebo = num_f.stream().map(i -> f.get(i - 1)).collect(toList());
        //[fuzhi_xiebo]=vi(num_f);      %谐波幅值
        final List<Double> fuzhi_xiebo = num_f.stream().map(i -> vi.get(i - 1)).collect(toList());
        //percent=100*fuzhi_xiebo/fuzhi_xiebo(1); %相对于基频的百分比
        final List<Double> percent = fuzhi_xiebo.stream()
                .map(i -> i / fuzhi_xiebo.get(0))
                .map(i -> i * 100)
                .collect(toList());
        //xiebo=[xiebo_pinlv',fuzhi_xiebo',percent'];  %输出【频率 幅值 相对百分比】
        final List<XieboResult> xieboResults = new ArrayList<>();
        for (int i = 0; i < valu_xiebo.size(); i++) {
            xieboResults.add(XieboResult.of(roundToDecimal(valu_xiebo.get(i)), roundToDecimal(fuzhi_xiebo.get(i)), roundToDecimal(percent.get(i))));
        }

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%边带计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、n为输入变量，入参；
         * 2、默认position为-5、-4、-3、-2 -1 0 1 2 3 4 5，但也可以作为输入变量，作为入参；
         */
        //f1=fmax/2+n*position;
        final List<Double> f1 = biandai.getPosition().stream().map(i -> fmax_1 / 2 + biandai.getN() * i).collect(toList());
        //num_f1=floor(f1/df)+1;
        final List<Integer> num_f1 = f1.stream()
                .map(i -> i / df)
                .map(Math::floor)
                .map(i -> i + 1)
                .map(Double::intValue)
                .collect(toList());
        //num_zx=floor((fmax/2)/df)+1;
        final Double num_zx = Math.floor((fmax_1 / 2) / df + 1);
        //[fuzhi_biandai]=vi(num_f1);               %幅值
        final List<Double> fuzhi_biandai = num_f1.stream().map(i -> vi.get(i - 1)).collect(toList());
        //[valu_biandai]=f(num_f1);                 %频率
        final List<Double> valu_biandai = num_f1.stream().map(i -> f.get(i - 1)).collect(toList());
        //k=[valu_biandai]./n;                      %阶次
        final List<Double> k = valu_biandai.stream().map(i -> i / biandai.getN()).collect(toList());
        //dB=20*log10([fuzhi_biandai]./vi(num_zx));  %dB
        final List<Double> dB = fuzhi_biandai.stream()
                .map(i -> 20 * Math.log10(i / vi.get(num_zx.intValue() - 1)))
                .collect(toList());
        //biandai=[position',valu_biandai',fuzhi_biandai',k',dB'] ;%输出【位置 频率 幅值 阶次 dB】
        final List<BiandaiResult> biandaiResults = new ArrayList<>();
        for (int i = 0; i < fuzhi_biandai.size(); i++) {
            biandaiResults.add(
                    BiandaiResult.of(
                            roundToDecimal(biandai.getPosition().get(i)),
                            roundToDecimal(valu_biandai.get(i)),
                            roundToDecimal(fuzhi_biandai.get(i)),
                            roundToDecimal(k.get(i)),
                            roundToDecimal(dB.get(i))
                    ));
        }

        //to result
        final FrequencyDomainOfVResult result = FrequencyDomainOfVResult.builder()
                .tv(roundToDecimal(TV))
                /**
                 * bpfi
                 */
                .bpfi1(output_BPFI.get(0))
                .bpfi2(output_BPFI.get(1))
                .bpfi3(output_BPFI.get(2))
                .bpfi4(output_BPFI.get(3))
                /**
                 * bpfo
                 */
                .bpfo1(output_BPFO.get(0))
                .bpfo2(output_BPFO.get(1))
                .bpfo3(output_BPFO.get(2))
                .bpfo4(output_BPFO.get(3))
                /**
                 * bsf
                 */
                .bsf1(output_BSF.get(0))
                .bsf2(output_BSF.get(1))
                .bsf3(output_BSF.get(2))
                .bsf4(output_BSF.get(3))
                /**
                 * ftf
                 */
                .ftf1(output_FTF.get(0))
                .ftf2(output_FTF.get(1))
                .ftf3(output_FTF.get(2))
                .ftf4(output_FTF.get(3))
                /**
                 * xiebo
                 */
                .harmonic1(xieboResults.get(0))
                .harmonic2(xieboResults.get(1))
                .harmonic3(xieboResults.get(2))
                .harmonic4(xieboResults.get(3))
                .harmonic5(xieboResults.get(4))
                .harmonic6(xieboResults.get(5))
                .harmonic7(xieboResults.get(6))
                .harmonic8(xieboResults.get(7))
                .harmonic9(xieboResults.get(8))
                .harmonic10(xieboResults.get(9))
                /**
                 * biandai
                 */
                .sidcband1(biandaiResults.get(0))
                .sidcband2(biandaiResults.get(1))
                .sidcband3(biandaiResults.get(2))
                .sidcband4(biandaiResults.get(3))
                .sidcband5(biandaiResults.get(4))
                .sidcband6(biandaiResults.get(5))
                .sidcband7(biandaiResults.get(6))
                .sidcband8(biandaiResults.get(7))
                .sidcband9(biandaiResults.get(8))
                .sidcband10(biandaiResults.get(9))
                .sidcband11(biandaiResults.get(10))
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    private static class FamResult {
        /**
         * 频率
         */
        @NonNull
        private final BigDecimal pl;
        /**
         * 幅值
         */
        @NonNull
        private final BigDecimal fz;
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    private static class XieboResult {
        /**
         * 频率
         */
        @NonNull
        private final BigDecimal pl;
        /**
         * 幅值
         */
        @NonNull
        private final BigDecimal fz;
        /**
         * 相对百分比
         */
        @NonNull
        private final BigDecimal percent;
    }

    @Getter
    @AllArgsConstructor(staticName = "of")
    private static class BiandaiResult {
        /**
         * 位置
         */
        @NonNull
        private final BigDecimal position;
        /**
         * 频率
         */
        @NonNull
        private final BigDecimal pl;
        /**
         * 幅值
         */
        @NonNull
        private final BigDecimal fz;
        /**
         * 阶次
         */
        @NonNull
        private final BigDecimal k;
        /**
         * db
         */
        @NonNull
        private final BigDecimal db;
    }

    @Getter
    @Builder
    private static class FrequencyDomainOfVResult {
        /**
         * 振动总值=整体频谱=整体趋势，m/s^2
         */
        @NonNull
        private BigDecimal tv;
        /**
         * 输出BPFI*1的频率和幅值 //该值为输出值，需要存库，bpfi1~4
         */
        @NonNull
        private FamResult bpfi1;
        @NonNull
        private FamResult bpfi2;
        @NonNull
        private FamResult bpfi3;
        @NonNull
        private FamResult bpfi4;
        /**
         * 输出BPFO*1的频率和幅值 //该值为输出值，需要存库，bpfo1~4
         */
        @NonNull
        private FamResult bpfo1;
        @NonNull
        private FamResult bpfo2;
        @NonNull
        private FamResult bpfo3;
        @NonNull
        private FamResult bpfo4;
        /**
         * 输出BSF*1的频率和幅值 //该值为输出值，需要存库，bsf1~4
         */
        @NonNull
        private FamResult bsf1;
        @NonNull
        private FamResult bsf2;
        @NonNull
        private FamResult bsf3;
        @NonNull
        private FamResult bsf4;
        /**
         * 输出FTF*1的频率和幅值 //该值为输出值，需要存库，ftf1~4
         */
        @NonNull
        private FamResult ftf1;
        @NonNull
        private FamResult ftf2;
        @NonNull
        private FamResult ftf3;
        @NonNull
        private FamResult ftf4;
        /**
         * 输出谐波为1时【频率 幅值 相对百分比】//对应K2的第1个值，该值为输出值，需要存库 harmonic1~10
         */
        @NonNull
        private XieboResult harmonic1;
        @NonNull
        private XieboResult harmonic2;
        @NonNull
        private XieboResult harmonic3;
        @NonNull
        private XieboResult harmonic4;
        @NonNull
        private XieboResult harmonic5;
        @NonNull
        private XieboResult harmonic6;
        @NonNull
        private XieboResult harmonic7;
        @NonNull
        private XieboResult harmonic8;
        @NonNull
        private XieboResult harmonic9;
        @NonNull
        private XieboResult harmonic10;
        /**
         * 输出不同位置的【位置 频率 幅值 阶次 dB】//，对应position的第1个值，该值为输出值，需要存库 sidcband1~11
         */
        @NonNull
        private BiandaiResult sidcband1;
        @NonNull
        private BiandaiResult sidcband2;
        @NonNull
        private BiandaiResult sidcband3;
        @NonNull
        private BiandaiResult sidcband4;
        @NonNull
        private BiandaiResult sidcband5;
        @NonNull
        private BiandaiResult sidcband6;
        @NonNull
        private BiandaiResult sidcband7;
        @NonNull
        private BiandaiResult sidcband8;
        @NonNull
        private BiandaiResult sidcband9;
        @NonNull
        private BiandaiResult sidcband10;
        @NonNull
        private BiandaiResult sidcband11;
    }
}
