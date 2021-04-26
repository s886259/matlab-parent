package com.tp.math.matlab;

import com.tp.math.matlab.util.ExcelUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-04-26
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
public class ExcelTest {

    @Test
    public void testReadByColumn() throws InvalidFormatException, IOException {
        String fileName = "/Users/tangpeng/Documents/matlab/excels/1414.xlsx";
        Map<Integer, List<Double>> result = ExcelUtils.readByColumn(fileName);
        System.out.println(result.get(0));
        System.out.println(result.size());
    }

}
