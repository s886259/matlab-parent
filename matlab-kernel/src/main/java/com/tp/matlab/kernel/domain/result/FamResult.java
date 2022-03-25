package com.tp.matlab.kernel.domain.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor(staticName = "of")
public class FamResult {
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