package com.tp.math.matlab.kernel.timedomain.acceleration;

import com.github.psambit9791.jdsp.signal.Detrend;
import com.tp.math.matlab.kernel.core.ResultComplex;
import com.tp.math.matlab.kernel.transform.FFTTransformer;
import com.tp.math.matlab.kernel.util.NumberFormatUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@Accessors(chain = true)
public class Filt {

    /**
     * 源数据
     */
    private List<Double> inputArray;
    /**
     * 采样频率
     */
    private Long fs;
    /**
     * 低频截止
     */
    private Double flow;
    /**
     * 高频截止
     */
    private Double fhigh;
    /**
     * result
     */
    @Getter
    private List<Double> result;

    public Filt(
            @NonNull final List<Double> inputArray,
            @NonNull final Long fs,
            @NonNull final Double flow,
            @NonNull final Double fhigh
    ) {
        this.inputArray = inputArray;
        this.fs = fs;
        this.flow = flow;
        this.fhigh = fhigh;
        this.result = transform();
    }


    /**
     * python scipy.signal.lfilter
     */
    private List<Double> transform() {
        final Integer n = inputArray.size();
        final Double fs = (double) this.fs / n;
//        final double a_fft= fft(detrend(a));

//        double[] doubles = new double[]{0, 4.5693e-06, -2.7685e-05, 4.2908e-05, -2.7339e-05, -9.1154e-05, 0.00022097, -0.00035567, 0.00023626, 2.8839e-05};
        final List<Double> detrend = detrend(this.inputArray);
        List<ResultComplex> transform = new FFTTransformer().transform(detrend);
        return null;

    }

    private List<Double> detrend(@NonNull final List<Double> signal) {
        final Detrend d1 = new Detrend(signal.stream().mapToDouble(i -> i).toArray(), "linear");
        final double[] out = d1.detrendSignal();
        return DoubleStream.of(out).boxed().collect(Collectors.toList());
//        Assertions.assertArrayEquals(result, out, 0.001);
    }

    /**
     * 格式化结果
     */
    public List<String> format() {
        return this.getResult().stream()
                .map(NumberFormatUtils::roundToString)
                .collect(Collectors.toList());
    }
}
