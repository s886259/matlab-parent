package com.tp.math.matlab;

import com.tp.math.matlab.service.HannWindowService;
import com.tp.math.matlab.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;

import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL;
import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class Fir1Test {

    @Autowired
    private HannWindowService hannWindowService;

    /**
     * clc;
     * clear;
     * a=xlsread('1414.xlsx',2);
     * x=a(:,8);
     * fs=25600;
     * n=length(x);
     * t=(0:n-1)/fs;
     * f=(0:n-1)/fs;
     * N=100;
     * window=hann(N+1);
     * wp1=[5/fs*2 10000/fs*2];
     * b=fir1(N,wp1,window);
     * x1 = filter(b,1,x);
     * figure;
     * plot(t,x1);
     */
    @Test
    public void testFir1() throws IOException, InvalidFormatException {
        final String fileName = this.getClass().getResource(TEST_EXCEL).getFile();
        //a=xlsread('1414.xlsx',2);
        //x=a(:,8);
        final List<Double> records = ExcelUtils.readColumn(fileName, TEST_EXCEL_COLUMN_INDEX - 1);
        final int fs = 25600;
        //n=length(x);
        final int n = records.size();
        //t=(0:n-1)/fs
        final List<Double> t = IntStream.range(0, n)
                .mapToDouble(i -> (double) i)
                .map(i -> i / fs)
                .boxed()
                .collect(toList());
        log.info("t=(0:n-1)/fs result:\n" + t);
        //f=(0:n-1)/fs
        final List<Double> f = t;
        //N=100
        final int N = 100;
        //window=hann(N+1)
        final List<String> window = hannWindowService.transform(N + 1);
        log.info("window=hann(N+1) result:\n" + window);
        //wp1=[5/fs*2 10000/fs*2]
        final List<Double> wp1 = DoubleStream.of(5, 10000)
                .boxed()
                .map((i -> i / fs * 2))
                .collect(toList());
        log.info("wp1=[5/fs*2 10000/fs*2] result:\n" + wp1);
        //b=fir1(N,wp1,window);
//        double[] x = WindowFunction.getWindowFunc(HANNING, N + 1);
//        FirFilter firFilter = new FirFilter();
//        double y[] = new double[8192];  //output signal
//        for (int i = 0; i < 8192; i++) {
//            y[i] = firFilter.filter(x[i]);
//        }
    }
}
