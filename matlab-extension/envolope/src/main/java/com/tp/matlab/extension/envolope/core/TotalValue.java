package com.tp.matlab.extension.envolope.core;

import com.tp.matlab.kernel.core.ResultComplex;
import com.tp.matlab.kernel.transform.FFTTransformer;
import com.tp.matlab.kernel.util.PythonUtils;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-05
 */
@RequiredArgsConstructor
class TotalValue {

    /**
     * 源数据
     */
    private final List<Double> a;
    private final Long fs;
    private final Integer fmin;
    private final Integer fmax;
    private final Integer m;

    public double execute() {
        final int n = this.a.size();
        //afft=fft(detrend(a),n);
        final List<Double> detrend = PythonUtils.detrend(this.a);
        final List<ResultComplex> afft = new FFTTransformer().transform(detrend);
        //df=fs/n;
        final double df = (double) fs / n;
        //n_inferior=round(fmin/df);
        final int n_inferior = (int) Math.round(this.fmin / df);
        //n_superior=round(fmax/df);
        final int n_superior = (int) Math.round(this.fmax / df);
        //f2=0:df:(fs/2)-df;
        final List<Double> f2 = DoubleStream.iterate(0, i -> i + df).limit((long) ((fs / 2) / df)).boxed().collect(toList());
        //%m=16;
        //vsum = zeros(n / m, 1);
        List<Double> vsum = DoubleStream.iterate(0, i -> i).limit((long) (n / m)).boxed().collect(toList());
        /**
         * k=1
         * matlab从1开始,这里k要取0
         */
        int k = 0;
        //for i=n_inferior:m:n_superior
        for (int i = n_inferior - 1; i < n_superior; i = i + m) {
            // for j=1:m-1
            for (int j = 0; j < m - 1; j++) {
                // vsum(k)=vsum(k)+abs(afft(j));
                vsum.set(k, vsum.get(k) + afft.get(j).getAbs());
            }
            k = k + 1;
        }
        //vsum=vsum/(m-1);
        vsum = vsum.stream().map(i -> i / (m - 1)).collect(toList());
        //vsum=vsum*2/n;
        vsum = vsum.stream().map(i -> i * 2 / n).collect(toList());
        //A = sqrt(sum(vsum. ^ 2)) / sqrt(1.5);
        final double A = Math.sqrt(vsum.stream().map(i -> Math.pow(i, 2)).mapToDouble(i -> i).sum())
                / Math.sqrt(1.5);
        return A;
    }
}
