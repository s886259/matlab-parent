package com.tp.math.matlab.timedomain.acceleration;

import com.tp.math.matlab.extension.acceleration.core.*;
import com.tp.math.matlab.extension.acceleration.core.Filt.FiltResult;
import com.tp.math.matlab.extension.acceleration.core.ValueOfPeak.ValueOfPeakResult;
import com.tp.math.matlab.kernel.core.DoubleMax;
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
        final List<Double> a = ExcelUtils.xlsRead(fileName, TEST_EXCEL_COLUMN_INDEX - 1);
        final long fs = 25600;           //%采样频率
        //n=length(inputArray);
        final int N = a.size();   //%数据长度
        //df=fs/N;
        final double df = fs / N;
        final double fcut = 5;          //%低频截止
        final double fmin = 5;          //%fmin：起始频率
        final double fmax = 10000;      //famx：终止频率
        final double time = (double) N / fs;
        //[a_fir,mf]=filt(a,fs,fcut,fs/2.25);
        final FiltResult filtResult = new Filt(a, fs, fcut, fs / 2.25).getResult();
        final double RPM = filtResult.get_mf() * 60;
        //[p,m]=max(a_fir);
        final DoubleMax pm_max = PythonUtils.getMax(filtResult.get_Afir());
        //tm=m/fs;
        final double tm = pm_max.getIndex() / fs;
        final double A = pm_max.getVal();
        //[Pp,Np]=Value_of_Peak(a_fir);
        final ValueOfPeakResult valueOfPeakResult = new ValueOfPeak(filtResult.get_Afir()).getResult();
        //[vmean]=Mean_Value(a_fir);
        final double vmean = new MeanValue(filtResult.get_Afir()).getResult();
        //[sigma]=Value_of_Sigma(a_fir,vmean);
        final double sigma = new ValueOfSigma(filtResult.get_Afir(), vmean).getResult();
        //[vrms]=Value_of_RMS(a_fir);
        final double vrms = new ValueOfRMS(filtResult.get_Afir()).getResult();
        //pf=p/vrms;
        final double pf = pm_max.getVal() / vrms;
        //[ske]=Value_of_Skewness(a_fir,vmean);
        final double ske = new ValueOfSkeness(filtResult.get_Afir(), vmean).getResult();
        //[ske]=Value_of_Kurtosis(a_fir,vmean,sigma);
        final double kur = new ValueOfKurtosis(filtResult.get_Afir(), vmean, sigma).getResult();
        //[TV]=total_value(a,fs,5,10000,16);
    }
}
