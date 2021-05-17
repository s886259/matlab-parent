package com.tp.matlab.web.time.acceleration.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-08
 */
@Data
@ApiModel(description = "【加速度时序图】【返回值】")
@NoArgsConstructor
public class TimeAccResponse {

    @ApiModelProperty(value = "需要分析的文件")
    private String file;

    @ApiModelProperty(value = "需要分析的通道序号")
    private Integer columnIndex;

    @ApiModelProperty(value = "实际转速", required = true)
    private BigDecimal rpm;

    @ApiModelProperty(value = "总时间，时间范围：0~time", required = true)
    private BigDecimal time;

    @ApiModelProperty(value = "幅值", required = true)
    private BigDecimal a;

    @ApiModelProperty(value = "峰值出现的位置", required = true)
    private Integer m;

    @ApiModelProperty(value = "峰值", required = true)
    private BigDecimal p;

    @ApiModelProperty(value = "时域值（峰值对应的时间点）", required = true)
    private BigDecimal tm;

    @ApiModelProperty(value = "正峰值", required = true)
    private BigDecimal pp;

    @ApiModelProperty(value = "负峰值", required = true)
    private BigDecimal np;

    @ApiModelProperty(value = "均值", required = true)
    private BigDecimal vmean;

    @ApiModelProperty(value = "均方根值", required = true)
    private BigDecimal vrms;

    @ApiModelProperty(value = "标准偏差", required = true)
    private BigDecimal sigma;

    @ApiModelProperty(value = "波峰因素", required = true)
    private BigDecimal pf;

    @ApiModelProperty(value = "偏斜度", required = true)
    private BigDecimal ske;

    @ApiModelProperty(value = "峭度", required = true)
    private BigDecimal kur;

    @ApiModelProperty(value = "振动总值，m/s^2（用于计算整体趋势）", required = true)
    private BigDecimal tv;

    @ApiModelProperty(value = "x轴,时间/s", required = true)
    private List<BigDecimal> x;

    @ApiModelProperty(value = "y轴,加速度时域/ (m/s^2）", required = true)
    private List<BigDecimal> y;

}
