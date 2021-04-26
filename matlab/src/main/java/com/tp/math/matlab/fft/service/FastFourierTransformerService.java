package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyComplex;
import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import com.tp.math.matlab.service.excel.ExcelService;
import com.tp.math.matlab.util.FileUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math3.transform.TransformType;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.apache.commons.math3.transform.TransformType.FORWARD;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
@RequiredArgsConstructor
public class FastFourierTransformerService {

    private final ExcelService excelService;

    public List<String> transform(@NonNull final MyComplex[] f, @NonNull final TransformType type) {
        MyFastFourierTransformer myFastFourierTransformer = new MyFastFourierTransformer();
        if (type == FORWARD) {
            return myFastFourierTransformer.fft(f).stream()
                    .map(Object::toString)
                    .collect(toList());
        } else {
            return myFastFourierTransformer.ifft(f).stream()
                    .map(Object::toString)
                    .collect(toList());
        }
    }

    public List<String> transformFromFile(@NonNull final String fileName, @NonNull final TransformType type) throws IOException, InvalidFormatException {
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
