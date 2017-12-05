package namdv.sensorapp.utils.features;

import java.util.ArrayList;

import namdv.sensorapp.utils.ListUtils;
import namdv.sensorapp.utils.data.SimpleAccelData;
import namdv.sensorapp.utils.data.WindowData;
import namdv.sensorapp.utils.file.FileUtils;

/**
 * Created by namdv on 10/2/17.
 */

public class BikeFunctions
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

    public BikeFunctions(String vehicle, String status) {
        this.vehicle = vehicle;
        this.status = status;
    }

    public void saveMean(ArrayList<SimpleAccelData> data) {
        double meanX = mean.getMeanX(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanX);

        double meanY = mean.getMeanY(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanY);

        double meanZ = mean.getMeanZ(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanZ);

        double meanXYZ = mean.getMean(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanXYZ);
    }

    public void saveFourier(WindowData wData, int index) {
        ArrayList<SimpleAccelData> data = wData.getAt(index);
        FrequencyStatistic frequency = new FrequencyStatistic(wData);

        //function 37
        data = ListUtils.shared.preDataProcessor(data);
        double fourier = frequency.getFourier(index,"x");
        FileUtils.fileUtils.writeToBikeFile("" + fourier);

        //function 39, 40
        double xfftEnergy = frequency.getXFFTEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + xfftEnergy);

        double yfftEnergy = frequency.getYFFTEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + yfftEnergy);

        double zfftEnergy = frequency.getZFFTEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + zfftEnergy);

        double meanfftEnergy = frequency.getMeanFFTEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanfftEnergy);

        //function 41
        double xfftEntropy = frequency.getXFFTEntropy(data);
        FileUtils.fileUtils.writeToBikeFile("" + xfftEntropy);

        double yfftEntropy = frequency.getYFFTEntropy(data);
        FileUtils.fileUtils.writeToBikeFile("" + yfftEntropy);

        double zfftEntropy = frequency.getZFFTEntropy(data);
        FileUtils.fileUtils.writeToBikeFile("" + zfftEntropy);

        double meanfftEntropy = frequency.getMeanFFTEntropy(data);
        FileUtils.fileUtils.writeToBikeFile("" + meanfftEntropy);

        //function 42
        double devX = frequency.getStandardDeviationX(index);
        FileUtils.fileUtils.writeToBikeFile("" + devX);

        double devY = frequency.getStandardDeviationY(index);
        FileUtils.fileUtils.writeToBikeFile("" + devY);

        double devZ = frequency.getStandardDeviationZ(index);
        FileUtils.fileUtils.writeToBikeFile("" + devZ);
    }

    public void saveGravity(ArrayList<SimpleAccelData> data) {
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            double value = mean.getSquareRootXYZ(acc);
            totalGravity += value;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();
        FileUtils.fileUtils.writeToBikeFile("" + averageGravity);
    }

    public void saveAccels(ArrayList<SimpleAccelData> data) {
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = rms.getHorizontalAcceleration(acc);
            totalHorizontalAccels += horizontalAccel;
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();
        FileUtils.fileUtils.writeToBikeFile("" + averageHorizontalAccels);

        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = rms.getVerticalAcceleration(acc);
            totalVerticalAccels += verticalAccel;
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();
        FileUtils.fileUtils.writeToBikeFile("" + averageVerticalAccels);
    }

    public void saveRMS(ArrayList<SimpleAccelData> data) {
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = rms.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();
        FileUtils.fileUtils.writeToBikeFile("" + averageRMS);
    }

    public void saveVariance(ArrayList<SimpleAccelData> data) {
        double var = variance.getVariance(data);
        FileUtils.fileUtils.writeToBikeFile("" + var);
    }

    public void saveHjorthFeatures(ArrayList<SimpleAccelData> data) {
        double activity = hjorth.getActivity(data);
        double mobility = hjorth.getMobility(data);
        double complexity = hjorth.getComplexity(data);
        
        FileUtils.fileUtils.writeToBikeFile("" + activity);
        FileUtils.fileUtils.writeToBikeFile("" + mobility);
        FileUtils.fileUtils.writeToBikeFile("" + complexity);
    }

    public void saveRelativeFeature(ArrayList<SimpleAccelData> data) {
        double relaFeature = relative.getRelativeFeature(data);
        FileUtils.fileUtils.writeToBikeFile("" + relaFeature);
    }

    public void saveSMA(ArrayList<SimpleAccelData> data) {
        double SMA = sma.getSMA(data);
        FileUtils.fileUtils.writeToBikeFile("" + SMA);

        double horizontalEnergy = sma.getHorizontalEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + horizontalEnergy);

        double verticalEnergy = sma.getVerticalEnergy(data);
        FileUtils.fileUtils.writeToBikeFile("" + verticalEnergy);

        double vectorSVM = sma.getVectorSVM(data);
        FileUtils.fileUtils.writeToBikeFile("" + vectorSVM);

        double dsvm = sma.getDSVM(data);
        FileUtils.fileUtils.writeToBikeFile("" + dsvm);

        double dsvmByRMS = sma.getDSVMByRMS(data);
        FileUtils.fileUtils.writeToBikeFile("" + dsvmByRMS);
    }

    public void addAttributeActivity() {
        FileUtils.fileUtils.writeLastDataBikeFile("" + vehicle + "," + status);
    }
}
