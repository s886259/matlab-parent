package com.tp.matlab.web.time.acceleration.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tp.matlab.web.param.FromArrayRequest;
import com.tp.matlab.web.time.acceleration.param.TimeAccResponse;
import com.tp.matlab.web.time.acceleration.service.TimeAccService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
@RestController
@RequestMapping(value = "/api/v1/time/acc/")
@RequiredArgsConstructor
@Api(value = "加速度时域图", tags = "加速度时域图")
public class TimeAccController {

    private final TimeAccService timeAccService;

    @ApiOperation(value = "从数组中生成加速度时域图")
    @PostMapping("fromArray")
    public ResponseEntity<TimeAccResponse> fromArray(
            @RequestBody @Valid FromArrayRequest fromArrayRequest
    ) throws JsonProcessingException {
        return execute(fromArrayRequest.getArray(), null, null);
    }

    @ApiOperation(value = "上传文件生成加速度时域图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "file", value = "需要上传的excel", required = true, dataType = "__file"),
            @ApiImplicitParam(name = "columnIndex", value = "excel列(从1开始)", required = true, dataType = "int")}
    )
    @PostMapping("fromUpload")
    public ResponseEntity<TimeAccResponse> fromUpload(
            @RequestPart MultipartFile file,
            @RequestParam Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final List<Double> records = xlsRead(file.getInputStream(), columnIndex);
        return execute(records, columnIndex, file.getOriginalFilename());
    }

    @ApiOperation(value = "从URL生成加速度时域图")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "url", value = "e.g \"ftp://ip:port/*.xlsx\"", required = true, dataType = "string"),
            @ApiImplicitParam(name = "columnIndex", value = "excel列(从1开始)", required = true, dataType = "int")}
    )
    @PostMapping("fromUrl")
    public ResponseEntity<TimeAccResponse> fromUrl(
            @RequestParam String url,
            @RequestParam Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final URLConnection conn = new URL(url).openConnection();
        //设置超时间为3秒
        conn.setConnectTimeout(3 * 1000);
        //得到输入流
        final InputStream inputStream = conn.getInputStream();
        final List<Double> records = xlsRead(inputStream, columnIndex);
        return execute(records, columnIndex, url);
    }

    private ResponseEntity<TimeAccResponse> execute(
            @NonNull final List<Double> records,
            @Nullable Integer columnIndex,
            @Nullable String file
    ) throws JsonProcessingException {
        final TimeAccResponse response = toValue(timeAccService.execute(records), TimeAccResponse.class);
        response.setColumnIndex(columnIndex);
        response.setFile(file);
        return ResponseEntity.ok(response);
    }

}
