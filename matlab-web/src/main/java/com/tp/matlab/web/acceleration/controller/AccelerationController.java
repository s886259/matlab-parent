package com.tp.matlab.web.acceleration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.web.acceleration.service.AccelerationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-05-07
 */
@RestController
@RequestMapping(value = "/api/v1/acc/")
@RequiredArgsConstructor
@Api(value = "加速度时序图", tags = "加速度时序图")
public class AccelerationController {

    private final AccelerationService accelerationService;

    @ApiOperation(value = "从指定值生成加速度时序图")
    @PostMapping
    public ResponseEntity<Map<String, Object>> run(@RequestBody TransformBody body) throws JsonProcessingException {
        return ResponseEntity.ok(
                accelerationService.execute(body.getA(), body.getC())
        );
    }

    @Data
    protected static class TransformBody {
        private List<Double> a;
        private Integer c;
    }
}
