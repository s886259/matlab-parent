package com.tp.matlab.extension.vectoramplitude;

import com.tp.matlab.kernel.core.ResultComplex;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tangpeng on 2021-07-30
 */
@RequiredArgsConstructor
public class OnceIntegral {

    private final List<Double> w;
    private final List<ResultComplex> afft;

    public OnceIntegralResult execute() {
        //nn=length(w);
        final int nn = this.w.size();
        //R=imag(A)./w;
        final List<Double> rList = new ArrayList<>();
        //I=real(A)./w;
        final List<Double> iList = new ArrayList<>();
        final List<ResultComplex> complexes = new ArrayList<>();
        for (int index = 0; index < nn; index++) {
            final double r = afft.get(index).getImag() / w.get(index);
            rList.add(r);
            final double i = afft.get(index).getReal() / w.get(index);
            iList.add(i);
            complexes.add(ResultComplex.of(r, i));
        }
        complexes.set(0, ResultComplex.of(0d, 0d));
        complexes.set(nn - 1, ResultComplex.of(0d, 0d));
        return OnceIntegralResult.of(rList, iList, complexes);
    }


    @Getter
    @RequiredArgsConstructor(staticName = "of")
    static class OnceIntegralResult {
        private final List<Double> rv;
        private final List<Double> iv;
        private final List<ResultComplex> complexv;
    }
}
