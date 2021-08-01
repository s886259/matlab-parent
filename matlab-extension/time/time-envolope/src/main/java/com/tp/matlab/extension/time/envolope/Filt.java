package com.tp.matlab.extension.time.envolope;

import com.tp.matlab.kernel.core.ValueWithIndex;
import com.tp.matlab.kernel.core.ResultComplex;
import com.tp.matlab.kernel.transform.FFTTransformer;
import com.tp.matlab.kernel.transform.IFFTTransformer;
import com.tp.matlab.kernel.util.MatlabUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-04
 */
@RequiredArgsConstructor
class Filt {
    /**
     * 源数据
     */
    private final List<Double> a;
    /**
     * 采样频率
     */
    private final Integer fs;
    /**
     * 低频截止
     */
    private final Double flow;
    /**
     * 高频截止
     */
    private final Double fhigh;

    public FiltResult execute() {
        final Integer n = this.a.size();
        final Double df = (double) this.fs / n;
        //final double a_fft= fft(detrend(a));
        final List<Double> detrend = MatlabUtils.detrend(this.a);
        final List<ResultComplex> afft = new FFTTransformer().transform(detrend);
        final int n_inferior = (int) Math.round(this.flow / df);
        final int n_superior = (int) Math.round(this.fhigh / df);
        final ResultComplex[] k = new ResultComplex[this.a.size()];
        for (int i = 0; i < k.length; i++) {
            if (i >= n_inferior - 1 && i < n_superior) {
                //k(n_inferior:n_superior)=a_fft(n_inferior:n_superior);
                k[i] = afft.get(i);
            } else if (i >= n - n_superior && i < n - n_inferior + 1) {
                //k(n-n_superior+1:n-n_inferior+1)=a_fft(n-n_superior+1:n-n_inferior+1);
                k[i] = afft.get(i);
            } else {
                k[i] = ResultComplex.of(0d, 0d);
            }
        }
        //[peak_mf,loc_mf]=max(abs_tmp);
        final ValueWithIndex k_max = MatlabUtils.getMax(Arrays.stream(k).map(ResultComplex::getAbs).collect(toList()), n / 2);
        final double peak_mf = k_max.getVal();
        final double loc_mf = k_max.getIndex();

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


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    public static class FiltResult {
        private final List<Double> afir;
        private final double mf;
    }
}
