package com.tp.matlab.kernel.domain.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.util.List;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;

/**
 * 频谱结果
 * 1)速度频谱(激振器)
 * 2)速度频谱(齿轮)
 * 3)加速度频谱(激振器)
 */
@Getter
@Builder
public class FrequencyResult {
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
    private BiandaiResult sidcband1;
    private BiandaiResult sidcband2;
    private BiandaiResult sidcband3;
    private BiandaiResult sidcband4;
    private BiandaiResult sidcband5;
    private BiandaiResult sidcband6;
    private BiandaiResult sidcband7;
    private BiandaiResult sidcband8;
    private BiandaiResult sidcband9;
    private BiandaiResult sidcband10;
    private BiandaiResult sidcband11;
    /**
     * 转频对应的幅值
     */
    @NonNull
    private BigDecimal fuzhi_zhuanpin;

    @NonNull
    private List<BigDecimal> x;
    @NonNull
    private List<BigDecimal> y;

    public static FrequencyResult from(
            @NonNull final Double TV,
            @NonNull final List<FamResult> output_BPFI,
            @NonNull final List<FamResult> output_BPFO,
            @NonNull final List<FamResult> output_BSF,
            @NonNull final List<FamResult> output_FTF,
            @NonNull final List<XieboResult> xieboResults,
            @NonNull final List<BiandaiResult> biandaiResults,
            @NonNull final List<BigDecimal> x,
            @NonNull final List<BigDecimal> y,
            @NonNull final BigDecimal fuzhi_zhuanpin
    ) {
        FrequencyResultBuilder builder = FrequencyResult.builder()
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
                 * xy
                 */
                .x(x)
                .y(y)
                .fuzhi_zhuanpin(fuzhi_zhuanpin);
        if (CollectionUtils.isNotEmpty(biandaiResults)) {
            /**
             * biandai
             */
            builder.sidcband1(biandaiResults.get(0))
                    .sidcband2(biandaiResults.get(1))
                    .sidcband3(biandaiResults.get(2))
                    .sidcband4(biandaiResults.get(3))
                    .sidcband5(biandaiResults.get(4))
                    .sidcband6(biandaiResults.get(5))
                    .sidcband7(biandaiResults.get(6))
                    .sidcband8(biandaiResults.get(7))
                    .sidcband9(biandaiResults.get(8))
                    .sidcband10(biandaiResults.get(9))
                    .sidcband11(biandaiResults.get(10));
        }
        return builder.build();
    }
}
