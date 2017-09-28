package namdv.sensorapp.modules;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import namdv.sensorapp.utils.WekaUtils;
import namdv.sensorapp.utils.data.AccelData;
import namdv.sensorapp.utils.data.SimpleAccelData;
import namdv.sensorapp.utils.data.WindowData;
import namdv.sensorapp.utils.features.SensorFunctions;
import namdv.sensorapp.utils.file.FileUtils;

/**
 * Created by namdv on 9/22/17.
 */

public class CreateModelHelper
{
    private Context context;
    private ArrayList<AccelData> accels = new ArrayList<>();

    public CreateModelHelper(Context context) {
        this.context = context;
    }
    public void saveData() {
        getAssetsFiles("Accel");
    }

    public void getAssetsFiles(String folderName) {
        try {
            String[] list = context.getAssets().list(folderName);
            for (String file : list) {
                if (!file.contains(".csv"))
                    break;
                String data = FileUtils.fileUtils.getAccelDataInFolder(context, folderName, file);
                String[] dataLines = data.split("\n");
                if (folderName.equals("Accel"))
                    saveAccels(dataLines);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private void saveAccels(String[] data) {
        AccelData record = getSingleRecord(data);
        accels.add(record);
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
        windowData.saveData(body, 50);

        AccelData singleAccel = new AccelData();
        singleAccel.vehicle = vehicle;
        singleAccel.status = status;
        singleAccel.data = windowData;

        return singleAccel;
    }

    public void calculateAccel() {
        FileUtils.fileUtils.writeAllTitles();

        for (AccelData accel : accels) {
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
        WekaUtils.shared.createRandomForestModel();
    }
}
