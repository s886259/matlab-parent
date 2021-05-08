package com.tp.matlab.web.acceleration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.web.acceleration.param.AccFromListRequest;
import com.tp.matlab.web.acceleration.param.AccResponse;
import com.tp.matlab.web.acceleration.service.AccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import static com.tp.matlab.kernel.util.ExcelUtils.xlsRead;
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
        return execute(accFromListRequest.getA(), null);
    }

    @ApiOperation(value = "上传文件生成加速度时序图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "需要上传的excel", required = true, dataType = "__file"),
            @ApiImplicitParam(name = "columnIndex", value = "excel列(从1开始)", required = true, dataType = "int")}
    )
    @PostMapping("fromUpload")
    public ResponseEntity<AccResponse> fromUpload(
            @RequestPart MultipartFile file,
            @RequestParam Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final List<Double> records = xlsRead(file.getInputStream(), columnIndex);
        return execute(records, columnIndex);
    }

    @ApiOperation(value = "从URL生成加速度时序图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "e.g \"ftp://ip:port/*.xlsx\"", required = true, dataType = "string"),
            @ApiImplicitParam(name = "columnIndex", value = "excel列(从1开始)", required = true, dataType = "int")}
    )
    @PostMapping("fromUrl")
    public ResponseEntity<AccResponse> fromUrl(
            @RequestParam String url,
            @RequestParam Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final URLConnection conn = new URL(url).openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //得到输入流
        final InputStream inputStream = conn.getInputStream();
        final List<Double> records = xlsRead(inputStream, columnIndex);
        return execute(records, columnIndex);
    }

    private ResponseEntity<AccResponse> execute(
            @NonNull final List<Double> records,
            @Nullable Integer columnIndex
    ) throws JsonProcessingException {
        final AccResponse response = toValue(accService.execute(records), AccResponse.class);
        response.setColumnIndex(columnIndex);
        return ResponseEntity.ok(response);
    }

}
