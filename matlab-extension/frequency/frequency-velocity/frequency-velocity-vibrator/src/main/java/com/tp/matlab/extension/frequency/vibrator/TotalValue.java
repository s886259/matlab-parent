package com.tp.matlab.extension.frequency.vibrator;

import com.tp.matlab.kernel.transform.FFTTransformer;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-05
 */
@RequiredArgsConstructor
class TotalValue {
    /**
     * 源数据
     */
    private final List<Double> v;
    private final Integer fs;
    private final Double fmin;
    private final Double fmax;

    public double execute() {
        final int n = this.v.size();
        //df=fs/n;
        final double df = (double) fs / n;
        //n_inferior=round(fmin/df)+1;
        final int n_inferior = (int) Math.round(this.fmin / df) + 1;
        //n_superior=round(fmax/df)+1;
        final int n_superior = (int) Math.round(this.fmax / df) + 1;
        //A=abs(fft(v,n)*2/n);    %每个幅值
        final List<Double> A = new FFTTransformer().transform(v).stream()
                .map(i -> i.getAbs() * 2 / n)
                .collect(toList());
        //a1=0;
        double a1 = 0;
        //for i=n_inferior:n_superior
        for (int i = n_inferior - 1; i < n_superior; i++) {
            //a1=a1+A(i)^2;
            a1 = a1 + Math.pow(A.get(i), 2);
        }
        //i=n-n_superior:n-n_inferior
        for (int i = n - n_superior - 1; i < n - n_inferior; i++) {
            //a1=a1+A(i)^2;
            a1 = a1 + Math.pow(A.get(i), 2);
        }
        //TV=sqrt(a1)/sqrt(1.5);
        final double TV = Math.sqrt(a1) / Math.sqrt(1.5);
        return TV;
    }
}
