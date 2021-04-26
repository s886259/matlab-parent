package com.tp.math.matlab.service.excel;

import com.tp.math.matlab.util.ExcelUtils;
import lombok.NonNull;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by tangpeng on 2021-04-26
 */
@Service
public class ExcelService {
    public Map<Integer, List<Double>> readByColumn(@NonNull final String fileName)
            throws InvalidFormatException, IOException {
        return ExcelUtils.readByColumn(fileName);
    }

}
