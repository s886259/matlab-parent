package com.tp.math.matlab.controller;

import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * Created by tangpeng on 2021-04-28
 */
public abstract class BaseFFTController {

    public abstract ResponseEntity<List<String>> transform(@RequestBody TransformBody body);

    @Data
    protected static class TransformBody {
        private List<Position> positions;
    }

    @Data
    protected static class Position {
        private double x;
        private double y;
    }
}
