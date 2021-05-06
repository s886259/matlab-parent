package com.tp.math.matlab.web.service;

import com.tp.math.matlab.kernel.windows.HannWindow;
import lombok.NonNull;
import org.springframework.stereotype.Service;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class HannWindowService {

    public HannWindow transform(@NonNull final Integer length) {
        return new HannWindow(length);
    }
}
