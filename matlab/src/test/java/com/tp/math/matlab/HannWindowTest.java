package com.tp.math.matlab;

import com.tp.math.matlab.hann.transform.HannWindow;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HannWindowTest {

    private static final String HANN_RESULT_TXT = "/Hann(%s)_matlab_output.txt";

    @Test
    public void testHannWindow() throws URISyntaxException, IOException {
        final int length = 1000;
        final URL resource = this.getClass().getResource(String.format(HANN_RESULT_TXT, length));
        final List<String> expects = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());

        String[] doubles = HannWindow.transform(length);
        for (int i = 0; i < doubles.length; i++) {
            System.out.println(i + ": " + doubles[i]);
            Assert.assertEquals(new BigDecimal(expects.get(i)), new BigDecimal(doubles[i]));
        }
    }
}
