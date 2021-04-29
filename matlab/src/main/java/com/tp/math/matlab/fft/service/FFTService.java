package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.FFTTransformer;
import com.tp.math.matlab.service.excel.ExcelService;
import com.tp.math.matlab.util.FileUtils;
import fftManager.Complex;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
@RequiredArgsConstructor
public class FFTService {

    private final ExcelService excelService;

    public List<String> transform(@NonNull final Complex[] f) {
        return new FFTTransformer().transform(f).stream()
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
        final List<Double> records = excelService.readByColumn(fileName).get(columnIndex - 1);
        //TODO: result to file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, columnIndex), records);
        //TODO: result to file
        final Complex[] myComplexes = records.stream()
                .map(i -> new Complex(i, 0))
                .toArray(Complex[]::new);
        return transform(myComplexes);
    }
}
