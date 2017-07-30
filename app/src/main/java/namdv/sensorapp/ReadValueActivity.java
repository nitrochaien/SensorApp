package namdv.sensorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

import namdv.sensorapp.Utils.SensorFunctions;
import namdv.sensorapp.Utils.SimpleAccelData;
import namdv.sensorapp.Utils.WindowData;

/**
 * Created by namdv on 7/20/17.
 */

public class ReadValueActivity extends AppCompatActivity {
    Button btnCalFunc, btnCalFourier;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_value);

        initView();
        initData();
    }

    private void initData() {
        String accelValue = FileUtils.fileUtils.getAccelData(this);
        String[] lines = accelValue.split("\n");
        saveData(lines);
    }

    private void initView() {
        btnCalFunc = (Button) findViewById(R.id.btnCalFunc);
        btnCalFunc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calculateFunctions();
            }
        });

        btnCalFourier = (Button) findViewById(R.id.btnCalFourier);
        btnCalFourier.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calculateFourier();
            }
        });
    }

    private void calculateFunctions() {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(0);

        //function 7
        double xRMS = SensorFunctions.mean.getMeanX(data);
        System.out.print("xRMS: " + xRMS + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xRMS);

        //function 8
//        ArrayList<Double> gravity = new ArrayList<>();
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            double value = SensorFunctions.mean.getSquareRootXYZ(acc);
            System.out.print("gravity value: " + value + "\n");
            totalGravity += value;
//            gravity.add(value);
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageGravity);

        //function 9 & 14
        double mean = SensorFunctions.mean.getMean(data);
        System.out.print("mean: " + mean + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + mean);

        //function 10
        double windowGravity = SensorFunctions.rms.getWindowsAvarageGravity(0);
        System.out.print("window gravity: " + windowGravity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + windowGravity);

        //function 11
//        ArrayList<Double> horizontalAccels = new ArrayList<>();
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = SensorFunctions.rms.getHorizontalAcceleration(acc);
            System.out.print("HorizontalAccel: " + horizontalAccel + "\n");
            totalHorizontalAccels += horizontalAccel;
//            horizontalAccels.add(horizontalAccel);
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageHorizontalAccels);

        //function 12
//        ArrayList<Double> VerticalAccels = new ArrayList<>();
        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = SensorFunctions.rms.getVerticalAcceleration(acc);
            System.out.print("verticalAccel: " + verticalAccel + "\n");
            totalVerticalAccels += verticalAccel;
//            VerticalAccels.add(verticalAccel);
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageVerticalAccels);

        //function 13
//        ArrayList<Double> RMSs = new ArrayList<>();
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double rms = SensorFunctions.rms.getRMS(data, i);
            System.out.print("RMS: " + rms + "\n");
            totalRMS += rms;
//            RMSs.add(rms);
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();
        FileUtils.fileUtils.writeToCalculatedDataFile("" + averageRMS);

        //function 15
        double variance = SensorFunctions.variance.getVariance(data);
        System.out.print("variance: " + variance + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + variance);

        //function 21
        double relaFeature = SensorFunctions.relative.getRelativeFeature(data);
        System.out.print("relaFeature: " + relaFeature + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + relaFeature);

        //function 28
        double sma = SensorFunctions.sma.getSMA(data);
        System.out.print("sma: " + sma + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + sma);

        //function 29
        double horizontalEnergy = SensorFunctions.sma.getHorizontalEnergy(data);
        System.out.print("horizontal Energy: " + horizontalEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + horizontalEnergy);

        //function 30
        double vectorSVM = SensorFunctions.sma.getVectorSVM(data);
        System.out.print("vector svm: " + vectorSVM + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + vectorSVM);

        //function 32
        double dsvm = SensorFunctions.sma.getDSVMByRMS(data);
        System.out.print("dsvm: " + dsvm + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + dsvm);

        //function 34, 35, 36
        double activity = SensorFunctions.hjorth.getActivity(0);
        double mobility = SensorFunctions.hjorth.getMobility(0);
        double complexity = SensorFunctions.hjorth.getComplexity(0);
        System.out.print("activity: " + activity + ", " + "mobility: " + mobility + ", " + "complexity: " + complexity + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + activity);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + mobility);
        FileUtils.fileUtils.writeToCalculatedDataFile("" + complexity);
    }

    void calculateFourier() {
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(0);

        //function 37
        preDataProcessor(data);
        double fourier = SensorFunctions.frequency.getFourier(0,"x");
        System.out.print("fourier: " + fourier + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + fourier);

        //function 39, 40
        double xfftEnergy = SensorFunctions.frequency.getXFFTEnergy(0);
        System.out.print("xfftEnergy: " + xfftEnergy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEnergy);

        //function 41
        double xfftEntropy = SensorFunctions.frequency.getXFFTEntropy(0);
        System.out.print("xfftEntropy: " + xfftEntropy + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + xfftEntropy);

        //function 42
        double standardDeviation = SensorFunctions.frequency.getStandardDeviationX(0);
        System.out.print("standardDeviation: " + standardDeviation + "\n");
        FileUtils.fileUtils.writeToCalculatedDataFile("" + standardDeviation);
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

    private void saveData(String[] input) {
        ArrayList<SimpleAccelData> data = new ArrayList<>();
        for (String line : input) {
            String[] element = line.split(";");
            String timeStamp = element[0];
            String x = element[1];
            String y = element[2];
            String z = element[3];
            SimpleAccelData acc = new SimpleAccelData(timeStamp, x, y, z);
            data.add(acc);
        }
        WindowData.window.add(data);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
