package com.tp.matlab.kernel.core;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2022-03-25
 */
@Getter
@Builder
public class Biandai {
    /**
     * 转频
     */
    @NonNull
    private Integer n;
    /**
     * 默认position为-5、-4、-3、-2 -1 0 1 2 3 4 5，但也可以作为输入变量，作为入参
     */
    @NonNull
    @Builder.Default
    private List<Integer> position = Arrays.asList(-5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5);
}
