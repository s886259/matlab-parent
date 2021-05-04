package com.tp.math.matlab.timedomain.acceleration;

import com.tp.math.matlab.kernel.core.DoubleMax;
import com.tp.math.matlab.kernel.timedomain.acceleration.Filt;
import com.tp.math.matlab.kernel.transform.FirFilter;
import com.tp.math.matlab.kernel.transform.FirWindow;
import com.tp.math.matlab.kernel.util.ExcelUtils;
import com.tp.math.matlab.kernel.util.PythonUtils;
import com.tp.math.matlab.service.FirFilterService;
import com.tp.math.matlab.service.FirWindowService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.DoubleStream;

import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;
import static com.tp.math.matlab.base.AbstractTransformTest.TIME_DOMAIN_TEST_EXCEL;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-05-04
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class TimeDomainOfATest {

    @Autowired
    private FirFilterService firFilterService;
    @Autowired
    private FirWindowService firWindowService;

    @Test
    public void testMain() throws URISyntaxException, IOException, InvalidFormatException {
        //my output
        final String fileName = this.getClass().getResource(TIME_DOMAIN_TEST_EXCEL).getFile();
        //a=xlsRead('1414.xlsx',2);
        //inputArray=a(:,8);
        final List<Double> records = ExcelUtils.xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX - 1);
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = records.size();   //%数据长度
        //df=fs/N;
        final double df = fs / N;
        final double fcut = 5;          //%低频截止
        final double fmin = 5;          //%fmin：起始频率
        final double fmax = 10000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.25);
        final Filt.FiltResult filtResult = new Filt(records, fs, fcut, fs / 2.25).getResult();
        final double RPM = filtResult.getMf() * 60;
        //[p,m]=max(a_fir);
        final DoubleMax pm_max = PythonUtils.getMax(filtResult.getA_fir());
        //tm=m/fs;
        final double tm = pm_max.getIndex() / fs;
        final double A = pm_max.getVal();

//        //t=(0:n-1)/fs
//        final List<Double> t = IntStream.range(0, n)
//                .mapToDouble(i -> (double) i)
//                .map(i -> i / fs)
//                .boxed()
//                .collect(toList());
//        log.info("t=(0:n-1)/fs result:\n" + t);
//        //f=(0:n-1)/fs
//        final List<Double> f = t;
//        //N=100
//        final int N = 100;
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
//        final URL resource = this.getClass().getResource(String.format(FILTER_RESULT_TXT, HANNING_LENGTH, FIR1_CUT_OFF, FIR1_NUM_TAPS));
//        final List<String> expecteds = Files.lines(Paths.get(resource.toURI()))
//                .map(String::trim)
//                .collect(toList());
//        AssertUtils.stringEquals(expecteds, firFilter.format());
    }
}
