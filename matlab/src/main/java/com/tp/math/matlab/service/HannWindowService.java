package com.tp.math.matlab.service;

import com.tp.math.matlab.transform.HannWindow;
import com.tp.math.matlab.util.NumberFormatUtils;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class HannWindowService {

    public List<String> transform(@NonNull final Integer length) {
        return HannWindow.transform(length).stream()
                .map(NumberFormatUtils::roundToString)
                .collect(Collectors.toList());
    }
}
