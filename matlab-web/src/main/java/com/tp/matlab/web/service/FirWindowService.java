package com.tp.matlab.web.service;

import com.tp.matlab.kernel.windows.FirWindow;
import lombok.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tangpeng on 2021-04-30
 */
@Service
public class FirWindowService {

    /**
     * matlab fir1()
     *
     * @param numtaps hanning window length(N+1)
     * @param cutoff
     * @return
     */
    public FirWindow fir1(@NonNull final Integer numtaps, @NonNull final List<Double> cutoff) {
        return this.fir1(numtaps, cutoff, null);
    }

    public FirWindow fir1(
            @NonNull final Integer numtaps,
            @NonNull final List<Double> cutoff,
            @Nullable final List<Double> hannWindow) {
        return new FirWindow(numtaps, cutoff, hannWindow);
    }
}