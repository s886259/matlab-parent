package com.tp.math.matlab.web.service;

import com.tp.math.matlab.kernel.transform.FFTTransformer;
import com.tp.math.matlab.kernel.util.FileUtils;
import lombok.NonNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.tp.math.matlab.kernel.util.ExcelUtils.xlsRead;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-25
 */
@Service
public class FFTService {

    public List<String> transform(@NonNull final List<Double> f) {
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
        final List<Double> records = xlsRead(fileName, columnIndex - 1);
        //TODO: result to file
        FileUtils.double2File(String.format("%s_column%d_source_.txt", fileName, columnIndex), records);
        //TODO: result to file
        return transform(records);
    }
}
