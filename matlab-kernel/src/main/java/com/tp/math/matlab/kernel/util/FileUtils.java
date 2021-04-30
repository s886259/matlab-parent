package com.tp.math.matlab.kernel.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import static java.util.stream.Collectors.joining;

/**
 * Created by tangpeng on 2021-04-26
 */
@Slf4j
@UtilityClass
public class FileUtils {

    public static void double2File(
            @NonNull final String fileName,
            @NonNull final List<Double> records
    ) throws IOException {
        final BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        final String line = records.stream().map(Object::toString).collect(joining(","));
        out.write(line + "\t\n");
        out.close();
    }

    public static void string2File(
            @NonNull final String fileName,
            @NonNull final List<String> records
    ) throws IOException {
        final File file = new File(fileName);
        BufferedWriter out = new BufferedWriter(new FileWriter(file));
        log.info("Writing result to :" + file.getAbsolutePath());
        for (String line : records) {
            out.write(line + "\t\n");
        }
        out.close();
        log.info("Write result success :" + file.getAbsolutePath());
    }
}
