package com.tp.matlab.web.acceleration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.web.acceleration.Param.AccListRequest;
import com.tp.matlab.web.acceleration.Param.AccResponse;
import com.tp.matlab.web.acceleration.service.AccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.tp.matlab.kernel.util.ObjectMapperUtils.toValue;

/**
 * Created by tangpeng on 2021-05-07
 */
@RestController
@RequestMapping(value = "/api/v1/acc/")
@RequiredArgsConstructor
@Api(value = "加速度时序图", tags = "加速度时序图")
public class AccController {

    private final AccService accService;

    @ApiOperation(value = "从数组中生成加速度时序图")
    @PostMapping("fromList")
    public ResponseEntity<AccResponse> fromList(
            @RequestBody @Valid AccListRequest accListRequest
    ) throws JsonProcessingException {
        final AccResponse response = toValue(accService.execute(accListRequest.getA()), AccResponse.class);
        return ResponseEntity.ok(response);
    }

}
