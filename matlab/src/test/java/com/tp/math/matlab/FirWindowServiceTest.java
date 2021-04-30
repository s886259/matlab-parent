package com.tp.math.matlab;

import com.tp.math.matlab.fir.service.FirWindowService;
import com.tp.math.matlab.util.AssertUtils;
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

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-29
 */
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
        final List<String> actuals = firWindowService.fir1(FIR1_NUM_TAPS, Arrays.asList(0.00039063, 0.78125));
        //matlab output
        final URL resource = this.getClass().getResource(String.format(FIR1_RESULT_TXT, HANNING_LENGTH, FIR1_CUT_OFF, FIR1_NUM_TAPS));
        final List<String> expecteds = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());
        AssertUtils.stringEquals(expecteds, actuals);
    }
}
