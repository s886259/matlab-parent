package com.tp.matlab.web.acceleration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.kernel.util.ExcelUtils;
import com.tp.matlab.web.acceleration.Param.AccFromListRequest;
import com.tp.matlab.web.acceleration.Param.AccResponse;
import com.tp.matlab.web.acceleration.service.AccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

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
            @RequestBody @Valid AccFromListRequest accFromListRequest
    ) throws JsonProcessingException {
        final AccResponse response = toValue(accService.execute(accFromListRequest.getA()), AccResponse.class);
        return ResponseEntity.ok(response);
    }

    @ApiOperation(value = "上传文件生成加速度时序图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "需要上传的excel", required = true, dataType = "__file"),
            @ApiImplicitParam(name = "columnIndex", value = "excel列(从1开始)", required = true, dataType = "int")}
    )
    @PostMapping("upload")
    public ResponseEntity<AccResponse> upload(
            @RequestPart MultipartFile file,
            @RequestParam Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final List<Double> records = ExcelUtils.xlsRead(file.getInputStream(), columnIndex);
        final AccResponse response = toValue(accService.execute(records, columnIndex), AccResponse.class);
        return ResponseEntity.ok(response);
    }

}
