package com.tp.matlab.extension.frequency.triaxial;

import com.tp.matlab.extension.frequency.triaxial.OnceIntegral.OnceIntegralResult;
import com.tp.matlab.kernel.core.ResultComplex;
import com.tp.matlab.kernel.transform.FFTTransformer;
import com.tp.matlab.kernel.transform.IFFTTransformer;
import com.tp.matlab.kernel.util.MatlabUtils;
import com.tp.matlab.kernel.util.NumberFormatUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.math3.complex.Complex;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-07-13
 */
@RequiredArgsConstructor
public class A2x {

    /**
     * 源数据
     */
    private final List<List<Double>> a;
    /**
     * 采样频率
     */
    private final int c;

    public List<Double> execute() {
        //fmin=5;fmax=300;
        final int fmin = 5;
        final int fmax = 300;
        //TODO: a1=a(:,c);
        final List<Double> a1 = a.get(c - 1);
        //fs=25600;
        final int fs = 25600;
        final int n = a1.size();     //%采样点数
        //t=(0:n-1)/fs; %时间
        final List<BigDecimal> t = DoubleStream.iterate(0, i -> i + 1)
                .limit(n)
                .map(i -> i / fs)
                .boxed()
                .map(NumberFormatUtils::roundToDecimal)
                .collect(toList());

        //final double a_fft= fft(detrend(a));
        final List<Double> detrend = MatlabUtils.detrend(a1);
        final List<ResultComplex> afft = new FFTTransformer().transform(detrend);
        //df=fs/n;
        final Double df = (double) fs / n;
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
        //n_inferior=round(fmin/df);
        final int n_inferior = (int) Math.round(fmin / df);
        //n_superior=round(fmax/df);
        final int n_superior = (int) Math.round(fmax / df);

        //[Rv,Iv,Complexv]=Once_integral(w,a_fft);
        final OnceIntegralResult onceIntegralResult = new OnceIntegral(w, afft).execute();
        //k=zeros(1,n);
//        List<Double> k = DoubleStream.iterate(0, i -> i).limit((long) (n)).boxed().collect(toList());

        final ResultComplex[] k = new ResultComplex[n];
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
        //v=real(v_time(1:n));
        final List<Double> v = v_time.stream().map(ResultComplex::getReal).collect(toList());


        /**
         * x
         */
        //v_fft=fft(detrend(v),n);
        final List<ResultComplex> vfft = new FFTTransformer().transform(MatlabUtils.detrend(v));

        //[Rv,Iv,Complexv]=Once_integral(w,a_fft);
        final OnceIntegralResult onceIntegralResultV = new OnceIntegral(w, vfft).execute();
        //k=zeros(1,n);
//        List<Double> k = DoubleStream.iterate(0, i -> i).limit((long) (n)).boxed().collect(toList());

        final ResultComplex[] k_x = new ResultComplex[n];
        for (int i = 0; i < k.length; i++) {
            if (i >= n_inferior - 1 && i < n_superior) {
                //k(n_inferior:n_superior)=Complexv(n_inferior:n_superior);
                k_x[i] = onceIntegralResultV.getComplexv().get(i);
            } else if (i >= n - n_superior && i < n - n_inferior + 1) {
                //k(n-n_superior+1:n-n_inferior+1)=Complexv(n-n_superior+1:n-n_inferior+1);
                k_x[i] = onceIntegralResultV.getComplexv().get(i);
            } else {
                k_x[i] = ResultComplex.of(0d, 0d);
            }
        }
        //x_time=ifft(k);
        final List<ResultComplex> x_time = new IFFTTransformer().transform(
                Arrays.stream(k_x).map(i -> new Complex(i.getReal(), i.getImag())).toArray(Complex[]::new)
        );
        //v=real(v_time(1:n));
        final List<Double> x = x_time.stream().map(ResultComplex::getReal).collect(toList());
        return x;
    }


}
