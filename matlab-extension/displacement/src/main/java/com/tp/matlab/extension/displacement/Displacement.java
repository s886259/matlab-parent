package com.tp.matlab.extension.displacement;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.displacement.A2v2x.A2v2xResult;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-07-31
 */
@Slf4j
public class Displacement {
    /**
     * @param a  需要分析的列值
     * @param fs 采样频率
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    public Map<String, Object> execute(
            @NonNull final List<Double> a,
            @NonNull final Integer fs
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     %   单位：mm
         *     %   fmin：低频截止；fmax：高频截止
         *     %   检测值：峰峰值PPV（就是位移单值）
         *     %   p：峰值；m：峰值对应的频率
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         */
        final int n = a.size();   //%数据长度
        final int fmin = 9;          //%fmin：起始频率
        final int fmax = 30;      //fmax：终止频率
        //[v,x]=a2v2x(a,fs,fmin,fmax);
        final A2v2xResult a2v2xResult = new A2v2x(a, fs, fmin, fmax).execute();
        //x=x*1000;
        final List<Double> x = a2v2xResult.getX().stream().map(i -> i * 1000).collect(toList());
        //[p1,m1]=max(x);%正半轴
        final DoubleMax pm1 = MatlabUtils.getMax(x);
        //[p2,m2]=max(-x);%负半轴
        final DoubleMax pm2 = MatlabUtils.getMax(x.stream().map(i -> i * -1).collect(toList()));
        //m1=m1/fs;
        final double m1 = (double) pm1.getIndex() / fs;
        //m2=m2/fs;
        final double m2 = (double) pm2.getIndex() / fs;
        //PPV=p1+p2;
        final double ppv = pm1.getVal() + pm2.getVal();

        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%作图%%%%%%%%%%%%%%%%%%%%%%%%
         */
        //t=(0:n-1)/fs; %时间
        final List<BigDecimal> t = DoubleStream.iterate(0, i -> i + 1)
                .limit(n)
                .map(i -> i / fs)
                .boxed()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());

        final DisplacementResult result = DisplacementResult.builder()
                .ppv(roundToDecimal(ppv))
                .p1(roundToDecimal(pm1.getVal()))
                .m1(roundToDecimal(m1))
                .p2(roundToDecimal(pm1.getVal()))
                .m2(roundToDecimal(m2))
                .x(t)
                .y(a2v2xResult.getX().stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
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
        private BigDecimal ppv;
        /**
         * p1：正半轴峰值
         */
        private BigDecimal p1;
        /**
         * p2：负半轴峰值
         */
        private BigDecimal p2;
        /**
         * m1：正半轴峰值对应的频率
         */
        private BigDecimal m1;
        /**
         * m2：负半轴峰值对应的频率
         */
        private BigDecimal m2;
        private List<BigDecimal> x;
        private List<BigDecimal> y;
    }
}
