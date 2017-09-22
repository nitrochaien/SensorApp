package namdv.sensorapp.Utils.features;

import java.util.ArrayList;

import namdv.sensorapp.Utils.file.FileUtils;
import namdv.sensorapp.Utils.data.SimpleAccelData;
import namdv.sensorapp.Utils.data.WindowData;

/**
 * Created by namdv on 7/15/17.
 */

public class SensorFunctions
{
    HjorthStatistics hjorth = new HjorthStatistics();
    MeanStatistic mean = new MeanStatistic();
    NumericalIntegration numerical = new NumericalIntegration();
    RMSFeature rms = new RMSFeature();
    SMAStatistic sma = new SMAStatistic();
    VarianceStatistic variance = new VarianceStatistic();
    RelativeFeatures relative = new RelativeFeatures();

    String vehicle;
    String status;

    public SensorFunctions(String vehicle, String status) {
        this.vehicle = vehicle;
        this.status = status;
    }

    public void saveMean(ArrayList<SimpleAccelData> data) {
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

    public void saveFourier(WindowData wData, int index) {
        ArrayList<SimpleAccelData> data = wData.getAt(index);
        FrequencyStatistic frequency = new FrequencyStatistic(wData);

        //function 37
        preDataProcessor(data);
        double fourier = frequency.getFourier(index,"x");
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

        double meanfftEnergy = frequency.getMeanFFTEnergy(data);
        System.out.print("meanfftEnergy: " + meanfftEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanfftEnergy);

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

        double meanfftEntropy = frequency.getMeanFFTEntropy(data);
        System.out.print("meanfftEntropy: " + meanfftEntropy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanfftEntropy);

        //function 42
        double devX = frequency.getStandardDeviationX(index);
        System.out.print("devX: " + devX + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devX);

        double devY = frequency.getStandardDeviationY(index);
        System.out.print("devY: " + devY + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devY);

        double devZ = frequency.getStandardDeviationZ(index);
        System.out.print("devZ: " + devZ + "\n");
        FileUtils.fileUtils.writeLastData("" + devZ + "," + vehicle);
    }

    public void saveGravity(ArrayList<SimpleAccelData> data) {
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            double value = mean.getSquareRootXYZ(acc);
            totalGravity += value;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();
        System.out.print("gravity value: " + averageGravity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageGravity);
    }

    public void saveAccels(ArrayList<SimpleAccelData> data) {
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

    public void saveRMS(ArrayList<SimpleAccelData> data) {
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = rms.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();
        System.out.print("RMS: " + averageRMS + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageRMS);
    }

    public void saveVariance(ArrayList<SimpleAccelData> data) {
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

    public void saveHjorthFeatures(ArrayList<SimpleAccelData> data) {
        double activity = hjorth.getActivity(data);
        double mobility = hjorth.getMobility(data);
        double complexity = hjorth.getComplexity(data);
        System.out.print("activity: " + activity + ", " + "mobility: " + mobility + ", " + "complexity: " + complexity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + activity);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + mobility);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + complexity);
    }

    public void saveRelativeFeature(ArrayList<SimpleAccelData> data) {
        double relaFeature = relative.getRelativeFeature(data);
        System.out.print("relaFeature: " + relaFeature + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + relaFeature);
    }

    public void saveSMA(ArrayList<SimpleAccelData> data) {
        double SMA = sma.getSMA(data);
        System.out.print("sma: " + SMA + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + SMA);

        double horizontalEnergy = sma.getHorizontalEnergy(data);
        System.out.print("horizontal Energy: " + horizontalEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + horizontalEnergy);

        double verticalEnergy = sma.getVerticalEnergy(data);
        System.out.print("vertical Energy: " + verticalEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + verticalEnergy);

        double vectorSVM = sma.getVectorSVM(data);
        System.out.print("vector svm: " + vectorSVM + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + vectorSVM);

        double dsvm = sma.getDSVM(data);
        System.out.print("dsvm: " + dsvm + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvm);

        double dsvmByRMS = sma.getDSVMByRMS(data);
        System.out.print("dsvmByRMS: " + dsvmByRMS + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvmByRMS);
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
