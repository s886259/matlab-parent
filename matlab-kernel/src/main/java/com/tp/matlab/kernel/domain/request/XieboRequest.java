package com.tp.matlab.kernel.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Arrays;
import java.util.List;

/**
 * 谐波光标计算
 * <p>
 * Created by tangpeng on 2022-03-25
 */
@Getter
@Builder
public class XieboRequest {
    /**
     * 转频
     */
    @NonNull
    private Integer n;
    /**
     * 默认K2谐波为1、2、3、4、5、6、7、8、9、10去乘以计算，但也可以作为输入变量，作为入参；
     */
    @NonNull
    @Builder.Default
    private List<Integer> k2 = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
}
