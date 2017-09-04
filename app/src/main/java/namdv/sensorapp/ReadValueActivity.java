package namdv.sensorapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import namdv.sensorapp.Utils.data.AccelData;
import namdv.sensorapp.Utils.data.SimpleAccelData;
import namdv.sensorapp.Utils.features.SensorFunctions;
import namdv.sensorapp.Utils.data.WindowData;
import namdv.sensorapp.Utils.file.CSV2Arff;
import namdv.sensorapp.Utils.file.FileUtils;
import namdv.sensorapp.Utils.file.WekaUtils;

/**
 * Created by namdv on 7/20/17.
 */

public class ReadValueActivity extends AppCompatActivity {
    Button btnCalFunc, btnCalGyro, btnCalFourier;

    private ArrayList<AccelData> accels = new ArrayList<>();
    private ArrayList<AccelData> gyros = new ArrayList<>();

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_value);
        initView();

        checkPermission();
    }

    private void saveData() {
        getAssetsFiles("Accel");
        getAssetsFiles("Gyro");
        System.out.println("Data saved!!!");
    }

    private void getAssetsFiles(String folderName) {
        try {
            String[] list = getAssets().list(folderName);
            for (String file : list) {
                if (!file.contains(".csv"))
                    break;
                String data = FileUtils.fileUtils.getAccelDataInFolder(this, folderName, file);
                String[] dataLines = data.split("\n");
                if (folderName.equals("Accel"))
                    saveAccels(dataLines);
                else if (folderName.equals("Gyro"))
                    saveGyros(dataLines);
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
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

    private void saveAccels(String[] data) {
        AccelData record = getSingleRecord(data);
        accels.add(record);
    }

    private void saveGyros(String[] data) {
        AccelData record = getSingleRecord(data);
        gyros.add(record);
    }

    private void initView() {
        btnCalFunc = (Button) findViewById(R.id.btnCalFunc);
        btnCalFunc.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calculateAccel();
            }
        });

        btnCalFourier = (Button) findViewById(R.id.btnCalFourier);
        btnCalFourier.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                randomForest();
            }
        });

        btnCalGyro = (Button) findViewById(R.id.btnCalGyro);
        btnCalGyro.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                calculateGyro();
            }
        });
    }

    private void convert() {
        //convert accel
        File root1 = Environment.getExternalStorageDirectory();
        String rootPath1 = root1.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME_ARFF;
        String[] inputs1 = new String[2];
//        inputs1[0] = FileUtils.fileUtils.getAssetFilePath(this, FileUtils.ACCEL_FUNCS_FILE_NAME);
        inputs1[0] = root1.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME;
        inputs1[1] = rootPath1;
        CSV2Arff.shared.convert(inputs1);

        //convert accel&gyro
//        File root = Environment.getExternalStorageDirectory();
//        String rootPath = root.getAbsolutePath() + "/" + FileUtils.ACCEL_AND_GYRO_FUNCS_FILE_NAME_ARFF;
//        String[] inputs = new String[2];
//        inputs[0] = FileUtils.fileUtils.getAssetFilePath(this, FileUtils.ACCEL_AND_GYRO_FUNCS_FILE_NAME);
//        inputs[1] = rootPath;
//        CSV2Arff.shared.convert(inputs);
    }

    private void calculateAccel() {
        FileUtils.fileUtils.writeAccelTitle();

        for (AccelData accel : accels) {
            WindowData wd = accel.data;
            int count = wd.getSize();
            String status = accel.getStatus();
            String vehicle = accel.getVehicle();

            for (int i = 0; i < count; i++) {
                SensorFunctions func = new SensorFunctions(vehicle, status);
                ArrayList<SimpleAccelData> data = wd.getAt(i);

                func.saveGravity(data);
                func.saveAccels(data);
                func.saveRMS(data);
                func.saveRelativeFeature(data);
                func.saveSMA(data);
                func.saveHjorthFeatures(data);
                func.saveFourier(wd, i);
            }
        }
        convert();
    }

    private void calculateGyro() {
        FileUtils.fileUtils.writeAllTitles();

        ArrayList<AccelData> allRecords = new ArrayList<>();
        allRecords.addAll(gyros);
        allRecords.addAll(accels);

        for (AccelData accel : allRecords) {
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
        convert();
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        } else {
            saveData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    saveData();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void randomForest() {
        WekaUtils.shared.classifyByRandomForest();
    }
}
