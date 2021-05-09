package com.tp.matlab.web.acceleration.param;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-08
 */
@Data
@ApiModel(description = "【加速度时序图】【返回值】")
@NoArgsConstructor
public class AccResponse {

    @ApiModelProperty(value = "需要分析的通道序号")
    private Integer columnIndex;

    @ApiModelProperty(value = "姓名", required = true)
    private List<Double> afir;

    @ApiModelProperty(value = "实际转速", required = true)
    private Double rpm;

    @ApiModelProperty(value = "总时间，时间范围：0~time", required = true)
    private Double time;

    @ApiModelProperty(value = "幅值", required = true)
    private Double a;

    @ApiModelProperty(value = "峰值出现的位置", required = true)
    private Integer m;

    @ApiModelProperty(value = "峰值", required = true)
    private Double p;

    @ApiModelProperty(value = "时域值（峰值对应的时间点）", required = true)
    private Double tm;

    @ApiModelProperty(value = "正峰值", required = true)
    private Double pp;

    @ApiModelProperty(value = "负峰值", required = true)
    private Double np;

    @ApiModelProperty(value = "均值", required = true)
    private Double vmean;

    @ApiModelProperty(value = "均方根值", required = true)
    private Double vrms;

    @ApiModelProperty(value = "标准偏差", required = true)
    private Double sigma;

    @ApiModelProperty(value = "波峰因素", required = true)
    private Double pf;

    @ApiModelProperty(value = "偏斜度", required = true)
    private Double ske;

    @ApiModelProperty(value = "峭度", required = true)
    private Double kur;

    @ApiModelProperty(value = "振动总值，m/s^2（用于计算整体趋势）", required = true)
    private Double tv;

}
