package namdv.sensorapp.modules;

import java.util.ArrayList;

import namdv.sensorapp.utils.data.SimpleAccelData;
import namdv.sensorapp.utils.data.WindowData;
import namdv.sensorapp.utils.features.FrequencyStatistic;
import namdv.sensorapp.utils.features.HjorthStatistics;
import namdv.sensorapp.utils.features.MeanStatistic;
import namdv.sensorapp.utils.features.RMSFeature;
import namdv.sensorapp.utils.features.RelativeFeatures;
import namdv.sensorapp.utils.features.SMAStatistic;
import namdv.sensorapp.utils.features.VarianceStatistic;

/**
 * Created by namdv on 9/22/17.
 */

public class TestModelHelper
{
    private static final int FREQUENCY = 50;

    public String calculateFunctions(ArrayList<SimpleAccelData> data) {
        //mean
        double meanX = MeanStatistic.shared.getMeanX(data);
        double meanY = MeanStatistic.shared.getMeanY(data);
        double meanZ = MeanStatistic.shared.getMeanZ(data);
        double meanXYZ = MeanStatistic.shared.getMean(data);

        //gravity
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            totalGravity += meanXYZ;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();

        //accels
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = RMSFeature.shared.getHorizontalAcceleration(acc);
            totalHorizontalAccels += horizontalAccel;
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();

        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = RMSFeature.shared.getVerticalAcceleration(acc);
            totalVerticalAccels += verticalAccel;
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();

        //rms
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = RMSFeature.shared.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();

        //variance
        double variance = VarianceStatistic.shared.getVariance(data);

        //hjorth
        double activity = HjorthStatistics.shared.getActivity(data);
        double mobility = HjorthStatistics.shared.getMobility(data);
        double complexity = HjorthStatistics.shared.getComplexity(data);

        //relative
        double relative = RelativeFeatures.shared.getRelativeFeature(data);

        //sma
        double SMA = SMAStatistic.sma.getSMA(data);
        double horizontalEnergy = SMAStatistic.sma.getHorizontalEnergy(data);
        double verticalEnergy = SMAStatistic.sma.getVerticalEnergy(data);
        double vectorSVM = SMAStatistic.sma.getVectorSVM(data);
        double dsvm = SMAStatistic.sma.getDSVM(data);
        double dsvmByRMS = SMAStatistic.sma.getDSVMByRMS(data);

        //frequency
        preDataProcessor(data);
        WindowData wData = new WindowData();
        wData.add(data);
        FrequencyStatistic frequency = new FrequencyStatistic(wData);

//        double fourier = frequency.getFourier(0,"x");
        double fourier = 0;
        double xfftEnergy = frequency.getXFFTEnergy(data);
        double yfftEnergy = frequency.getYFFTEnergy(data);
        double zfftEnergy = frequency.getZFFTEnergy(data);
        double meanfftEnergy = frequency.getMeanFFTEnergy(data);
        double xfftEntropy = frequency.getXFFTEntropy(data);
        double yfftEntropy = frequency.getYFFTEntropy(data);
        double zfftEntropy = frequency.getZFFTEntropy(data);
        double meanfftEntropy = frequency.getMeanFFTEntropy(data);
        double devX = frequency.getStandardDeviationX(0);
        double devY = frequency.getStandardDeviationY(0);
        double devZ = frequency.getStandardDeviationZ(0);

        return meanX + "," + meanY + "," + meanZ + "," + meanXYZ + "," +
                variance + "," +
                averageGravity + "," + averageHorizontalAccels + "," + averageVerticalAccels + "," +
                averageRMS + "," +
                relative + "," +
                SMA + "," +
                horizontalEnergy + "," + verticalEnergy + "," +
                vectorSVM + "," +
                dsvm + "," + dsvmByRMS + "," +
                activity + "," + mobility + "," + complexity + "," +
                fourier + "," +
                xfftEnergy + "," + yfftEnergy + "," + zfftEnergy + "," + meanfftEnergy + "," +
                xfftEntropy + "," + yfftEntropy + "," + zfftEntropy + "," + meanfftEntropy + "," +
                devX + "," + devY + "," + devZ + "," + "?";
    }

    public int sensorDelay() {
        return 1000 / FREQUENCY;
    }

    void preDataProcessor(ArrayList<SimpleAccelData> data) {
        int size = data.size();
        boolean sizeIsPowerOfTwo = (size & (size - 1)) == 0;

        if (!sizeIsPowerOfTwo) {
            int newSize = convertToNearestPowerOfTwo(size);
            addZeroValues(data, newSize);
        }
    }

    void addZeroValues(ArrayList<SimpleAccelData> data, int newSize) {
        int numberOfZeros = newSize - data.size();
        for (int i = 0; i < numberOfZeros; i++) {
            SimpleAccelData acc = new SimpleAccelData("0","0","0","0");
            data.add(acc);
        }
    }

    int convertToNearestPowerOfTwo(int x) {
        x = x - 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }
}
