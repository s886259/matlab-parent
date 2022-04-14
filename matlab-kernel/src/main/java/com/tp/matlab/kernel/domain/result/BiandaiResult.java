package com.tp.matlab.kernel.domain.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(staticName = "of")
public class BiandaiResult {
    /**
     * 位置
     */
    @NonNull
    private final Double position;
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
     * 阶次
     */
    @NonNull
    private final Double k;
    /**
     * db
     */
    @NonNull
    private final Double db;
}