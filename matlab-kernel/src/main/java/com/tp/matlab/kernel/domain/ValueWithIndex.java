package com.tp.matlab.kernel.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by tangpeng on 2021-05-04
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class ValueWithIndex {
    private final double val;
    /**
     * matlab index从1开始, 这里也是从1开始
     */
    private final int index;
}
