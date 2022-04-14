package com.tp.matlab.extension.vectoramplitude;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.kernel.core.A2v2x;
import com.tp.matlab.kernel.core.A2v2x.A2v2xResult;
import com.tp.matlab.kernel.domain.ValueWithIndex;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.DoubleStream;

import static com.tp.matlab.kernel.util.MatlabUtils.getMax;
import static com.tp.matlab.kernel.util.MatlabUtils.getMin;
import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDouble;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

/**
 * 矢量振幅
 * Created by tangpeng on 2021-08-01
 */
@Slf4j
public class VectorAmplitude {
    /**
     * @param a     需要分析的列值
     * @param fs    采样频率
     * @param n     转频 (20220323)
     * @param flcut 低频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @param fhcut 高频截止,也可以作为入参输入实现图表重新计算 (20220323)
     * @return 分析后的结果
     * @throws JsonProcessingException
     */
    public Map<String, Object> execute(
            @NonNull final List<double[]> a,
            @NonNull final Integer fs,
            @NonNull final Integer n,
            @Nullable final Double flcut,
            @Nullable final Double fhcut
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
        final int size = ax1.size();
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     %   单位：mm
         *     %   flcut：低频截止；fhcut：高频截止
         *     %   检测值：左侧矢量振幅A1（对应通道1、2），右侧矢量振幅A2（对应通道4、5）   //该值为输出值，需要存库
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         * //1、n为转频，作为入参；
         * //2、滤波器：
         * //1）每张图表有固定的滤波器设置；
         * //2）也可以作为入参输入实现图表重新计算：flcut、fhcut作为入参；
         */
        final double flcut_1;
        final double fhcut_1;
        if (n == 0) {
            //flcut=0;fhcut=1;    %低频截止与高频截止
            flcut_1 = Optional.ofNullable(flcut).orElse(0d);
            fhcut_1 = Optional.ofNullable(fhcut).orElse(1d);
        } else {
            //flcut=n-0.25*n;        %低频截止
            flcut_1 = Optional.ofNullable(flcut).orElse(n - 0.25 * n);
            //fhcut=n+0.25*n;        %高频截止
            fhcut_1 = Optional.ofNullable(fhcut).orElse(n + 0.25 * n);
        }
        //[v1,x1]=a2v2x(ax1,fs,flcut,fhcut);
        final A2v2xResult v1x1 = new A2v2x(ax1, fs, flcut_1, fhcut_1).execute();
        //[v2,y1]=a2v2x(ay1,fs,flcut,fhcut);
        final A2v2xResult v2y1 = new A2v2x(ay1, fs, flcut_1, fhcut_1).execute();
        //[v3,x2]=a2v2x(ax2,fs,flcut,fhcut);
        final A2v2xResult v3x2 = new A2v2x(ax2, fs, flcut_1, fhcut_1).execute();
        //[v4,y2]=a2v2x(ay2,fs,flcut,fhcut);
        final A2v2xResult v4y2 = new A2v2x(ay2, fs, flcut_1, fhcut_1).execute();

        /**
         * %%%%%%%%%%%%%%%%用于绘图的数据%%%%%%%%%%%%%%%%%%
         */
        //x_left=x1*1000;     %左侧水平方向位移（对应通道1）
        final List<Double> leftx = v1x1.getX().stream().map(i -> i * 1000).collect(toList());
        //y_left=y1*1000;     %左侧竖直平方向位移（对应通道2）
        final List<Double> lefty = v2y1.getX().stream().map(i -> i * 1000).collect(toList());
        //x_right=x2*1000;    %右侧水平方向位移（对应通道4）
        final List<Double> rightx = v3x2.getX().stream().map(i -> i * 1000).collect(toList());
        //y_right=y2*1000;    %右侧竖直方向位移（对应通道5）
        final List<Double> righty = v4y2.getX().stream().map(i -> i * 1000).collect(toList());

        //delta=1000*max([max(x1),max(y1),max(x2),max(y2)]);
        final double delta = DoubleStream.of(getMax(v1x1.getX()).getVal(), getMax(v2y1.getX()).getVal(), getMax(v3x2.getX()).getVal(), getMax(v4y2.getX()).getVal()).max().getAsDouble() * 1000;
        //x1=x1*1000+delta;
        final List<Double> x1 = v1x1.getX().stream().map(i -> i * 1000 + delta).collect(toList());
        //y1=y1*1000+delta;
        final List<Double> y1 = v2y1.getX().stream().map(i -> i * 1000 + delta).collect(toList());
        //x2=x2*1000+delta;
        final List<Double> x2 = v3x2.getX().stream().map(i -> i * 1000 + delta).collect(toList());
        //y2=y2*1000+delta;
        final List<Double> y2 = v4y2.getX().stream().map(i -> i * 1000 + delta).collect(toList());

        //xmax_1=max(x1);
        final double xmax_1 = getMax(x1).getVal();
        //ymmax_1=max(y1);
        final double ymmax_1 = getMax(y1).getVal();
        //xmin_1=min(x1);
        final double xmin_1 = getMin(x1).getVal();
        //ymin_1=min(y1);
        final double ymin_1 = getMin(y1).getVal();
        //theta1=180*atan((ymmax_1-ymin_1)/(xmax_1-xmin_1))/pi;  %左侧（对应通道1、2）与水平方向夹角  //该值为输出值，需要存库
        final double theta1 = Math.toDegrees(Math.atan((ymmax_1 - ymin_1) / (xmax_1 - xmin_1)));

        //xmax_2=max(x2);
        final double xmax_2 = getMax(x2).getVal();
        //ymmax_2=max(y2);
        final double ymmax_2 = getMax(y2).getVal();
        //xmin_2=min(x2);
        final double xmin_2 = getMin(x2).getVal();
        //ymin_2=min(y2);
        final double ymin_2 = getMin(y2).getVal();
        //theta2=180*atan((ymax_2-ymin_2)/(xmax_2-xmin_2))/pi;   %右侧（对应通道4、5）与水平方向夹角  //该值为输出值，需要存库
        final double theta2 = Math.toDegrees(Math.atan((ymmax_2 - ymin_2) / (xmax_2 - xmin_2)));

        //dis_xy1=sqrt(x1.^2+y1.^2);
        final List<Double> dis_xy1 = DoubleStream.iterate(0, i -> i + 1).limit(size)
                .map(i -> Math.sqrt(Math.pow(x1.get((int) i), 2) + Math.pow(y1.get((int) i), 2)))
                .boxed()
                .collect(toList());
        //dis_xy2=sqrt(x2.^2+y2.^2);
        final List<Double> dis_xy2 = DoubleStream.iterate(0, i -> i + 1).limit(size)
                .map(i -> Math.sqrt(Math.pow(x2.get((int) i), 2) + Math.pow(y2.get((int) i), 2)))
                .boxed()
                .collect(toList());

        //[p1,m1]=max(dis_xy1);%正半轴
        final ValueWithIndex pm1 = getMax(dis_xy1);
        //[p2,m2]=min(dis_xy1);%负半轴
        final ValueWithIndex pm2 = getMin(dis_xy1);
        //[p3,m3]=max(dis_xy2);%正半轴
        final ValueWithIndex pm3 = getMax(dis_xy2);
        //[p4,m4]=min(dis_xy2);%负半轴
        final ValueWithIndex pm4 = getMin(dis_xy2);
        //A1=p1-p2;     %矢量幅值   //该值为输出值，需要存库
        final double a1 = pm1.getVal() - pm2.getVal();
        //A2=p3-p4;     %矢量幅值   //该值为输出值，需要存库
        final double a2 = pm3.getVal() - pm4.getVal();

        final VectorAmplitudeResult result = VectorAmplitudeResult.builder()
                .leftx(leftx.stream().map(NumberFormatUtils::roundToDouble).collect(toList()))
                .lefty(lefty.stream().map(NumberFormatUtils::roundToDouble).collect(toList()))
                .rightx(rightx.stream().map(NumberFormatUtils::roundToDouble).collect(toList()))
                .righty(righty.stream().map(NumberFormatUtils::roundToDouble).collect(toList()))
                .theta1(roundToDouble(theta1))
                .theta2(roundToDouble(theta2))
                .a1(roundToDouble(a1))
                .a2(roundToDouble(a2))
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class VectorAmplitudeResult {
        /**
         * leftx：单位mm,左侧（对应通道1、2）X数组
         */
        private List<Double> leftx;
        /**
         * lefty：单位mm,左侧（对应通道1、2）Y数组
         */
        private List<Double> lefty;
        /**
         * rightx：单位mm,右侧（对应通道4、5）X数组
         */
        private List<Double> rightx;
        /**
         * righty：单位mm,右侧（对应通道4、5）Y数组
         */
        private List<Double> righty;
        /**
         * theta1：左侧（对应通道1、2）与水平方向夹角
         */
        private Double theta1;
        /**
         * theta2：右侧（对应通道4、5）与水平方向夹角
         */
        private Double theta2;
        /**
         * a1：左侧矢量振幅A1（对应通道1、2）
         */
        private Double a1;
        /**
         * a2：右侧矢量振幅A2（对应通道4、5）
         */
        private Double a2;
    }
}
