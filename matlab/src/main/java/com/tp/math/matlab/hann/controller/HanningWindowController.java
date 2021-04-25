package com.tp.math.matlab.hann.controller;

import com.tp.math.matlab.hann.service.HanningWindowTransformerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tangpeng on 2021-04-25
 */
@RestController
@RequestMapping("/api/v1/hann")
@RequiredArgsConstructor
public class HanningWindowController {

    private final HanningWindowTransformerService hanningWindowTransformerService;

    @GetMapping
    public ResponseEntity<String[]> transform(@RequestParam Integer length) {
        return ResponseEntity.ok(hanningWindowTransformerService.transform(length));
    }
}
