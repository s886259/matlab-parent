package com.tp.math.matlab.hann.service;

import com.tp.math.matlab.hann.transform.HanningWindow;
import lombok.NonNull;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class HannWindowService {

    public List<String> transform(@NonNull final Integer length) {
        return HanningWindow.transform(length);
    }
}
