package com.tp.math.matlab.fft.controller;

import com.tp.math.matlab.controller.BaseFFTController;
import com.tp.math.matlab.fft.service.FFTService;
import fftManager.Complex;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
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
@RequestMapping("/api/v1/fft")
@RequiredArgsConstructor
public class FFTController extends BaseFFTController {

    private final FFTService FFTService;

    @PostMapping()
    @Override
    public ResponseEntity<List<String>> transform(@RequestBody TransformBody body) {
        return ResponseEntity.ok(
                FFTService.transform(convert(body.getPositions()))
        );
    }

    private Complex[] convert(@NonNull final List<Position> complexes) {
        return complexes.stream()
                .map(i -> new Complex(i.getX(), i.getY()))
                .toArray(Complex[]::new);
    }

}
