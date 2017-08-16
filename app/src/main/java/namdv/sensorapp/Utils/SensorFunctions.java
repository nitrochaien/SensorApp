package namdv.sensorapp.Utils;

import java.util.ArrayList;

import namdv.sensorapp.FileUtils;

/**
 * Created by namdv on 7/15/17.
 */

public class SensorFunctions {
    public static SensorFunctions shared = new SensorFunctions();

    FrequencyStatistic frequency = new FrequencyStatistic();
    HjorthStatistics hjorth = new HjorthStatistics();
    MeanStatistic mean = new MeanStatistic();
    NumericalIntegration numerical = new NumericalIntegration();
    RMSFeature rms = new RMSFeature();
    SMAStatistic sma = new SMAStatistic();
    VarianceStatistic variance = new VarianceStatistic();
    RelativeFeatures relative = new RelativeFeatures();

    public void saveMean(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);

        double meanX = mean.getMeanX(data);
        System.out.print("meanX: " + meanX + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanX);

        double meanY = mean.getMeanY(data);
        System.out.print("meanY: " + meanY + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanY);

        double meanZ = mean.getMeanZ(data);
        System.out.print("meanZ: " + meanZ + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanZ);

        double meanXYZ = mean.getMean(data);
        System.out.print("meanXYZ: " + meanXYZ + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanXYZ);
    }

    public void saveFourier() {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(0);

        //function 37
        preDataProcessor(data);
        double fourier = frequency.getFourier(0,"x");
        System.out.print("fourier: " + fourier + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + fourier);

        //function 39, 40
        double xfftEnergy = frequency.getXFFTEnergy(data);
        System.out.print("xfftEnergy: " + xfftEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEnergy);

        double yfftEnergy = frequency.getYFFTEnergy(data);
        System.out.print("yfftEnergy: " + yfftEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + yfftEnergy);

        double zfftEnergy = frequency.getZFFTEnergy(data);
        System.out.print("zfftEnergy: " + zfftEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + zfftEnergy);

        //function 41
        double xfftEntropy = frequency.getXFFTEntropy(data);
        System.out.print("xfftEntropy: " + xfftEntropy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEntropy);

        double yfftEntropy = frequency.getYFFTEntropy(data);
        System.out.print("yfftEntropy: " + yfftEntropy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + yfftEntropy);

        double zfftEntropy = frequency.getZFFTEntropy(data);
        System.out.print("zfftEntropy: " + zfftEntropy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + zfftEntropy);

        //function 42
        double devX = frequency.getStandardDeviationX(0);
        System.out.print("devX: " + devX + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devX);

        double devY = frequency.getStandardDeviationY(0);
        System.out.print("devY: " + devY + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devY);

        double devZ = frequency.getStandardDeviationZ(0);
        System.out.print("devZ: " + devZ + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devZ);
    }

    public void saveGravity(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            double value = mean.getSquareRootXYZ(acc);
            totalGravity += value;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();
        System.out.print("gravity value: " + averageGravity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageGravity);
    }

    public void saveAccels(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = rms.getHorizontalAcceleration(acc);
            totalHorizontalAccels += horizontalAccel;
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();
        System.out.print("HorizontalAccel: " + averageHorizontalAccels + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageHorizontalAccels);

        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = rms.getVerticalAcceleration(acc);
            totalVerticalAccels += verticalAccel;
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();
        System.out.print("verticalAccel: " + averageVerticalAccels + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageVerticalAccels);
    }

    public void saveRMS(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = rms.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();
        System.out.print("RMS: " + averageRMS + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageRMS);
    }

    public void saveVariance(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double var = variance.getVariance(data);
        System.out.print("variance: " + var + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + var);
    }

    void preDataProcessor(ArrayList<SimpleAccelData> data) {
        int size = data.size();
        boolean sizeIsPowerOfTwo = (size & (size - 1)) == 0;

        if (!sizeIsPowerOfTwo) {
            int newSize = convertToNearestPowerOfTwo(size);
            addZeroValues(data, newSize);
        }
    }

    public void saveHjorthFeatures(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double activity = hjorth.getActivity(data);
        double mobility = hjorth.getMobility(data);
        double complexity = hjorth.getComplexity(data);
        System.out.print("activity: " + activity + ", " + "mobility: " + mobility + ", " + "complexity: " + complexity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + activity);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + mobility);
        FileUtils.fileUtils.writeLastData("" + complexity);
    }

    public void saveRelativeFeature(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);
        double relaFeature = relative.getRelativeFeature(data);
        System.out.print("relaFeature: " + relaFeature + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + relaFeature);
    }

    public void saveSMA(int index) {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(index);

        double SMA = sma.getSMA(data);
        System.out.print("sma: " + SMA + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + SMA);

        double horizontalEnergy = sma.getHorizontalEnergy(data);
        System.out.print("horizontal Energy: " + horizontalEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + horizontalEnergy);

        double vectorSVM = sma.getVectorSVM(data);
        System.out.print("vector svm: " + vectorSVM + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + vectorSVM);

        double dsvm = sma.getDSVMByRMS(data);
        System.out.print("dsvm: " + dsvm + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvm);
    }

    void addZeroValues(ArrayList<SimpleAccelData> data, int newSize) {
        int numberOfZeros = newSize - data.size();
        System.out.print("former data size: " + data.size() + "\n");
        System.out.print("new size: " + newSize + "\n");

        for (int i = 0; i < numberOfZeros; i++) {
            SimpleAccelData acc = new SimpleAccelData("0","0","0","0");
            data.add(acc);
        }

        System.out.print("new data size: " + data.size() + "\n");
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
