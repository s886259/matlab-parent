package com.tp.matlab.extension.frequency.gear;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.frequency.gear.Spectrum.SpectrumResult;
import com.tp.matlab.kernel.domain.TotalValue;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.domain.request.BiandaiRequest;
import com.tp.matlab.kernel.domain.request.FamRequest;
import com.tp.matlab.kernel.domain.request.XieboRequest;
import com.tp.matlab.kernel.domain.result.BiandaiResult;
import com.tp.matlab.kernel.domain.result.FamResult;
import com.tp.matlab.kernel.domain.result.FrequencyResult;
import com.tp.matlab.kernel.domain.result.XieboResult;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
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
 * 速度频谱(齿轮)
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
            @NonNull final FamRequest fam,
            @NonNull final XieboRequest xiebo,
            @NonNull final BiandaiRequest biandai,
            @Nullable final Double fmin,
            @Nullable final Double fmax,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     单位：mm/s
         *     fcut：低频截止
         *     fmin：开始频率；fmax：结束频率；fmin~fmax为频率范围
         *     ymax：满刻度
         *     检测值：峰值
         *     p：峰值；mf：峰值对应的频率
         *     TV：振动总值=整体频谱=整体趋势，m/s^2  //该值为输出值，需要存库
         *     光标信息：见下面光标信息后的注释
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、采样频率和数据长度为入参；
         * 2、滤波器：
         * 1）每张图表有固定的滤波器设置；
         * 2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
         */
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fmin_1 = Optional.ofNullable(fmin).orElse(0d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(1000d);          //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(500d);         //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(10000d);       //fhcut：高频截止
        final int ymax = 300;
        //[a_fir]=hann_filt(a,fs,flcut,fhcut);
        final List<Double> a_fir = new HannFilt(fs, a, flcut_1, fhcut_1).execute();
        //[f,ai]=spectrum(fs,a_fir);    %ai用于存储频谱幅值数据
        final SpectrumResult spectrumResult = new Spectrum(fs, a_fir).execute();    //%ai用于存储频谱幅值数据
        final List<Double> ai = spectrumResult.getAi();
        final List<Double> f = spectrumResult.getF();
        //[p,m]=max(ai);  %寻峰
        final ValueWithIndex pm_max = MatlabUtils.getMax(ai);
        //mf=f(m);    %峰值对应频率值
        final double mf = spectrumResult.getF().get(pm_max.getIndex() - 1);
        //[TV]=total_value(a_fir,fs,fmin,fmax);  %整体频谱 (也是 整体趋势）  //该值为输出值，需要存库
        final double TV = new TotalValue(a_fir, fs, fmin_1, fmax_1).execute();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、BPFI、BPFO、BSF、FTF、n为输入变量，入参；
         * 2、默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参；
         */
        //BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
        final List<Integer> f_k = fam.getK1().stream().map(k -> k * fam.getN()).collect(toList());
        //f_BPFI=k*BPFI;f_BPFO=k*BPFO;f_BSF=k*BSF;f_FTF=k*FTF;
        final List<Double> f_BPFI = f_k.stream().map(i -> i * fam.getBpfi()).collect(toList());
        final List<Double> f_BPFO = f_k.stream().map(i -> i * fam.getBpfo()).collect(toList());
        final List<Double> f_BSF = f_k.stream().map(i -> i * fam.getBsf()).collect(toList());
        final List<Double> f_FTF = f_k.stream().map(i -> i * fam.getFtf()).collect(toList());
        //num_BPFI=floor(f_BPFI/df)+1;
        final List<Double> num_BPFI = f_BPFI.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_BPFO=floor(f_BPFO/df)+1;
        final List<Double> num_BPFO = f_BPFO.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_BSF=floor(f_BSF/df)+1;
        final List<Double> num_BSF = f_BSF.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_FTF=floor(f_FTF/df)+1;
        final List<Double> num_FTF = f_FTF.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //r_BPFI=f(num_BPFI);      %BPFI实际频率
        List<Double> r_BPFI = num_BPFI.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BPFO=f(num_BPFO);      %PBF0实际频率
        List<Double> r_BPFO = num_BPFO.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BSF=f(num_BSF);       %BSF实际频率
        List<Double> r_BSF = num_BSF.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_FTF=f(num_FTF);       %FTF实际频率
        List<Double> r_FTF = num_FTF.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //valu_BPFI=vi(num_BPFI);  %BPFI幅值
        final List<Double> valu_BPFI = num_BPFI.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BPFO=vi(num_BPFO);  %BPF0幅值
        final List<Double> valu_BPFO = num_BPFO.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BSF=vi(num_BSF);    %BSF幅值
        final List<Double> valu_BSF = num_BSF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_FTF=vi(num_FTF);    %FTF幅值
        final List<Double> valu_FTF = num_FTF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
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
        //f_xiebo=k*12;   %谐波
        final List<Integer> f_xiebo = xiebo.getK2().stream().map(i -> i * 12).collect(toList());
        //num_f=floor(f_xiebo/df)+1;
        final List<Integer> num_f = f_xiebo.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).map(Double::intValue).collect(toList());
        //[xiebo_pinlv]=f(num_f);        %谐波频率
        final List<Double> valu_xiebo = num_f.stream().map(i -> f.get(i - 1)).collect(toList());
        //[fuzhi_xiebo]=ai(num_f);      %谐波幅值
        final List<Double> fuzhi_xiebo = num_f.stream().map(i -> ai.get(i - 1)).collect(toList());
        //percent=100*fuzhi_xiebo/fuzhi_xiebo(1); %相对于基频的百分比
        final List<Double> percent = fuzhi_xiebo.stream().map(i -> i / fuzhi_xiebo.get(0)).map(i -> i * 100).collect(toList());
        //xiebo=[xiebo_pinlv',fuzhi_xiebo',percent'];  %输出【频率 幅值 相对百分比】
        final List<XieboResult> xieboResults = new ArrayList<>();
        for (int i = 0; i < valu_xiebo.size(); i++) {
            xieboResults.add(XieboResult.of(roundToDecimal(valu_xiebo.get(i)), roundToDecimal(fuzhi_xiebo.get(i)), roundToDecimal(percent.get(i))));
        }

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%边带计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、n为输入变量，入参；
         * 2、默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
         */
        //f1=fmax/2+n*position;
        final List<Double> f1 = biandai.getPosition().stream().map(i -> fmax_1 / 2 + biandai.getN() * i).collect(toList());
        //num_f1=floor(f1/df)+1;
        final List<Integer> num_f1 = f1.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).map(Double::intValue).collect(toList());
        //num_zx=floor((fmax/2)/df)+1;
        final Double num_zx = Math.floor((fmax_1 / 2) / df + 1);
        //[fuzhi_biandai]=vi(num_f1);               %幅值
        final List<Double> fuzhi_biandai = num_f1.stream().map(i -> ai.get(i - 1)).collect(toList());
        //[valu_biandai]=f(num_f1);                 %频率
        final List<Double> valu_biandai = num_f1.stream().map(i -> f.get(i - 1)).collect(toList());
        //k=[valu_biandai]./n;                      %阶次
        final List<Double> k = valu_biandai.stream().map(i -> i / biandai.getN()).collect(toList());
        //dB=20*log10([fuzhi_biandai]./vi(num_zx));  %dB
        final List<Double> dB = fuzhi_biandai.stream().map(i -> 20 * Math.log10(i / ai.get(num_zx.intValue() - 1))).collect(toList());
        //biandai=[position',valu_biandai',fuzhi_biandai',k',dB'] ;%输出【位置 频率 幅值 阶次 dB】
        final List<BiandaiResult> biandaiResults = new ArrayList<>();
        for (int i = 0; i < fuzhi_biandai.size(); i++) {
            biandaiResults.add(BiandaiResult.of(roundToDecimal(biandai.getPosition().get(i)), roundToDecimal(valu_biandai.get(i)),
                    roundToDecimal(fuzhi_biandai.get(i)), roundToDecimal(k.get(i)), roundToDecimal(dB.get(i))));
        }

        //to result
        final List<BigDecimal> x = spectrumResult.getF().stream().map(NumberFormatUtils::roundToDecimal).collect(toList());
        final List<BigDecimal> y = spectrumResult.getAi().stream().map(NumberFormatUtils::roundToDecimal).collect(toList());
        final FrequencyResult result = FrequencyResult.from(TV, output_BPFI, output_BPFO, output_BSF, output_FTF, xieboResults, biandaiResults, x, y);
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }
}
package com.tp.matlab.extension.frequency.gear;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.frequency.gear.Spectrum.SpectrumResult;
import com.tp.matlab.kernel.domain.TotalValue;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.domain.request.BiandaiRequest;
import com.tp.matlab.kernel.domain.request.FamRequest;
import com.tp.matlab.kernel.domain.request.XieboRequest;
import com.tp.matlab.kernel.domain.result.BiandaiResult;
import com.tp.matlab.kernel.domain.result.FamResult;
import com.tp.matlab.kernel.domain.result.FrequencyResult;
import com.tp.matlab.kernel.domain.result.XieboResult;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
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
 * 速度频谱(齿轮)
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
            @NonNull final FamRequest fam,
            @NonNull final XieboRequest xiebo,
            @NonNull final BiandaiRequest biandai,
            @Nullable final Double fmin,
            @Nullable final Double fmax,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     单位：mm/s
         *     fcut：低频截止
         *     fmin：开始频率；fmax：结束频率；fmin~fmax为频率范围
         *     ymax：满刻度
         *     检测值：峰值
         *     p：峰值；mf：峰值对应的频率
         *     TV：振动总值=整体频谱=整体趋势，m/s^2  //该值为输出值，需要存库
         *     光标信息：见下面光标信息后的注释
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、采样频率和数据长度为入参；
         * 2、滤波器：
         * 1）每张图表有固定的滤波器设置；
         * 2）也可以作为入参输入实现图表重新计算：fmin、fmax、flcut、fhcut等字段；
         */
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = (double) fs / N;
        final double fmin_1 = Optional.ofNullable(fmin).orElse(0d);             //fmin：起始频率
        final double fmax_1 = Optional.ofNullable(fmax).orElse(1000d);          //famx：终止频率
        final double flcut_1 = Optional.ofNullable(flcut).orElse(500d);         //flcut：低频截止
        final double fhcut_1 = Optional.ofNullable(fhcut).orElse(10000d);       //fhcut：高频截止
        final int ymax = 300;
        //[a_fir]=hann_filt(a,fs,flcut,fhcut);
        final List<Double> a_fir = new HannFilt(fs, a, flcut_1, fhcut_1).execute();
        //[f,ai]=spectrum(fs,a_fir);    %ai用于存储频谱幅值数据
        final SpectrumResult spectrumResult = new Spectrum(fs, a_fir).execute();    //%ai用于存储频谱幅值数据
        final List<Double> ai = spectrumResult.getAi();
        final List<Double> f = spectrumResult.getF();
        //[p,m]=max(ai);  %寻峰
        final ValueWithIndex pm_max = MatlabUtils.getMax(ai);
        //mf=f(m);    %峰值对应频率值
        final double mf = spectrumResult.getF().get(pm_max.getIndex() - 1);
        //[TV]=total_value(a_fir,fs,fmin,fmax);  %整体频谱 (也是 整体趋势）  //该值为输出值，需要存库
        final double TV = new TotalValue(a_fir, fs, fmin_1, fmax_1).execute();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%FAM栏计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、BPFI、BPFO、BSF、FTF、n为输入变量，入参；
         * 2、默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参；
         */
        //BPFI=9.429032;BPFO=6.570968;BSF=2.645376;FTF=0.410686;
        final List<Integer> f_k = fam.getK1().stream().map(k -> k * fam.getN()).collect(toList());
        //f_BPFI=k*BPFI;f_BPFO=k*BPFO;f_BSF=k*BSF;f_FTF=k*FTF;
        final List<Double> f_BPFI = f_k.stream().map(i -> i * fam.getBpfi()).collect(toList());
        final List<Double> f_BPFO = f_k.stream().map(i -> i * fam.getBpfo()).collect(toList());
        final List<Double> f_BSF = f_k.stream().map(i -> i * fam.getBsf()).collect(toList());
        final List<Double> f_FTF = f_k.stream().map(i -> i * fam.getFtf()).collect(toList());
        //num_BPFI=floor(f_BPFI/df)+1;
        final List<Double> num_BPFI = f_BPFI.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_BPFO=floor(f_BPFO/df)+1;
        final List<Double> num_BPFO = f_BPFO.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_BSF=floor(f_BSF/df)+1;
        final List<Double> num_BSF = f_BSF.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //num_FTF=floor(f_FTF/df)+1;
        final List<Double> num_FTF = f_FTF.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).collect(toList());
        //r_BPFI=f(num_BPFI);      %BPFI实际频率
        List<Double> r_BPFI = num_BPFI.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BPFO=f(num_BPFO);      %PBF0实际频率
        List<Double> r_BPFO = num_BPFO.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_BSF=f(num_BSF);       %BSF实际频率
        List<Double> r_BSF = num_BSF.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //r_FTF=f(num_FTF);       %FTF实际频率
        List<Double> r_FTF = num_FTF.stream().map(i -> f.get(i.intValue() - 1)).collect(toList());
        //valu_BPFI=vi(num_BPFI);  %BPFI幅值
        final List<Double> valu_BPFI = num_BPFI.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BPFO=vi(num_BPFO);  %BPF0幅值
        final List<Double> valu_BPFO = num_BPFO.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_BSF=vi(num_BSF);    %BSF幅值
        final List<Double> valu_BSF = num_BSF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
        //valu_FTF=vi(num_FTF);    %FTF幅值
        final List<Double> valu_FTF = num_FTF.stream().map(i -> ai.get(i.intValue() - 1)).collect(toList());
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
        //f_xiebo=k*12;   %谐波
        final List<Integer> f_xiebo = xiebo.getK2().stream().map(i -> i * 12).collect(toList());
        //num_f=floor(f_xiebo/df)+1;
        final List<Integer> num_f = f_xiebo.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).map(Double::intValue).collect(toList());
        //[xiebo_pinlv]=f(num_f);        %谐波频率
        final List<Double> valu_xiebo = num_f.stream().map(i -> f.get(i - 1)).collect(toList());
        //[fuzhi_xiebo]=ai(num_f);      %谐波幅值
        final List<Double> fuzhi_xiebo = num_f.stream().map(i -> ai.get(i - 1)).collect(toList());
        //percent=100*fuzhi_xiebo/fuzhi_xiebo(1); %相对于基频的百分比
        final List<Double> percent = fuzhi_xiebo.stream().map(i -> i / fuzhi_xiebo.get(0)).map(i -> i * 100).collect(toList());
        //xiebo=[xiebo_pinlv',fuzhi_xiebo',percent'];  %输出【频率 幅值 相对百分比】
        final List<XieboResult> xieboResults = new ArrayList<>();
        for (int i = 0; i < valu_xiebo.size(); i++) {
            xieboResults.add(XieboResult.of(roundToDecimal(valu_xiebo.get(i)), roundToDecimal(fuzhi_xiebo.get(i)), roundToDecimal(percent.get(i))));
        }

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%边带计算%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         * 1、n为输入变量，入参；
         * 2、默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
         */
        //f1=fmax/2+n*position;
        final List<Double> f1 = biandai.getPosition().stream().map(i -> fmax_1 / 2 + biandai.getN() * i).collect(toList());
        //num_f1=floor(f1/df)+1;
        final List<Integer> num_f1 = f1.stream().map(i -> i / df).map(Math::floor).map(i -> i + 1).map(Double::intValue).collect(toList());
        //num_zx=floor((fmax/2)/df)+1;
        final Double num_zx = Math.floor((fmax_1 / 2) / df + 1);
        //[fuzhi_biandai]=vi(num_f1);               %幅值
        final List<Double> fuzhi_biandai = num_f1.stream().map(i -> ai.get(i - 1)).collect(toList());
        //[valu_biandai]=f(num_f1);                 %频率
        final List<Double> valu_biandai = num_f1.stream().map(i -> f.get(i - 1)).collect(toList());
        //k=[valu_biandai]./n;                      %阶次
        final List<Double> k = valu_biandai.stream().map(i -> i / biandai.getN()).collect(toList());
        //dB=20*log10([fuzhi_biandai]./vi(num_zx));  %dB
        final List<Double> dB = fuzhi_biandai.stream().map(i -> 20 * Math.log10(i / ai.get(num_zx.intValue() - 1))).collect(toList());
        //biandai=[position',valu_biandai',fuzhi_biandai',k',dB'] ;%输出【位置 频率 幅值 阶次 dB】
        final List<BiandaiResult> biandaiResults = new ArrayList<>();
        for (int i = 0; i < fuzhi_biandai.size(); i++) {
            biandaiResults.add(BiandaiResult.of(roundToDecimal(biandai.getPosition().get(i)), roundToDecimal(valu_biandai.get(i)),
                    roundToDecimal(fuzhi_biandai.get(i)), roundToDecimal(k.get(i)), roundToDecimal(dB.get(i))));
        }

        //to result
        final List<BigDecimal> x = spectrumResult.getF().stream().map(NumberFormatUtils::roundToDecimal).collect(toList());
        final List<BigDecimal> y = spectrumResult.getAi().stream().map(NumberFormatUtils::roundToDecimal).collect(toList());
        final FrequencyResult result = FrequencyResult.from(TV, output_BPFI, output_BPFO, output_BSF, output_FTF, xieboResults, biandaiResults, x, y);
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }
}
