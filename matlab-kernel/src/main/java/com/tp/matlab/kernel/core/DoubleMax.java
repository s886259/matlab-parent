package com.tp.matlab.kernel.core;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Created by tangpeng on 2021-05-04
 */
@Getter
@RequiredArgsConstructor(staticName = "of")
public class DoubleMax {
    private final double val;
    /**
     * 注意：
     * matlab index从1开始,
     * 此处从0,返回处理时应加1
     */
    private final int index;
}
