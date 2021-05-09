package com.tp.matlab.web.acceleration.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * Created by tangpeng on 2021-05-08
 */
@Data
public class AccFromArrayRequest {

    @NotEmpty(message = "【需要分析的列值】缺失")
    @ApiModelProperty(value = "需要分析的列值,必须为2的幂次个数", required = true)
    private List<Double> array;
}
