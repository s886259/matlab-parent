package com.tp.matlab.web;

import com.tp.math.matlab.kernel.filter.FirFilter;
import com.tp.math.matlab.kernel.util.ExcelUtils;
import com.tp.math.matlab.kernel.windows.FirWindow;
import com.tp.matlab.web.base.AbstractTransformTest;
import com.tp.matlab.web.util.AssertUtils;
import com.tp.matlab.web.service.FirFilterService;
import com.tp.matlab.web.service.FirWindowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
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
import java.util.List;
import java.util.stream.DoubleStream;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-29
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class FirFilterServiceTest {

    private static final int HANNING_LENGTH = 100;
    private static final int FIR1_NUM_TAPS = HANNING_LENGTH + 1;
    private static final String FIR1_CUT_OFF = "[3.9063e-04,0.78125]";
    private static final String FILTER_RESULT_TXT = "/filter(%s,%s,hann(%s))_matlab_output.txt";

    @Autowired
    private FirFilterService firFilterService;
    @Autowired
    private FirWindowService firWindowService;

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
    public void testFilter() throws URISyntaxException, IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(AbstractTransformTest.TEST_EXCEL).getFile();
        //a=xlsRead('1414.xlsx',2);
        //inputArray=a(:,8);
        final List<Double> records = ExcelUtils.xlsRead(fileName, AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX - 1);
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
        final FirWindow d = firWindowService.fir1(N + 1, wp1);
        log.info("fir=fir1(N,wp1,window) result:\n" + d.format().toString());
        // x1 = filter(fir,1,inputArray);
        final FirFilter firFilter = firFilterService.filter(d.getResult(), records);
        log.info(firFilter.getResult().toString());

        //matlab output
        final URL resource = this.getClass().getResource(String.format(FILTER_RESULT_TXT, HANNING_LENGTH, FIR1_CUT_OFF, FIR1_NUM_TAPS));
        final List<String> expecteds = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());
        AssertUtils.stringEquals(expecteds, firFilter.format());

    }
}
