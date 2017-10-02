package namdv.sensorapp.modules;

import android.content.Context;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import namdv.sensorapp.Constant;
import namdv.sensorapp.utils.WekaUtils;
import namdv.sensorapp.utils.data.AccelData;
import namdv.sensorapp.utils.data.SimpleAccelData;
import namdv.sensorapp.utils.data.WindowData;
import namdv.sensorapp.utils.features.AccelFunctions;
import namdv.sensorapp.utils.features.BikeFunctions;
import namdv.sensorapp.utils.file.CSV2Arff;
import namdv.sensorapp.utils.file.FileUtils;

/**
 * Created by namdv on 9/22/17.
 */

public class CreateModelHelper
{
    private Context context;
    private ArrayList<AccelData> accels = new ArrayList<>();

    private ArrayList<AccelData> bikeActivities = new ArrayList<>();

    public CreateModelHelper(Context context) {
        this.context = context;
    }
    public void saveData() {
        getAssetsFiles("Accel");
    }

    public void getAssetsFiles(String folderName) {
        try {
            String[] list = context.getAssets().list(folderName);
            if (folderName.equals("Accel"))
            {
                for (String file : list) {
                    if (!file.contains(".csv"))
                        break;
                    String data = FileUtils.fileUtils.getAccelDataInFolder(context, folderName, file);
                    String[] dataLines = data.split("\n");

                    //save vehicle
                    saveAccels(dataLines);

                    //save activity
                    String[] split = file.split("_");
                    String vehicleType = split[7];
                    System.out.println("Vehicle type: " + vehicleType);
                    if (vehicleType.equals("Bike")) {
                        saveBikeActivities(dataLines);
                    }
                }
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

    private void saveBikeActivities(String[] data) {
        AccelData record = getSingleRecord(data);
        bikeActivities.add(record);
    }

    private AccelData getSingleRecord(String[] data) {
        if (data.length == 0)
            return new AccelData();

        //Separate header
        String vehicle = data[2];
        String status = data[3];
        status = validStatus(status);

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
        FileUtils.fileUtils.writeAccelTitles();

        for (AccelData accel : accels) {
            WindowData wd = accel.data;
            int count = wd.getSize();
            String status = accel.getStatus();
            String vehicle = accel.getVehicle();

            for (int i = 0; i < count; i++) {
                AccelFunctions func = new AccelFunctions(vehicle, status);
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
                func.addAttributeVehicle();
            }
        }
        accels.clear();
        CSV2Arff.shared.convert(new String[] { Constant.CSV_PATH, Constant.ARFF_PATH });
        WekaUtils.shared.createRandomForestModel(Constant.ARFF_PATH, Constant.MODEL_PATH);
        System.out.println("Created accel model!!!");
    }

    public void calculateBikeActivity() {
        FileUtils.fileUtils.writeActivityTitles();

        for (AccelData bike : bikeActivities) {
            WindowData wd = bike.data;
            int count = wd.getSize();
            String status = bike.getStatus();
            String vehicle = bike.getVehicle();

            for (int i = 0; i < count; i++) {
                BikeFunctions func = new BikeFunctions(vehicle, status);
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
                func.addAttributeActivity();
            }
        }
        bikeActivities.clear();
        CSV2Arff.shared.convert(new String[] { Constant.BIKE_PATH_CSV, Constant.BIKE_PATH_ARFF});
        WekaUtils.shared.createRandomForestModel(Constant.BIKE_PATH_ARFF, Constant.BIKE_MODEL_PATH);
    }

    private String validStatus(String input) {
        if (input.equals("S")) return "Stop";
        if (input.equals("L")) return "Left";
        if (input.equals("R")) return "Right";
        if (input.equals("M") || input.equals("Mov")) return "Moving";
        return input;
    }
}
