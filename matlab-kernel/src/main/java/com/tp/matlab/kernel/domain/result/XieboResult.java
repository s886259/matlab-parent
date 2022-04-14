package com.tp.matlab.kernel.domain.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(staticName = "of")
public class XieboResult {
    /**
     * 频率
     */
    @NonNull
    private final Double pl;
    /**
     * 幅值
     */
    @NonNull
    private final Double fz;
    /**
     * 相对百分比
     */
    @NonNull
    private final Double percent;
}