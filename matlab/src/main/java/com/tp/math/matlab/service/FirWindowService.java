package com.tp.math.matlab.service;

import com.tp.math.matlab.transform.FirWindow;
import com.tp.math.matlab.util.NumberFormatUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-30
 */
@Service
public class FirWindowService {

    public List<String> fir1(final int numtaps, @NonNull final List<Double> cutoff) {
        return FirWindow.fir1(numtaps, cutoff).stream()
                .map(NumberFormatUtils::roundToString)
                .collect(Collectors.toList());
    }
}