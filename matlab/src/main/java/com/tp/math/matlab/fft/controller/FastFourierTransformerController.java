package com.tp.math.matlab.fft.controller;

import com.tp.math.matlab.fft.service.FastFourierTransformerService;
import com.tp.math.matlab.fft.transform.MyComplex;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.transform.TransformType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by tangpeng on 2021-04-25
 */
@RestController
@RequestMapping("/api/v1/")
@RequiredArgsConstructor
public class FastFourierTransformerController {

    private final FastFourierTransformerService fastFourierTransformerService;

    @PostMapping("fft")
    public ResponseEntity<List<String>> transform(
            @RequestBody TransformBody body) {
        return ResponseEntity.ok(
                fastFourierTransformerService.transform(convert(body.getPositions()), TransformType.FORWARD)
        );
    }

    @PostMapping("ifft")
    public ResponseEntity<List<String>> inverseTransform(
            @RequestBody TransformBody body) {
        return ResponseEntity.ok(
                fastFourierTransformerService.transform(convert(body.getPositions()), TransformType.INVERSE)
        );
    }

    private MyComplex[] convert(List<Position> complexes) {
        return complexes.stream()
                .map(i -> new MyComplex(i.getX(), i.getY()))
                .toArray(MyComplex[]::new);
    }

    @Data
    private static class TransformBody {
        private List<Position> positions;
    }

    @Data
    private static class Position {
        private double x;
        private double y;
    }
}
