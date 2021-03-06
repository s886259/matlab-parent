package com.tp.matlab.extension.displacement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.kernel.core.A2v2x;
import com.tp.matlab.kernel.core.A2v2x.A2v2xResult;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import static com.tp.matlab.kernel.util.NumberFormatUtils.round;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * 位移（单轴加速度传感器使用）单值
 * Created by tangpeng on 2021-07-31
 */
@Slf4j
public class Displacement {
    /**
     * @param a  需要分析的列值
     * @param fs 采样频率
     * @param n  转频为入参 (20220323)
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs,
            @NonNull final Integer n
    ) throws JsonProcessingException {
        //N=length(a); %采样点数
        int N = a.size();
        /**
         *  %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     单位：gE
         *     flcut：低频截止；fhcut：高频截止
         *     检测值：峰峰值PPV（就是位移单值） //该值为输出值，需要存库
         *     p：峰值；m：峰值对应的频率
         *  %%%%%%%%%%%%%%%%%%%%%%%%计算%%%%%%%%%%%%%%%%%%%%%%%%
         *  1、转频为入参；
         */
        final double flcut;
        final double fhcut;
        if (n == 0) {
            //flcut=0;fhcut=1;    %低频截止与高频截止
            flcut = 0d;
            fhcut = 1d;
        } else {
            //flcut=n-0.25*n;        %低频截止
            flcut = n - 0.25 * n;
            //fhcut=n+0.25*n;        %高频截止
            fhcut = n + 0.25 * n;
        }
        //[v,x]=a2v2x(a,fs,flcut,fhcut);
        final A2v2xResult a2v2xResult = new A2v2x(a, fs, flcut, fhcut).execute();
        //x=x*1000;              %单位换算
        final List<Double> x = a2v2xResult.getX().stream().map(i -> i * 1000).collect(toList());
        //[p1,m1]=max(x);%正半轴
        final ValueWithIndex pm1 = MatlabUtils.getMax(x);
        //[p2,m2]=max(-x);%负半轴
        final ValueWithIndex pm2 = MatlabUtils.getMax(x.stream().map(i -> i * -1).collect(toList()));
        //m1=m1/fs;
        final double m1 = (double) pm1.getIndex() / fs;
        //m2=m2/fs;
        final double m2 = (double) pm2.getIndex() / fs;
        //PPV=p1+p2;
        final double ppv = pm1.getVal() + pm2.getVal();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%用于输出图像的数据%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //t=(1:N)/fs; %横轴：时间
        final List<Double> t = DoubleStream.iterate(1, i -> i + 1)
                .limit(N)
                .map(i -> i / fs)
                .boxed()
                .map(NumberFormatUtils::round)
                .collect(toList());
        //x_plot=x;   %纵轴：位移
        final List<Double> x_plot = a2v2xResult.getX().stream().map(NumberFormatUtils::round).collect(toList());

        final DisplacementResult result = DisplacementResult.builder()
                .ppv(round(ppv))
                .p1(round(pm1.getVal()))
                .m1(round(m1))
                .p2(round(pm1.getVal()))
                .m2(round(m2))
                .x(t)
                .y(x_plot)
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class DisplacementResult {
        /**
         * 检测值：峰峰值PPV（就是位移单值）
         */
        private Double ppv;
        /**
         * p1：正半轴峰值
         */
        private Double p1;
        /**
         * p2：负半轴峰值
         */
        private Double p2;
        /**
         * m1：正半轴峰值对应的频率
         */
        private Double m1;
        /**
         * m2：负半轴峰值对应的频率
         */
        private Double m2;
        private List<Double> x;
        private List<Double> y;
    }
}
