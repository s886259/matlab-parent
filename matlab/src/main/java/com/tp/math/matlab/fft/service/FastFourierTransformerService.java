package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import com.tp.math.matlab.service.excel.ExcelService;
import com.tp.math.matlab.util.FileUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.transform.TransformType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
@RequiredArgsConstructor
public class FastFourierTransformerService {

    private final ExcelService excelService;

    public List<String> transform(final MyComplex[] f, final TransformType type) {
        MyFastFourierTransformer myFastFourierTransformer = new MyFastFourierTransformer();
        if (type == TransformType.FORWARD) {
            return myFastFourierTransformer.fft(f).stream().map(Object::toString).collect(Collectors.toList());
        } else {
            return myFastFourierTransformer.ifft(f).stream().map(Object::toString).collect(Collectors.toList());
        }
    }

    public List<String> transformFromFile(final String fileName, final TransformType type) throws IOException, InvalidFormatException {
        final Map<Integer, List<Double>> records = excelService.readByColumn(fileName);
        //TODO: result to file
        FileUtils.double2File(fileName + "_column1_source_.txt", records.get(0));
        //TODO: result to file
        final MyComplex[] myComplexes = records.get(0).stream()
                .map(i -> new MyComplex(i, 0))
                .toArray(MyComplex[]::new);
        return transform(myComplexes, type);
    }
}
