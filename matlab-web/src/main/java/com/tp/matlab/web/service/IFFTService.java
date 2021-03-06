package com.tp.matlab.web.service;

import com.tp.matlab.kernel.transform.IFFTTransformer;
import com.tp.matlab.kernel.util.FileUtils;
import lombok.NonNull;
import org.apache.commons.math3.complex.Complex;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.tp.matlab.kernel.util.ExcelUtils.xlsRead;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class IFFTService {

    public List<String> transform(@NonNull final Complex[] f) {
        return new IFFTTransformer().transform(f).stream()
                .map(Object::toString)
                .collect(toList());
    }

    /**
     * @param fileName
     * @param columnIndex columnIndex start from 1
     */
    public List<String> transformFromFile(
            @NonNull final String fileName,
            @NonNull final Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final List<Double> records = xlsRead(fileName, columnIndex);
        //save source input file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, columnIndex), records);
        final Complex[] myComplexes = records.stream()
                .map(i -> new Complex(i, 0))
                .toArray(Complex[]::new);
        return transform(myComplexes);
    }
}
