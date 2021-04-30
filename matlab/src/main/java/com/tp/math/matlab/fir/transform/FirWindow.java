package com.tp.math.matlab.fir.transform;

import com.tp.math.matlab.hann.transform.HannWindow;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import static java.lang.Math.sin;
import static java.util.stream.Collectors.toList;

/**
 * Created by tangpeng on 2021-04-30
 *
 * <p>
 * import scipy.signal
 * scipy.signal.fir1(101, [0.00039063,0.78125], window="hann", pass_zero=False)
 */
@Slf4j
@UtilityClass
public class FirWindow {

    /**
     * @param numtaps Length of the filter (number of coefficients, i.e. the filter
     *                order + 1).  `numtaps` must be odd if a passband includes the
     *                Nyquist frequency.
     * @param cutoff  Cutoff frequency of filter (expressed in the same units as `fs`)
     *                OR an array of cutoff frequencies (that is, band edges). In the
     *                latter case, the frequencies in `cutoff` should be positive and
     *                monotonically increasing between 0 and `fs/2`.  The values 0 and
     *                `fs/2` must not be included in `cutoff`.
     */
    public static List<Double> fir1(final int numtaps, @NonNull final List<Double> cutoff) {
        if (cutoff.size() != 2) {
            throw new IllegalArgumentException("cutoff size must be two");
        }
        if (cutoff.stream().min(Double::compareTo).get() <= 0 || cutoff.stream().max(Double::compareTo).get() >= 1) {
            throw new IllegalArgumentException("Invalid cutoff frequency: frequencies must be greater than 0 and less than fs/2.");
        }
        //Build up the coefficients.
        final Double alpha = 0.5 * (numtaps - 1);
        final List<Double> m = IntStream.range(0, numtaps).boxed().map(i -> i - alpha).collect(toList());
        final double left = cutoff.get(0);
        final double right = cutoff.get(1);
        final List<Double> h = m.stream()
                .map(i -> {
                    // h += right * sinc(right * m)
                    double tmp = right * sinc(i * right);
                    // h -= right * sinc(left * m)
                    return tmp - left * sinc(left * i);
                })
                .collect(toList());
        /**
         * Get and apply the window function.
         */
        // win = get_window(window, numtaps, fftbins=False)
        final List<Double> win = HannWindow.transform(numtaps);
        // h *= win
        final List<Double> result = new ArrayList<>();
        Assert.isTrue(h.size() == win.size());
        for (int i = 0; i < h.size(); i++) {
            result.add(h.get(i) * win.get(i));
        }
        log.info("\"h *= win\" --> " + result);
        // Now handle scaling if desired.
        final double scaleFrequency = getScaleFrequency(left, right);
        final List<Double> c = m.stream().map(i -> Math.cos(Math.PI * i * scaleFrequency)).collect(toList());
        Assert.isTrue(result.size() == c.size());
        // s = np.sum(h * c)
        final ArrayList<Double> sList = new ArrayList<>();
        for (int i = 0; i < h.size(); i++) {
            sList.add(result.get(i) * c.get(i));
        }
        final double s = sList.stream().mapToDouble(i -> i).sum();
//        h /= s
        return result.stream().map(i -> i / s).collect(toList());
    }

    private static double sinc(@NonNull final Double x) {
        final double y = Math.PI * (x == 0 ? 1.0e-20 : x);
        return sin(y) / y;
    }

    private static double getScaleFrequency(final double left, final double right) {
        if (left == 0) {
            return 0.0;
        } else if (right == 1) {
            return 1.0;
        } else {
            return 0.5 * (left + right);
        }
    }
}
