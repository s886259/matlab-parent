package com.tp.math.matlab.hann.transform;

import com.tp.math.matlab.util.MathUtils;
import lombok.experimental.UtilityClass;

/**
 * Created by tangpeng on 2021-04-24
 */
@UtilityClass
public class HanningWindowTransform {

    public static String[] transform(int length) {
        String[] recordedData = new String[length];
        for (int n = 0; n < length; n++) {
            double d = 0.5 * (1 - Math.cos((2 * Math.PI * n) / (length - 1)));
            recordedData[n] = MathUtils.roundToString(d, true);
        }
        return recordedData;
    }
}
