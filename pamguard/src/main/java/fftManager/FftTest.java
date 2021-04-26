package fftManager;

import Filters.*;
import PamUtils.PamUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FftTest {

    /**
     * @param args
     */
    public static void main(String[] args) {
        int nTrial = 10000;
        int fftLen = 1024;
        long startTime, endTime;
        FastFFT fastFFT = new FastFFT();
        FFT fft = new FFT();

        FilterParams filterParams = new FilterParams();
        filterParams.filterType = FilterType.BUTTERWORTH;
        filterParams.filterBand = FilterBand.LOWPASS;
        filterParams.filterOrder = 80;
        filterParams.lowPassFreq = 2000;
        filterParams.passBandRipple = 2;

        float sampleRate = 48000;

        Filter filter = new IirfFilter(0, sampleRate, filterParams);

        try {
            if (args.length > 0) {
                nTrial = Integer.valueOf(args[0]);
            }
            if (args.length > 1) {
                fftLen = Integer.valueOf(args[1]);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return;
        }

        int m = PamUtils.log2(fftLen);

        double[] realData = new double[fftLen];
        Complex[] complexData = Complex.allocateComplexArray(fftLen);
        Random r = new Random();
        for (int i = 0; i < fftLen; i++) {
            realData[i] = r.nextGaussian();
        }
        startTime = System.currentTimeMillis();
        for (int i = 0; i < nTrial; i++) {
            fastFFT.rfft(realData, complexData, m);
//			filter.runFilter(realData);
        }
        endTime = System.currentTimeMillis();
        long timeTaken = endTime - startTime;
        System.out.println(String.format("Time taken for %d %dpt FFT's = %d ms", nTrial, fftLen, timeTaken));

//		Complex[] c = Complex.allocateComplexArray(8);
//		double[] d = new double[8];
//		d[1] = d[0] = 1;
//		c[1].real = 1;
//		Complex[] c2 = fastFFT.rfft(d, null, 3);
//		Complex[] c2a = fft.recursiveFFT(c);
//		fastFFT.fft(c, 3);
//		for (int i = 0; i < 4; i++) {
//			c[i].assign(c2[i]);
//			c[7-i].assign(c2[i].conj());
//		}
//		Complex[] c3 = fft.recursiveIFFT(c2);
//        fastFFT.ifft(c, 3);
        String str = "-14.66493271,-20.38126362,-1.795706046,-15.44307199,-16.61028092,-0.329212775,-18.28627323,-21.81782846,0.209499039,1.047495193,0.748210852";
//        String str = "-14.66493271,-20.38126362,-1.795706046,-15.44307199,-16.61028092,-0.329212775,-18.28627323,-21.81782846";

        List<String> strings = Arrays.asList(str.split(","));
        Complex[] c =new Complex[strings.size()];
        for (int i = 0; i < strings.size(); i++) {
            c[i]=new Complex(Double.valueOf(strings.get(i)), 0);
        }
        fastFFT.fft(c);
        for (int i = 0; i < c.length; i++) {
            System.out.println(c[i].real+" "+c[i].imag);
        }
    }
}
