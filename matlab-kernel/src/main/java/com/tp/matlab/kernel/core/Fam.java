package com.tp.matlab.kernel.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2021-07-30
 */
@Getter
@Builder
public class Fam {
    /**
     * BPFI
     */
    @NonNull
    private Double bpfi;
    /**
     * BPFO
     */
    @NonNull
    private Double bpfo;
    /**
     * BSF
     */
    @NonNull
    private Double bsf;
    /**
     * FTF
     */
    @NonNull
    private Double ftf;
    /**
     * 转频
     */
    @NonNull
    private Integer n;
    /**
     * 默认K1的倍频为1、2、3、4去乘以计算，但也可以作为输入变量，作为入参
     */
    @NonNull
    @Builder.Default
    private List<Integer> k1 = Arrays.asList(1, 2, 3, 4);
}
