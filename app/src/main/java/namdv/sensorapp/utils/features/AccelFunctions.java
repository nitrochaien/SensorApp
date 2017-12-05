package namdv.sensorapp.utils.features;

import java.util.ArrayList;

import namdv.sensorapp.utils.ListUtils;
import namdv.sensorapp.utils.file.FileUtils;
import namdv.sensorapp.utils.data.SimpleAccelData;
import namdv.sensorapp.utils.data.WindowData;

/**
 * Created by namdv on 7/15/17.
 */

public class AccelFunctions
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

    public AccelFunctions(String vehicle, String status) {
        this.vehicle = vehicle;
        this.status = status;
    }

    public void saveMean(ArrayList<SimpleAccelData> data) {
        double meanX = mean.getMeanX(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanX);

        double meanY = mean.getMeanY(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanY);

        double meanZ = mean.getMeanZ(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanZ);

        double meanXYZ = mean.getMean(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanXYZ);
    }

    public void saveFourier(WindowData wData, int index) {
        ArrayList<SimpleAccelData> data = wData.getAt(index);
        FrequencyStatistic frequency = new FrequencyStatistic(wData);

        //function 37
        data = ListUtils.shared.preDataProcessor(data);
        double fourier = frequency.getFourier(index,"x");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + fourier);

        //function 39, 40
        double xfftEnergy = frequency.getXFFTEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEnergy);

        double yfftEnergy = frequency.getYFFTEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + yfftEnergy);

        double zfftEnergy = frequency.getZFFTEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + zfftEnergy);

        double meanfftEnergy = frequency.getMeanFFTEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanfftEnergy);

        //function 41
        double xfftEntropy = frequency.getXFFTEntropy(data);
               FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEntropy);

        double yfftEntropy = frequency.getYFFTEntropy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + yfftEntropy);

        double zfftEntropy = frequency.getZFFTEntropy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + zfftEntropy);

        double meanfftEntropy = frequency.getMeanFFTEntropy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + meanfftEntropy);

        //function 42
        double devX = frequency.getStandardDeviationX(index);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devX);

        double devY = frequency.getStandardDeviationY(index);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devY);

        double devZ = frequency.getStandardDeviationZ(index);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + devZ);
    }

    public void saveGravity(ArrayList<SimpleAccelData> data) {
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            double value = mean.getSquareRootXYZ(acc);
            totalGravity += value;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageGravity);
    }

    public void saveAccels(ArrayList<SimpleAccelData> data) {
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = rms.getHorizontalAcceleration(acc);
            totalHorizontalAccels += horizontalAccel;
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageHorizontalAccels);

        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = rms.getVerticalAcceleration(acc);
            totalVerticalAccels += verticalAccel;
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageVerticalAccels);
    }

    public void saveRMS(ArrayList<SimpleAccelData> data) {
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = rms.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageRMS);
    }

    public void saveVariance(ArrayList<SimpleAccelData> data) {
        double var = variance.getVariance(data);
               FileUtils.fileUtils.writeToCalculatedDataFile("" + var);
    }

    public void saveHjorthFeatures(ArrayList<SimpleAccelData> data) {
        double activity = hjorth.getActivity(data);
        double mobility = hjorth.getMobility(data);
        double complexity = hjorth.getComplexity(data);
               FileUtils.fileUtils.writeToCalculatedDataFile("" + activity);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + mobility);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + complexity);
    }

    public void saveRelativeFeature(ArrayList<SimpleAccelData> data) {
        double relaFeature = relative.getRelativeFeature(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + relaFeature);
    }

    public void saveSMA(ArrayList<SimpleAccelData> data) {
        double SMA = sma.getSMA(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + SMA);

        double horizontalEnergy = sma.getHorizontalEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + horizontalEnergy);

        double verticalEnergy = sma.getVerticalEnergy(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + verticalEnergy);

        double vectorSVM = sma.getVectorSVM(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + vectorSVM);

        double dsvm = sma.getDSVM(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvm);

        double dsvmByRMS = sma.getDSVMByRMS(data);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvmByRMS);
    }

    public void addAttributeVehicle() {
        FileUtils.fileUtils.writeLastData("" + vehicle);
    }
}
