package com.tp.matlab.web;

import com.tp.matlab.web.util.AssertUtils;
import com.tp.matlab.web.service.HannWindowService;
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

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-24
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HannWindowServiceTest {

    private static final String HANN_RESULT_TXT = "/hann(%s)_matlab_output.txt";

    @Autowired
    private HannWindowService hannWindowService;

    @Test
    public void testHannWindow() throws URISyntaxException, IOException {
        //matlab output
        final int length = 101;
        final URL resource = this.getClass().getResource(String.format(HANN_RESULT_TXT, length));
        final List<String> expecteds = Files.lines(Paths.get(resource.toURI()))
                .map(String::trim)
                .collect(toList());
        //my output
        final List<String> actuals = hannWindowService.transform(length).format();
        AssertUtils.stringEquals(expecteds, actuals);
    }
}
