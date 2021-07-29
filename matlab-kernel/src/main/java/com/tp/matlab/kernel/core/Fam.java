package com.tp.matlab.kernel.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

/**
 * Created by tangpeng on 2021-07-30
 */
@Getter
@Builder
public class Fam {
    @NonNull
    private Double bpfi;
    @NonNull
    private Double bpfo;
    @NonNull
    private Double bsf;
    @NonNull
    private Double ftf;
}
