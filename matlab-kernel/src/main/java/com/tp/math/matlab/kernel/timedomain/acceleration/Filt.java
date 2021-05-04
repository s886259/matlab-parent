package com.tp.math.matlab.kernel.timedomain.acceleration;

import com.github.psambit9791.jdsp.signal.Detrend;
import com.tp.math.matlab.kernel.core.DoubleMax;
import com.tp.math.matlab.kernel.core.ResultComplex;
import com.tp.math.matlab.kernel.transform.FFTTransformer;
import com.tp.math.matlab.kernel.transform.IFFTTransformer;
import com.tp.math.matlab.kernel.util.PythonUtils;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

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
    private FiltResult result;

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

    private FiltResult transform() {
        final Integer n = this.inputArray.size();
        final Double df = (double) this.fs / n;
        //final double a_fft= fft(detrend(a));
        final List<Double> detrend = detrend(this.inputArray);
        final List<ResultComplex> a_fft = new FFTTransformer().transform(detrend);
        final int n_inferior = (int) Math.round(this.flow / df);
        final int n_superior = (int) Math.round(this.fhigh / df);
        final ResultComplex[] k = new ResultComplex[this.inputArray.size()];
        for (int i = 0; i < k.length; i++) {
            if (i >= n_inferior - 1 && i < n_superior) {
                //k(n_inferior:n_superior)=a_fft(n_inferior:n_superior);
                k[i] = a_fft.get(i);
            } else if (i >= n - n_superior && i < n - n_inferior + 1) {
                //k(n-n_superior+1:n-n_inferior+1)=a_fft(n-n_superior+1:n-n_inferior+1);
                k[i] = a_fft.get(i);
            } else {
                k[i] = ResultComplex.of(0d, 0d);
            }
        }
        //[peak_mf,loc_mf]=max(abs_tmp);
        final DoubleMax k_max = PythonUtils.getMax(Arrays.stream(k).map(ResultComplex::getAbs).collect(toList()), n / 2);
        final double peak_mf = k_max.getVal();
        //matlab index from 1
        final double loc_mf = k_max.getIndex() + 1;

        //mf=loc_mf*df;
        final double mf = loc_mf * df;
        //a_time=ifft(k)
        final List<ResultComplex> a_time = new IFFTTransformer().transform(
                Arrays.stream(k).map(i -> new Complex(i.getReal(), i.getImag())).toArray(Complex[]::new)
        );
        // %滤波后数据
        final List<Double> a_fir = a_time.stream().map(ResultComplex::getReal).collect(toList());
        return FiltResult.of(a_fir, mf);
    }

    private List<Double> detrend(@NonNull final List<Double> signal) {
        final Detrend d1 = new Detrend(signal.stream().mapToDouble(i -> i).toArray(), "linear");
        final double[] out = d1.detrendSignal();
        return DoubleStream.of(out).boxed().collect(toList());
//        Assertions.assertArrayEquals(result, out, 0.001);
    }

    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class FiltResult {
        private final List<Double> a_fir;
        private final double mf;
    }
}
