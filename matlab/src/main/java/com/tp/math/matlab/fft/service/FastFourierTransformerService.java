package com.tp.math.matlab.fft.service;

import com.tp.math.matlab.fft.transform.MyFastFourierTransformer;
import com.tp.math.matlab.fft.transform.OriginComplex;
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

    public List<String> transform(@NonNull final OriginComplex[] f, @NonNull final TransformType type) {
        final MyFastFourierTransformer myFastFourierTransformer = new MyFastFourierTransformer();
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

    /**
     * @param fileName
     * @param type
     * @param columnIndex columnIndex start from 1
     */
    public List<String> transformFromFile(
            @NonNull final String fileName,
            @NonNull final TransformType type,
            @NonNull final Integer columnIndex
    ) throws IOException, InvalidFormatException {
        final Map<Integer, List<Double>> records = excelService.readByColumn(fileName);
        //TODO: result to file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, columnIndex), records.get(columnIndex - 1));
        //TODO: result to file
        final OriginComplex[] myComplexes = records.get(columnIndex - 1).stream()
                .map(i -> new OriginComplex(i, 0))
                .toArray(OriginComplex[]::new);
        return transform(myComplexes, type);
    }
}
