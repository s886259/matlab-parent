package com.tp.matlab.extension.time.envolope;

import com.tp.matlab.kernel.core.ResultComplex;
import com.tp.matlab.kernel.transform.FFTTransformer;
import com.tp.matlab.kernel.transform.IFFTTransformer;
import com.tp.matlab.kernel.util.MatlabUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.math3.complex.Complex;

import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-10
 */
@RequiredArgsConstructor
class PinPu {

    /**
     * 源数据
     */
    private final List<Double> a;
    /**
     * 采样频率
     */
    private final Long fs;
    /**
     * 低频截止
     */
    private final Double flow;
    /**
     * 高频截止
     */
    private final Double fhigh;

    public List<Double> execute() {
        final Integer n = this.a.size();     //%采样点数
        //final double a_fft= fft(detrend(a));
        final List<Double> detrend = MatlabUtils.detrend(this.a);
        final List<ResultComplex> afft = new FFTTransformer().transform(detrend);
        //df=fs/n;
        final Double df = (double) this.fs / n;
        //f1=-(fs/2):df:-df;
        final List<Double> f1 = DoubleStream.iterate((double) -fs / 2, i -> i + df)
                .limit((long) ((double) fs / 2 / df))
                .boxed()
                .collect(toList());
        //f2=0:df:(fs/2)-df;
        final List<Double> f2 = DoubleStream.iterate(0, i -> i + df)
                .limit((long) ((double) fs / 2 / df))
                .boxed()
                .collect(toList());
        final List<Double> f = ListUtils.union(f2, f1);
        //w=2*pi*f;
        final List<Double> w = f.stream().map(i -> 2 * Math.PI * i).collect(toList());
        final int n_inferior = (int) Math.round(this.flow / df);
        final int n_superior = (int) Math.round(this.fhigh / df);
        //[Rv,Iv,Complexv]=Once_integral(w,a_fft);
        final OnceIntegral.OnceIntegralResult onceIntegralResult = new OnceIntegral(w, afft).execute();
        final ResultComplex[] k = new ResultComplex[this.a.size()];
        for (int i = 0; i < k.length; i++) {
            if (i >= n_inferior - 1 && i < n_superior) {
                //k(n_inferior:n_superior)=Complexv(n_inferior:n_superior);
                k[i] = onceIntegralResult.getComplexv().get(i);
            } else if (i >= n - n_superior && i < n - n_inferior + 1) {
                //k(n-n_superior+1:n-n_inferior+1)=Complexv(n-n_superior+1:n-n_inferior+1);
                k[i] = onceIntegralResult.getComplexv().get(i);
            } else {
                k[i] = ResultComplex.of(0d, 0d);
            }
        }
        //v_time=ifft(k);
        final List<ResultComplex> v_time = new IFFTTransformer().transform(
                Arrays.stream(k).map(i -> new Complex(i.getReal(), i.getImag())).toArray(Complex[]::new)
        );

        final List<Double> v = MatlabUtils.detrend( //v=detrend(v);
                v_time.stream().map(ResultComplex::getReal).collect(toList()))  //v=real(v_time(1:n));
                .stream().map(i -> 1000 * i)    //v=1000*v';
                .collect(toList());
        return v;
    }


}
