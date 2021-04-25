package com.tp.math.matlab;

import com.tp.math.matlab.hann.transform.HanningWindowTransform;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HanningWindowTest {

    @Test
    public void testHanningWindow() {
        String[] doubles = HanningWindowTransform.transform(10);
        for (int i = 0; i < doubles.length; i++) {
            System.out.println(doubles[i]);
        }
    }
}
