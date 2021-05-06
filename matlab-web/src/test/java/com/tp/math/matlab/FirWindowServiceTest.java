package com.tp.math.matlab;

import com.tp.math.matlab.web.service.FirWindowService;
import com.tp.math.matlab.util.AssertUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FirWindowServiceTest {

    private static final int HANNING_LENGTH = 100;
    private static final int FIR1_NUM_TAPS = HANNING_LENGTH + 1;
    private static final String FIR1_CUT_OFF = "[3.9063e-04,0.78125]";
    private static final String FIR1_RESULT_TXT = "/fir1(%s,%s,hann(%s))_matlab_output.txt";

    @Autowired
    private FirWindowService firWindowService;

    @Test
    public void testFir1() throws URISyntaxException, IOException {
        //my output
        final List<String> actuals = firWindowService.fir1(FIR1_NUM_TAPS, Arrays.asList(0.00039063, 0.78125)).format();
        //matlab output
        final URL resource = this.getClass().getResource(String.format(FIR1_RESULT_TXT, HANNING_LENGTH, FIR1_CUT_OFF, FIR1_NUM_TAPS));
        final List<String> expecteds = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());
        AssertUtils.stringEquals(expecteds, actuals);
    }

    /**
     * clc;
     * clear;
     * a=xlsRead('1414.xlsx',2);
     * inputArray=a(:,8);
     * fs=25600;
     * n=length(inputArray);
     * t=(0:n-1)/fs;
     * f=(0:n-1)/fs;
     * N=100;
     * window=hann(N+1);
     * wp1=[5/fs*2 10000/fs*2];
     * fir=fir1(N,wp1,window);
     * x1 = filter(fir,1,inputArray);
     * figure;
     * plot(t,x1);
     */
    @Test
    public void testTransformFromFile() {
//        final String fileName = this.getClass().getResource(TEST_EXCEL).getFile();
//        //a=xlsRead('1414.xlsx',2);
//        //inputArray=a(:,8);
//        final List<Double> records = ExcelUtils.xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX - 1);
        final int fs = 25600;
        //n=length(inputArray);
//        final int n = records.size();
        //t=(0:n-1)/fs
//        final List<Double> t = IntStream.range(0, n)
//                .mapToDouble(i -> (double) i)
//                .map(i -> i / fs)
//                .boxed()
//                .collect(toList());
//        log.info("t=(0:n-1)/fs result:\n" + t);
        //f=(0:n-1)/fs
//        final List<Double> f = t;
        //N=100
        final int N = 100;
        //window=hann(N+1)
//        final HannWindow window = hannWindowService.transform(N + 1);
//        log.info("window=hann(N+1) result:\n" + window.getResult());
        //wp1=[5/fs*2 10000/fs*2]
        final List<Double> wp1 = DoubleStream.of(5, 10000)
                .boxed()
                .map((i -> i / fs * 2))
                .collect(toList());
        log.info("wp1=[5/fs*2 10000/fs*2] result:\n" + wp1);
        // fir=fir1(N,wp1,window);
        final List<String> d = firWindowService.fir1(N + 1, wp1).format();
        log.info("fir=fir1(N,wp1,window) result:\n" + d.toString());
        //fir=fir1(N,wp1,window);
//        double[] inputArray = WindowFunction.getWindowFunc(HANNING, N + 1);
//        FirFilter firFilter = new FirFilter();
//        double y[] = new double[8192];  //output signal
//        for (int i = 0; i < 8192; i++) {
//            y[i] = firFilter.filter(inputArray[i]);
//        }
    }


}
