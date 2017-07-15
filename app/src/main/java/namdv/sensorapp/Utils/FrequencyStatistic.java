package namdv.sensorapp.Utils;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import java.util.ArrayList;

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

    private Complex[] getFFT(int windowIndex, String type) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(windowIndex);
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
    public double getXFFTEnergy(int windowIndex) {
        Complex[] xFFT = getFFT(windowIndex, X);
        int length = xFFT.length;
        if (length == 0) return -1;

        double sumXFFEnergy = 0;
        for (int i = 0; i < length; i++){
            sumXFFEnergy += xFFT[i].getReal()*xFFT[i].getReal();
        }
        return sumXFFEnergy/length;
    }

    public double getYFFTEnergy(int windowIndex) {
        Complex[] yFFT = getFFT(windowIndex, Y);
        int length = yFFT.length;
        if (length == 0) return -1;

        double sumYFFEnergy = 0;
        for (int i = 0; i < length; i++){
            sumYFFEnergy += yFFT[i].getReal()*yFFT[i].getReal();
        }
        return sumYFFEnergy/length;
    }

    public double getZFFTEnergy(int windowIndex) {
        Complex[] zFFT = getFFT(windowIndex, Z);
        int length = zFFT.length;
        if (length == 0) return -1;

        double sumZFFEnergy = 0;
        for (int i = 0; i < length; i++){
            sumZFFEnergy += zFFT[i].getReal()*zFFT[i].getReal();
        }
        return sumZFFEnergy/length;
    }

    public double getMeanFFTEnergy(int windowIndex) {
        Complex[] meanFFT = getFFT(windowIndex, MEAN);
        int length = meanFFT.length;
        if (length == 0) return -1;

        double sumMeanFFEnergy = 0;
        for (int i = 0; i < length; i++) {
            sumMeanFFEnergy += meanFFT[i].getReal()*meanFFT[i].getReal();
        }
        return sumMeanFFEnergy / length;
    }

    //DOCME: function (41)
    public double getXFFTEntropy(int windowIndex){
        Complex[] xFFT = getFFT(windowIndex,X);
        int length = xFFT.length;
        if (length == 0) return -1;
        double sumXFFTComponents = 0;
        for (int i = 0; i < length; i++){
            sumXFFTComponents += xFFT[i].getReal();
        }

        double sumXFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            pi = xFFT[i].getReal() / sumXFFTComponents;
            sumXFFTEntropy += pi*Math.log(pi);
        }
        return -(sumXFFTEntropy/length);
    }

    public double getYFFTEntropy(int windowIndex){
        Complex[] yFFT = getFFT(windowIndex,Y);
        int length = yFFT.length;
        if (length == 0) return -1;
        double sumYFFTComponents = 0;
        for (int i = 0; i < length; i++){
            sumYFFTComponents += yFFT[i].getReal();
        }

        double sumYFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            pi = yFFT[i].getReal() / sumYFFTComponents;
            sumYFFTEntropy += pi*Math.log(pi);
        }
        return -(sumYFFTEntropy/length);
    }

    public double getZFFTEntropy(int windowIndex){
        Complex[] zFFT = getFFT(windowIndex,Z);
        int length = zFFT.length;
        if (length == 0) return -1;
        double sumZFFTComponents = 0;
        for (int i = 0; i < length; i++){
            sumZFFTComponents += zFFT[i].getReal();
        }

        double sumZFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            pi = zFFT[i].getReal() / sumZFFTComponents;
            sumZFFTEntropy += pi*Math.log(pi);
        }
        return -(sumZFFTEntropy/length);
    }

    public double getMeanFFTEntropy(int windowIndex){
        Complex[] meanFFT = getFFT(windowIndex,MEAN);
        int length = meanFFT.length;
        if (length == 0) return -1;
        double sumMeanFFTComponents = 0;
        for (int i = 0; i < length; i++){
            sumMeanFFTComponents += meanFFT[i].getReal();
        }

        double sumMeanFFTEntropy = 0;
        double pi;
        for (int i = 0; i < length; i++){
            pi = meanFFT[i].getReal() / sumMeanFFTComponents;
            sumMeanFFTEntropy += pi*Math.log(pi);
        }
        return -(sumMeanFFTEntropy/length);
    }

    public double getFourier(int windowIndex, String type) {
        //DOCME: function (37)
        Complex[] fft = getFFT(windowIndex, type);
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
