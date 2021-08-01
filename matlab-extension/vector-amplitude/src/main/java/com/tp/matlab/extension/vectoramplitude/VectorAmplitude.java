package com.tp.matlab.extension.vectoramplitude;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.extension.vectoramplitude.A2v2x.A2v2xResult;
import com.tp.matlab.kernel.core.ValueWithIndex;
import com.tp.matlab.kernel.util.MatlabUtils;
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
 * Created by tangpeng on 2021-08-01
 */
@Slf4j
public class VectorAmplitude {
    /**
     * @param a  需要分析的列值
     * @param fs 采样频率
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    public Map<String, Object> execute(
            @NonNull final List<double[]> a,
            @NonNull final Integer fs
    ) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%% xy平面矢量振幅计算 %%%%%%%%%%%%%%%%%%%%%%%%
         */
        //ax1=aa(:,1);
        final List<Double> ax1 = DoubleStream.of(a.get(0)).boxed().collect(toList());
        //ay1=aa(:,2);
        final List<Double> ay1 = DoubleStream.of(a.get(1)).boxed().collect(toList());
        //ay1=aa(:,4);
        final List<Double> ax2 = DoubleStream.of(a.get(3)).boxed().collect(toList());
        //ay2=aa(:,5);
        final List<Double> ay2 = DoubleStream.of(a.get(4)).boxed().collect(toList());
        //n=length(ax1); %采样点数
        final int n = ax1.size();
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     %   单位：mm
         *     %   fmin：低频截止；fmax：高频截止
         *     %   检测值：左侧矢量振幅A1（对应通道1、2），右侧矢量振幅A2（对应通道4、5）
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         */
        final int fmin = 9;       //fmin：起始频率
        final int fmax = 30;      //fmax：终止频率
        //[v1,x1]=a2v2x(ax1,fs,fmin,fmax);
        final A2v2xResult v1x1 = new A2v2x(ax1, fs, fmin, fmax).execute();
        //[v2,y1]=a2v2x(ay1,fs,fmin,fmax);
        final A2v2xResult v2y1 = new A2v2x(ay1, fs, fmin, fmax).execute();
        //[v3,x2]=a2v2x(ax2,fs,fmin,fmax);
        final A2v2xResult v3x2 = new A2v2x(ax2, fs, fmin, fmax).execute();
        //[v4,y2]=a2v2x(ay2,fs,fmin,fmax);
        final A2v2xResult v4y2 = new A2v2x(ay2, fs, fmin, fmax).execute();
        //x1=x1*1000+20;
        final List<Double> x1 = v1x1.getX().stream().map(i -> i * 1000 + 20).collect(toList());
        //y1=y1*1000+20;
        final List<Double> y1 = v2y1.getX().stream().map(i -> i * 1000 + 20).collect(toList());
        //x2=x2*1000+20;
        final List<Double> x2 = v3x2.getX().stream().map(i -> i * 1000 + 20).collect(toList());
        //y2=y2*1000+20;
        final List<Double> y2 = v4y2.getX().stream().map(i -> i * 1000 + 20).collect(toList());
        //dis_xy1=sqrt(x1.^2+y1.^2);
        final List<Double> dis_xy1 = DoubleStream.iterate(0, i -> i + 1).limit(n)
                .map(i -> Math.sqrt(Math.pow(x1.get((int) i), 2) + Math.pow(y1.get((int) i), 2)))
                .boxed()
                .collect(toList());
        //dis_xy2=sqrt(x2.^2+y2.^2);
        final List<Double> dis_xy2 = DoubleStream.iterate(0, i -> i + 1).limit(n)
                .map(i -> Math.sqrt(Math.pow(x2.get((int) i), 2) + Math.pow(y2.get((int) i), 2)))
                .boxed()
                .collect(toList());
        //[p1,m1]=max(dis_xy1);%正半轴
        final ValueWithIndex pm1 = MatlabUtils.getMax(dis_xy1);
        //[p2,m2]=min(dis_xy1);%负半轴
        final ValueWithIndex pm2 = MatlabUtils.getMin(dis_xy1);
        //[p3,m3]=max(dis_xy2);%正半轴
        final ValueWithIndex pm3 = MatlabUtils.getMax(dis_xy2);
        //[p4,m4]=min(dis_xy2);%负半轴
        final ValueWithIndex pm4 = MatlabUtils.getMin(dis_xy2);
        //m1=m1/fs;
        final double m1 = (double) pm1.getIndex() / fs;
        //m2=m2/fs;
        final double m2 = (double) pm2.getIndex() / fs;
        //m3=m3/fs;
        final double m3 = (double) pm3.getIndex() / fs;
        //m4=m4/fs;   %极值出现的位置
        final double m4 = (double) pm4.getIndex() / fs;
        //A1=p1-p2;
        final double a1 = pm1.getVal() - pm2.getVal();
        //A2=p3-p4;   %矢量幅值
        final double a2 = pm3.getVal() - pm4.getVal();

        final DisplacementResult result = DisplacementResult.builder()
                .p1(roundToDecimal(pm1.getVal()))
                .p2(roundToDecimal(pm2.getVal()))
                .p3(roundToDecimal(pm3.getVal()))
                .p4(roundToDecimal(pm4.getVal()))
                .m1(roundToDecimal(m1))
                .m2(roundToDecimal(m2))
                .m3(roundToDecimal(m3))
                .m4(roundToDecimal(m4))
                .a1(roundToDecimal(a1))
                .a2(roundToDecimal(a2))
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class DisplacementResult {
        /**
         * p1：左侧正半轴峰值
         */
        private BigDecimal p1;
        /**
         * p2：左侧负半轴峰值
         */
        private BigDecimal p2;
        /**
         * p3：右侧正半轴峰值
         */
        private BigDecimal p3;
        /**
         * p4：右侧负半轴峰值
         */
        private BigDecimal p4;
        /**
         * m1：左侧正半轴峰值对应的频率
         */
        private BigDecimal m1;
        /**
         * m2：左侧负半轴峰值对应的频率
         */
        private BigDecimal m2;
        /**
         * m3：右侧正半轴峰值对应的频率
         */
        private BigDecimal m3;
        /**
         * m4：右侧负半轴峰值对应的频率
         */
        private BigDecimal m4;
        /**
         * a1：左侧矢量振幅A1（对应通道1、2）
         */
        private BigDecimal a1;
        /**
         * a2：右侧矢量振幅A2（对应通道4、5）
         */
        private BigDecimal a2;
    }
}
