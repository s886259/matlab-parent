package com.tp.matlab.kernel.core;

import cn.hutool.core.util.NumberUtil;
import com.tp.matlab.kernel.transform.FFTTransformer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-17
 */
@RequiredArgsConstructor
public class Spectrum {

    private final Integer fs;
    /**
     * 源数据
     */
    private final List<Double> a;

    public SpectrumResult execute() {
        final int n = this.a.size();
        //A=abs(fft(a,n)*2/n);
        final List<Double> A = new FFTTransformer().transform(a).stream()
                .map(i -> i.getAbs() * 2 / n)
                .collect(toList());
        //f=fs/2*linspace(0,1,n/2);
        final List<Double> f = DoubleStream.iterate(0, i -> NumberUtil.add(i, (double) 2 / n)).limit(n / 2).boxed()
                .map(i -> i * fs / 2)
                .collect(toList());
        final List<Double> y = A.subList(0, n / 2);
        //y(1)=0; %去除直流分量
        y.set(0, 0d);
        return SpectrumResult.of(f, y);
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class SpectrumResult {
        private final List<Double> f;
        private final List<Double> ai;
    }
}
