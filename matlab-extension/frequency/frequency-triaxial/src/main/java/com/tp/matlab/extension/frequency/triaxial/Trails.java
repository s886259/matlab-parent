package com.tp.matlab.extension.frequency.triaxial;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.tp.matlab.kernel.core.DoubleMax;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.tp.matlab.kernel.util.NumberFormatUtils.roundToDecimal;
import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;
import static java.util.stream.Collectors.toList;

//import com.tp.matlab.extension.time.envolope.Filt.FiltResult;

/**
 * Created by tangpeng on 2021-07-13
 */
@Slf4j
public class Trails {

    /**
     * @param a 需要分析的列值
     * @return 分析后的结果
     */
    public Map<String, Object> execute(@NonNull final List<List<Double>> a) throws JsonProcessingException {
        /**
         * %%%%%%%%%%%%%%%%%%%%%%%%字母说明%%%%%%%%%%%%%%%%%%%%%%%%
         *     %   单位：mm
         *     %   phi_1:左侧轨迹角度值
         *     %   phi_2:右侧轨迹角度值
         *     %   delta：两侧轨迹角度差（反映了偏离程度）
         *     %   pks：幅值（峰峰值），从pks（1）到pks（6）分别为1~6通道的位移值
         * %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
         */
        final int N = a.size();   //%数据长度
        final long fs = 25600;           //%采样频率
        /**
         * x1=1000*a2x(a,1);y1=1000*a2x(a,2);z1=1000*a2x(a,3);
         * x2=1000*a2x(a,4);y2=1000*a2x(a,5);z2=1000*a2x(a,6);
         */
        final List<Double> x1 = new A2x(a, 1).execute().stream().map(i -> i * 1000).collect(toList());
        final List<Double> y1 = new A2x(a, 2).execute().stream().map(i -> i * 1000).collect(toList());
        final List<Double> z1 = new A2x(a, 3).execute().stream().map(i -> i * 1000).collect(toList());
        final List<Double> x2 = new A2x(a, 4).execute().stream().map(i -> i * 1000).collect(toList());
        final List<Double> y2 = new A2x(a, 5).execute().stream().map(i -> i * 1000).collect(toList());
        final List<Double> z2 = new A2x(a, 6).execute().stream().map(i -> i * 1000).collect(toList());
        //xx=[x1 y1 z1 x2 y2 z2];
        final List<List<Double>> xx = Arrays.asList(x1, y1, z1, x2, y2, z2);
        //pks=zeros(6,1);
        final List<Double> pks = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            // pks_x1=findpeaks(xx(:,i));%正峰值
            final List<Double> pks_x1 = MatlabUtils.findPeaks(xx.get(i)).stream()
                    .map(DoubleMax::getVal)
                    .collect(toList());
            // pks_x2=findpeaks(-1*xx(:,i));%负峰值
            final List<Double> pks_x2 = MatlabUtils.findPeaks(xx.get(i).stream().map(j -> j * -1).collect(toList()))
                    .stream()
                    .map(DoubleMax::getVal)
                    .collect(toList());
            // mx1=mean(pks_x1);
            final Double mx1 = pks_x1.stream().collect(Collectors.averagingDouble(p -> p));
            // mx2=mean(pks_x2);
            final Double mx2 = pks_x2.stream().collect(Collectors.averagingDouble(p -> p));
            pks.add(mx1 + mx2);
        }
        /**
         * x1_min=min(x1);
         * y1_min=min(y1);
         * x2_min=min(x2);
         * y2_min=min(y2);
         * x1_max=max(x1);
         * y1_max=max(y1);
         * x2_max=max(x2);
         * y2_max=max(y2);
         */
        final double x1_min = x1.stream().mapToDouble(x -> x).min().getAsDouble();
        final double y1_min = y1.stream().mapToDouble(x -> x).min().getAsDouble();
        final double x2_min = x2.stream().mapToDouble(x -> x).min().getAsDouble();
        final double y2_min = y2.stream().mapToDouble(x -> x).min().getAsDouble();
        final double x1_max = x1.stream().mapToDouble(x -> x).max().getAsDouble();
        final double y1_max = y1.stream().mapToDouble(x -> x).max().getAsDouble();
        final double x2_max = x2.stream().mapToDouble(x -> x).max().getAsDouble();
        final double y2_max = y2.stream().mapToDouble(x -> x).max().getAsDouble();
        //phi_1=atand((y1_max-y1_min)/(x1_max-x1_min));
        final double phi_1 = Math.toDegrees(Math.atan((y1_max - y1_min) / (x1_max - x1_min)));
        //phi_2=atand((y2_max-y2_min)/(x2_max-x2_min));
        final double phi_2 = Math.toDegrees(Math.atan((y2_max - y2_min) / (x2_max - x2_min)));
        //delta=abs(phi_2-phi_1);
        final double delta = Math.abs(phi_2 - phi_1);

        final TrailsResult result = new TrailsResult.TrailsResultBuilder()
                .x1(x1.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .y1(y1.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .z1(z1.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .x2(x2.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .y2(y2.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .z2(z2.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .phi_1(roundToDecimal(phi_1))
                .phi_2(roundToDecimal(phi_2))
                .delta(roundToDecimal(delta))
                .pks(pks.stream().map(NumberFormatUtils::roundToDecimal).collect(toList()))
                .build();
        return toValue(result, new TypeReference<Map<String, Object>>() {
        });
    }

    @Getter
    @Builder
    private static class TrailsResult {
        private List<BigDecimal> x1;
        private List<BigDecimal> y1;
        private List<BigDecimal> z1;
        private List<BigDecimal> x2;
        private List<BigDecimal> y2;
        private List<BigDecimal> z2;
        /**
         * 左侧轨迹角度值 单位：mm
         */
        private BigDecimal phi_1;
        /**
         * 右侧轨迹角度值 单位：mm
         */
        private BigDecimal phi_2;
        /**
         * 两侧轨迹角度差（反映了偏离程度）单位：mm
         */
        private BigDecimal delta;
        /**
         * 幅值（峰峰值），从pks（1）到pks（6）分别为1~6通道的位移值
         */
        private List<BigDecimal> pks;

    }
}
