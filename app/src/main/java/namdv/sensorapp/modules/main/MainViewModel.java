package namdv.sensorapp.modules.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import namdv.sensorapp.Utils.data.AccelData;
import namdv.sensorapp.Utils.data.SimpleAccelData;
import namdv.sensorapp.Utils.data.WindowData;
import namdv.sensorapp.Utils.features.SensorFunctions;
import namdv.sensorapp.Utils.file.FileUtils;
import namdv.sensorapp.Utils.file.WekaUtils;

/**
 * Created by namdv on 9/11/17.
 */

public class MainViewModel {
    private static final int FREQUENCY = 50;
    private ArrayList<AccelData> rawData = new ArrayList<>();

    public void writeAcceleromterDataToFile(SensorEvent event) {
        if (isAccelerometer(event.sensor)) {
            Date date = new Date();
            Long diffTime = date.getTime();
            float fTime = diffTime.floatValue();

            String timeStamp = String.valueOf(fTime);
            String x = String.valueOf(event.values[0]);
            String y = String.valueOf(event.values[1]);
            String z = String.valueOf(event.values[2]);

            String writeData = timeStamp + ";" +
                    x + ";" +
                    y + ";" +
                    z + ";\n";
            System.out.println("Write data:" + writeData);
            FileUtils.fileUtils.writeToRawFile(writeData);
        }
    }

    public void saveData() {
        String data = FileUtils.fileUtils.getRawData();
        String[] split = data.split("\n");
        saveRaw(split);
    }

    private void saveRaw(String[] data) {
        AccelData record = getSingleRecord(data);
        rawData.add(record);
    }

    private AccelData getSingleRecord(String[] data) {
        if (data.length == 0)
            return new AccelData();

        //Separate header
        String vehicle = data[2];
        String status = data[3];

        //Get body
        ArrayList<String> body = new ArrayList<>();
        body.addAll(Arrays.asList(data).subList(5, data.length));
        WindowData windowData = new WindowData();
        windowData.saveData(body, FREQUENCY);

        AccelData singleAccel = new AccelData();
        singleAccel.vehicle = vehicle;
        singleAccel.status = status;
        singleAccel.data = windowData;

        return singleAccel;
    }

    public void calculateFunctions() {
        FileUtils.fileUtils.writeAllTitles();

        for (AccelData accel : rawData) {
            WindowData wd = accel.data;
            int count = wd.getSize();
            String status = accel.getStatus();
            String vehicle = accel.getVehicle();

            for (int i = 0; i < count; i++) {
                SensorFunctions func = new SensorFunctions(vehicle, status);
                ArrayList<SimpleAccelData> data = wd.getAt(i);

                func.saveMean(data);
                func.saveVariance(data);
                func.saveGravity(data);
                func.saveAccels(data);
                func.saveRMS(data);
                func.saveRelativeFeature(data);
                func.saveSMA(data);
                func.saveHjorthFeatures(data);
                func.saveFourier(wd, i);
            }
        }
        WekaUtils.shared.convert();
    }

    private boolean isAccelerometer(Sensor sensor) {
        return sensor.getType() == Sensor.TYPE_ACCELEROMETER;
    }

    public void writeHeader() {
        String header = "UserID: Starting" + "\n" +
                "Username: Namdv" + "\n" +
                "Vehicle: " + "\n" +
                "Status: " + "\n\n";
        FileUtils.fileUtils.writeToRawFile(header);
    }

    public int sensorDelay() {
        return 1000 / FREQUENCY;
    }
}
