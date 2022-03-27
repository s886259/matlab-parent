package com.tp.matlab.kernel.domain.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

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