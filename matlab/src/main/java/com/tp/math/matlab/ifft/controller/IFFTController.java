package com.tp.math.matlab.ifft.controller;

import com.tp.math.matlab.controller.BaseFFTController;
import com.tp.math.matlab.ifft.service.IFFTService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;
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
@RequestMapping("/api/v1/ifft")
@RequiredArgsConstructor
public class IFFTController extends BaseFFTController {

    private final IFFTService IFFTService;

    @PostMapping()
    @Override
    public ResponseEntity<List<String>> transform(@RequestBody TransformBody body) {
        return ResponseEntity.ok(
                IFFTService.transform(convert(body.getPositions()))
        );
    }

    private Complex[] convert(@NonNull final List<Position> complexes) {
        return complexes.stream()
                .map(i -> new Complex(i.getX(), i.getY()))
                .toArray(Complex[]::new);
    }
}
