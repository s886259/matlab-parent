package com.tp.matlab.extension.frequency.acceleration;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-20
 */
@RequiredArgsConstructor
class Screen {

    private final List<Integer> num_f;
    private final List<Double> a;

    public List<Double> execute() {
        //m=length(num_f);
        final int m = num_f.size();
        //valu=zeros(m,1);
        final List<Double> valu = DoubleStream.iterate(0, i -> i).boxed().limit(m).collect(toList());
        for (int i = 0; i < m; i++) {
            //n1=num_f(i)-1;
            final int n1 = num_f.get(i) - 1;
            //n2=num_f(i);
            final int n2 = num_f.get(i);
            //valu1=a(n1);
            final double valu1 = a.get(n1 - 1);
            //valu2=a(n2);
            final double valu2 = a.get(n2 - 1);
            //valu(i)=max([valu1 valu2]);    %取前一个和后一个点中的较大者
            valu.set(i, Math.max(valu1, valu2));
        }
        return valu;
    }

}
