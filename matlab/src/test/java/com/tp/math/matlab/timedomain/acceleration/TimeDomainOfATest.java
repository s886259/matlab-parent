package com.tp.math.matlab.timedomain.acceleration;

import com.tp.math.matlab.kernel.core.DoubleMax;
import com.tp.math.matlab.kernel.timedomain.acceleration.Filt;
import com.tp.math.matlab.kernel.timedomain.acceleration.Filt.FiltResult;
import com.tp.math.matlab.kernel.timedomain.acceleration.MeanValue;
import com.tp.math.matlab.kernel.timedomain.acceleration.ValueOfPeak;
import com.tp.math.matlab.kernel.timedomain.acceleration.ValueOfPeak.ValueOfPeakResult;
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

import static com.tp.math.matlab.base.AbstractTransformTest.TEST_EXCEL_COLUMN_INDEX;
import static com.tp.math.matlab.base.AbstractTransformTest.TIME_DOMAIN_TEST_EXCEL;

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
        final FiltResult filtResult = new Filt(records, fs, fcut, fs / 2.25).getResult();
        final double RPM = filtResult.getMf() * 60;
        //[p,m]=max(a_fir);
        final DoubleMax pm_max = PythonUtils.getMax(filtResult.getA_fir());
        //tm=m/fs;
        final double tm = pm_max.getIndex() / fs;
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(a_fir);
        final ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(filtResult.getA_fir()).getResult();
        //[vmean]=Mean_Value(a_fir);
        final double vmean = new MeanValue(filtResult.getA_fir()).getResult();
        //[sigma]=Value_of_Sigma(a_fir,vmean);
        System.out.println(vmean);
    }
}
