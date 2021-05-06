package com.tp.matlab.web.util;

import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.hamcrest.Matchers;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Created by tangpeng on 2021-04-30
 */
@Slf4j
@UtilityClass
public class AssertUtils {

    public static void stringEquals(@NonNull final List<String> expecteds, @NonNull final List<String> actuals) {
        for (int i = 0; i < actuals.size(); i++) {
            log.info("[{}]: expected:[{}], actuals:[{}]", i, expecteds.get(i), actuals.get(i));
            assertEquals(new BigDecimal(expecteds.get(i)), new BigDecimal(actuals.get(i)));
        }
    }

    public static void doubleEquals(@NonNull final List<Double> expecteds, @NonNull final List<Double> actuals) {
        for (int i = 0; i < actuals.size(); i++) {
            log.info(i + ": " + actuals.get(i));
            assertEquals(new BigDecimal(expecteds.get(i)), new BigDecimal(actuals.get(i)));
        }
    }

    public static void assertEquals(@NonNull BigDecimal expected, @NonNull BigDecimal actual) {
        assertThat(expected, Matchers.comparesEqualTo(actual));
    }


}
