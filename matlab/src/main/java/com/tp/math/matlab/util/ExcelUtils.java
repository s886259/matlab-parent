package com.tp.math.matlab.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by tangpeng on 2021-04-26
 */
@Slf4j
@UtilityClass
public class ExcelUtils {

    public Map<Integer, List<Double>> readByColumn(@NonNull final String fileName)
            throws InvalidFormatException, IOException {
        final Map<Integer, List<Double>> result = new HashMap<>();
        // 获取文件
        final File file = new File(fileName);
        //获取工作簿
        final XSSFWorkbook wb = new XSSFWorkbook(file); // XSSFWorkbook支持2007格式
        //获取excel表sheet
        final XSSFSheet sheet = Optional.ofNullable(wb.getSheet("加速度"))
                .orElseThrow(() -> new InvalidFormatException("工作簿加速度为空"));
        // 遍历sheet所有行
        for (int rowIndex = 0; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
            final XSSFRow row = sheet.getRow(rowIndex); // 获取行
            if (row == null) {
                continue;
            }
            //遍历所有列
            for (int columnIndex = 0; columnIndex <= row.getLastCellNum(); columnIndex++) {
                final XSSFCell cell = row.getCell(columnIndex);
                if (cell == null) {
                    continue;
                }
                //按列存值
                final List<Double> columnList = result.computeIfAbsent(columnIndex, integer -> new ArrayList<>());
                columnList.add(readVal(cell));
                result.put(columnIndex, columnList);
            }
        }
        return result;
    }

    /**
     * 列获取值操作
     */
    private static Double readVal(@NonNull final XSSFCell cell) throws InvalidFormatException {
        if (cell.getCellType() != XSSFCell.CELL_TYPE_NUMERIC) {
            throw new InvalidFormatException("Cell must be originNumber");
        }
        return cell.getNumericCellValue();
    }
}
