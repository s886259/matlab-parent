package com.tp.math.matlab.service;

import com.tp.math.matlab.kernel.filter.FirFilter;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangpeng on 2021-05-03
 */
@Service
public class FirFilterService {

    public FirFilter filter(
            @NonNull final List<Double> firArray,
            @NonNull final List<Double> inputArray) {
        return new FirFilter(firArray, inputArray);
    }
}
