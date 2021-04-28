package com.tp.math.matlab.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by tangpeng on 2021-04-26
 */
@UtilityClass
public class FileUtils {

    public void double2File(
            @NonNull final String fileName,
            @NonNull final List<Double> records
    ) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        final String line = records.stream().map(Object::toString).collect(joining(","));
        out.write(line + "\t\n");
        out.close();
    }

    public void string2File(
            @NonNull final String fileName,
            @NonNull final List<String> records
    ) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        for (String line : records) {
            out.write(line + "\t\n");
        }
        out.close();
    }
}
