package com.tp.math.matlab.kernel.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by tangpeng on 2021-05-04
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class DoubleMax {
    private final double val;
    private final int index;
}
