package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import com.tp.math.matlab.util.ExcelUtils;
import org.apache.commons.math3.transform.TransformType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class FastFourierTransformerService {

    public List<String> transform(final MyComplex[] f, final TransformType type) {
        MyFastFourierTransformer myFastFourierTransformer = new MyFastFourierTransformer();
        if (type == TransformType.FORWARD) {
            return myFastFourierTransformer.fft(f).stream().map(Object::toString).collect(Collectors.toList());
        } else {
            return myFastFourierTransformer.ifft(f).stream().map(Object::toString).collect(Collectors.toList());
        }
    }

    public List<String> transformFromFile(final String fileName, final TransformType type) throws IOException, InvalidFormatException {
        final Map<Integer, List<Double>> records = ExcelUtils.readByColumn(fileName);
        resultToFile(fileName, records);

        final MyComplex[] myComplexes = records.get(0).stream()
                .map(i -> new MyComplex(i, 0))
                .toArray(MyComplex[]::new);
        return transform(myComplexes, type);
    }

    private void resultToFile(String fileName, Map<Integer, List<Double>> records) throws IOException {
        //写入文件
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName + "_.txt"));
//        for (List<Double> column : result.values()) {
//            final String line = column.stream().map(Object::toString).collect(Collectors.joining(","));
//            out.write(line + "\t\n");
//        }
        final String line = records.get(0).stream().map(Object::toString).collect(Collectors.joining(","));
        out.write(line + "\t\n");
        out.close();
    }
}
