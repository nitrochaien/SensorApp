package namdv.sensorapp.Utils.features;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;

import namdv.sensorapp.Utils.data.SimpleAccelData;
import namdv.sensorapp.Utils.data.WindowData;

/**
 * Created by namdv on 7/7/17.
 */

public class FrequencyStatistic {
    public static FrequencyStatistic frequency = new FrequencyStatistic();

    private static final String X = "x";
    private static final String Y = "y";
    private static final String Z = "z";
    private static final String MEAN = "mean";

    private FastFourierTransformer fastFT;

    public FrequencyStatistic() {
        fastFT = new FastFourierTransformer(DftNormalization.STANDARD);
    }

    private Complex[] getFFT(ArrayList<SimpleAccelData> data, String type) {
        if (data == null || data.size() == 0) return new Complex[0];
        int length = data.size();
        if (type.equals(X)) {
            Complex[] xFFT = new Complex[length];
            for(int i = 0; i < length; i++){
                xFFT[i] = new Complex(data.get(i).getX());
            }
            xFFT = fastFT.transform(xFFT,TransformType.FORWARD);
            return xFFT;
        }
        else if (type.equals(Y)) {
            Complex[] yFFT = new Complex[length];
            for(int i = 0; i < length; i++){
                yFFT[i] = new Complex(data.get(i).getY());
            }
            yFFT = fastFT.transform(yFFT,TransformType.FORWARD);
            return yFFT;
        }
        else if (type.equals(Z)) {
            Complex[] zFFT = new Complex[length];
            for (int i = 0; i < length; i++)
            {
                zFFT[i] = new Complex(data.get(i).getZ());
            }
            zFFT = fastFT.transform(zFFT, TransformType.FORWARD);
            return zFFT;
        } else {
            Complex[] meanFFT = new Complex[length];
            for (int i = 0; i < length; i++) {
                meanFFT[i] = new Complex(MeanStatistic.shared.getMean(data));
            }
            meanFFT = fastFT.transform(meanFFT, TransformType.FORWARD);
            return meanFFT;
        }
    }

    //DOCME: function (39), (40)
    public double getXFFTEnergy(ArrayList<SimpleAccelData> data) {
        Complex[] xFFT = getFFT(data, X);
        int length = xFFT.length;
        if (length == 0) return -1;

        double sumXFFEnergy = 0;
        for (int i = 0; i < length; i++)
        {
            double real = xFFT[i].getReal();
            if (real < 0) real = 0;
            sumXFFEnergy += real*real;
        }
        return sumXFFEnergy/length;
    }

    public double getYFFTEnergy(ArrayList<SimpleAccelData> data) {
        Complex[] yFFT = getFFT(data, Y);
        int length = yFFT.length;
        if (length == 0) return -1;

        double sumYFFEnergy = 0;
        for (int i = 0; i < length; i++){
            double real = yFFT[i].getReal();
            if (real < 0) real = 0;
            sumYFFEnergy += real*real;
        }
        return sumYFFEnergy/length;
    }

    public double getZFFTEnergy(ArrayList<SimpleAccelData> data) {
        Complex[] zFFT = getFFT(data, Z);
        int length = zFFT.length;
        if (length == 0) return -1;

        double sumZFFEnergy = 0;
        for (int i = 0; i < length; i++){
            double real = zFFT[i].getReal();
            if (real < 0) real = 0;
            sumZFFEnergy += real*real;
        }
        return sumZFFEnergy/length;
    }

    public double getMeanFFTEnergy(ArrayList<SimpleAccelData> data) {
        Complex[] meanFFT = getFFT(data, MEAN);
        int length = meanFFT.length;
        if (length == 0) return -1;

        double sumMeanFFEnergy = 0;
        for (int i = 0; i < length; i++) {
            double real = meanFFT[i].getReal();
            if (real < 0) real = 0;
            sumMeanFFEnergy += real*real;
        }
        return sumMeanFFEnergy / length;
    }

    //DOCME: function (41)
    public double getXFFTEntropy(ArrayList<SimpleAccelData> data){
        Complex[] xFFT = getFFT(data,X);
        int length = xFFT.length;
        if (length == 0) return -1;
        double sumXFFTComponents = 0;
        for (int i = 0; i < length; i++){
            double real = xFFT[i].getReal();
            if (real < 0) real = 0;
            sumXFFTComponents += real;
        }

        if (sumXFFTComponents == 0) return -1;
        double sumXFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            double real = xFFT[i].getReal();
            if (real < 0) real = 0;
            pi = real / sumXFFTComponents;
            if (pi <= 0) pi = 1;
            sumXFFTEntropy += pi*Math.log(pi);
        }
        System.out.println("sumXFFTentropy: " + sumXFFTEntropy);
        return -(sumXFFTEntropy/length);
    }

    public double getYFFTEntropy(ArrayList<SimpleAccelData> data){
        Complex[] yFFT = getFFT(data,Y);
        int length = yFFT.length;
        if (length == 0) return -1;
        double sumYFFTComponents = 0;
        for (int i = 0; i < length; i++){
            double real = yFFT[i].getReal();
            if (real < 0) real = 0;
            sumYFFTComponents += real;
        }

        if (sumYFFTComponents == 0) return -1;
        double sumYFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            double real = yFFT[i].getReal();
            if (real < 0) real = 0;
            pi = real / sumYFFTComponents;
            if (pi <= 0) pi = 1;
            sumYFFTEntropy += pi*Math.log(pi);
        }
        return -(sumYFFTEntropy/length);
    }

    public double getZFFTEntropy(ArrayList<SimpleAccelData> data){
        Complex[] zFFT = getFFT(data, Z);
        int length = zFFT.length;
        if (length == 0) return -1;
        double sumZFFTComponents = 0;
        for (int i = 0; i < length; i++){
            double real = zFFT[i].getReal();
            if (real < 0) real = 0;
            sumZFFTComponents += real;
        }

        if (sumZFFTComponents == 0) return -1;
        double sumZFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            double real = zFFT[i].getReal();
            if (real < 0) real = 0;
            pi = real / sumZFFTComponents;
            if (pi <= 0) pi = 1;
            sumZFFTEntropy += pi*Math.log(pi);
        }
        return -(sumZFFTEntropy/length);
    }

    public double getMeanFFTEntropy(ArrayList<SimpleAccelData> data){
        Complex[] meanFFT = getFFT(data, MEAN);
        int length = meanFFT.length;
        if (length == 0) return -1;
        double sumMeanFFTComponents = 0;
        for (int i = 0; i < length; i++){
            sumMeanFFTComponents += meanFFT[i].getReal();
        }

        if (sumMeanFFTComponents == 0) return -1;
        double sumMeanFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            double real = meanFFT[i].getReal();
            if (real < 0) real = 0;
            pi = real / sumMeanFFTComponents;
            if (pi <= 0) pi = 1;
            sumMeanFFTEntropy += pi*Math.log(pi);
        }
        return -(sumMeanFFTEntropy/length);
    }

    public double getFourier(int windowIndex, String type) {
        //DOCME: function (37)
        //DOCME: type = x | y | z | mean
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(windowIndex);
        Complex[] fft = getFFT(data, type);
        double defaultFourier = getDefaultFourierValue(fft);
        return defaultFourier * WindowData.window.getHammingWindow(windowIndex);
    }

    private double getDefaultFourierValue(Complex[] input) {
        double result = 0;
        for (Complex n : input) {
            result += n.getReal();
        }
        return result;
    }

    //DOCME: function (42)
    public double getStandardDeviationX(int windowIndex) {
        double[] values = WindowData.window.getXValueInWidow(windowIndex);
        if (values.length == 0) return -1;

        DescriptiveStatistics statistic = new DescriptiveStatistics(values);
        return statistic.getStandardDeviation();
    }

    public double getStandardDeviationY(int windowIndex) {
        double[] values = WindowData.window.getYValueInWidow(windowIndex);
        if (values.length == 0) return -1;

        DescriptiveStatistics statistic = new DescriptiveStatistics(values);
        return statistic.getStandardDeviation();
    }

    public double getStandardDeviationZ(int windowIndex) {
        double[] values = WindowData.window.getZValueInWidow(windowIndex);
        if (values.length == 0) return -1;

        DescriptiveStatistics statistic = new DescriptiveStatistics(values);
        return statistic.getStandardDeviation();
    }
}
