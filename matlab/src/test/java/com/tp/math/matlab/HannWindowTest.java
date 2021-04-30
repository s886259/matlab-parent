package com.tp.math.matlab;

import com.tp.math.matlab.hann.service.HannWindowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
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

    private static final String HANN_RESULT_TXT = "/hann(%s)_matlab_output.txt";

    @Autowired
    private HannWindowService hannWindowService;

    @Test
    public void testHannWindow() throws URISyntaxException, IOException {
        final int length = 101;
        final URL resource = this.getClass().getResource(String.format(HANN_RESULT_TXT, length));
        final List<String> expects = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());

        final List<String> result = hannWindowService.transform(length);

        for (int i = 0; i < result.size(); i++) {
            System.out.println(i + ": " + result.get(i));
            Assert.assertEquals(new BigDecimal(expects.get(i)), new BigDecimal(result.get(i)));
        }
    }
}
