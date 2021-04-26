package com.tp.math.matlab.util;

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

    public void double2File(String fileName, List<Double> records) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
//        for (List<Double> column : result.values()) {
//            final String line = column.stream().map(Object::toString).collect(Collectors.joining(","));
//            out.write(line + "\t\n");
//        }
        final String line = records.stream().map(Object::toString).collect(joining(","));
        out.write(line + "\t\n");
        out.close();
    }

    public void string2File(String fileName, List<String> records) throws IOException {
        BufferedWriter out = new BufferedWriter(new FileWriter(fileName));
        for (String line : records) {
            out.write(line + "\t\n");
        }
        out.close();
    }
}
