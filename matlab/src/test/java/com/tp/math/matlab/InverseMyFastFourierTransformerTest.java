package com.tp.math.matlab;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.math3.transform.DftNormalization.STANDARD;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InverseMyFastFourierTransformerTest {

    @Test
    public void testIFFT() {
        List<MyComplex> list = new ArrayList<>();
        list.add(new MyComplex(5.0));
        list.add(new MyComplex(2.0));
        list.add(new MyComplex(3.0));
        list.add(new MyComplex(4.0));
        list.add(new MyComplex(5.0));
        list.add(new MyComplex(6.0));
        list.add(new MyComplex(7.0));
        list.add(new MyComplex(9.0));
        List<MyComplex> complexes = new MyFastFourierTransformer(STANDARD).transform(list.toArray(new MyComplex[0]), TransformType.INVERSE);
        complexes.forEach(System.out::println);
    }
}
