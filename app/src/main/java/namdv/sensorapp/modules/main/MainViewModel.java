package namdv.sensorapp.modules.main;

import android.hardware.Sensor;
import android.hardware.SensorEvent;

import java.util.Date;

import namdv.sensorapp.Utils.file.FileUtils;

/**
 * Created by namdv on 9/11/17.
 */

public class MainViewModel {
    private static final int FREQUENCY = 50;
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
