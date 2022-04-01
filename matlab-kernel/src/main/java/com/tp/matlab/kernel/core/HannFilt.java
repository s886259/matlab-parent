package com.tp.matlab.kernel.core;

import com.tp.matlab.kernel.windows.FirWindow;
import com.tp.matlab.kernel.windows.HannWindow;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
public class HannFilt {
    /**
     * 采样频率
     */
    private final Integer fs;
    /**
     * 源数据
     */
    private final List<Double> a;
    /**
     * 低频截止
     */
    private final Double flcut;
    /**
     * 高频截止
     */
    private final Double fhcut;

    public List<Double> execute() {
        final Integer N = 100;  //%滤波器阶次
        //window=hann(N+1);
        final List<Double> window = new HannWindow(N + 1).getResult();
        //wp1=[flcut/fs*2 fhcut/fs*2];
        final List<Double> wp1 = Arrays.asList(flcut / fs * 2, fhcut / fs * 2);
        //b=fir1(N,wp1,window);
        final List<Double> b = new FirWindow(N + 1, wp1, window).getResult();
        final ArrayList<Double> tmp = new ArrayList<>();
        tmp.add(1d);
        //x=filtfilt(b,1,a);   %滤波后的时域
        final List<Double> x = Filtfilt.doFiltfilt((ArrayList<Double>) b, tmp, (ArrayList<Double>) a);
        return x;
    }

}
