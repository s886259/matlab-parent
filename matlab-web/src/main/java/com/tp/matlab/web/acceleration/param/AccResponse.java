package com.tp.matlab.web.acceleration.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-08
 */
@Data
@ApiModel(description = "【加速度时序图】【返回值】")
public class AccResponse {

    @ApiModelProperty(value = "姓名", required = true)
    private List<Double> _Afir;
    @ApiModelProperty(value = "需要分析的通道序号")
    private Integer columnIndex;
    @ApiModelProperty(value = "时域值", required = true)
    private double tm;
    @ApiModelProperty(value = "峰值", required = true)
    private double p;
}
