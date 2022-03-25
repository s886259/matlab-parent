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