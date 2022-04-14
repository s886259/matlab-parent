package com.tp.matlab.kernel.domain.result;

import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tangpeng on 2022-04-02
 */
@Getter
@Builder
public class TimeResult {
    /**
     * rpp：真峰峰值
     */
    @NonNull
    private Double rpp;
    /**
     * time：总时间，时间范围：0~time
     */
    @NonNull
    private Double time;
    /**
     * A：幅值
     */
    @NonNull
    private Double a;
    /**
     * p：峰值；
     */
    @NonNull
    private Double p;
    /**
     * tm：时域值（峰值对应的时间点）
     */
    @NonNull
    private Double tm;
    /**
     * Pp：正峰值；
     */
    @NonNull
    private Double pp;
    /**
     * Np：负峰值
     */
    @NonNull
    private Double np;
    /**
     * vrms：均方根值
     */
    @NonNull
    private Double vrms;
    /**
     * sigma：标准偏差
     */
    @NonNull
    private Double sigma;
    /**
     * pf：波峰因素
     */
    @NonNull
    private Double pf;
    /**
     * ske：偏斜度
     */
    @NonNull
    private Double ske;
    /**
     * kur：峭度
     */
    @NonNull
    private Double kur;
    /**
     * TV：振动总值，m/s^2（用于计算整体趋势）
     */
    @NonNull
    private Double tv;
    @NonNull
    private List<Double> x;
    @NonNull
    private List<Double> y;
}
