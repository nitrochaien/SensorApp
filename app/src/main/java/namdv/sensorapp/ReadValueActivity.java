package namdv.sensorapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import namdv.sensorapp.Utils.features.SensorFunctions;
import namdv.sensorapp.Utils.data.WindowData;
import namdv.sensorapp.Utils.file.FileUtils;
import namdv.sensorapp.Utils.file.WekaUtils;

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

//        initData();
//        FileUtils.fileUtils.writeTitle();
        checkPermission();
    }

    private void initData() {
        String accelValue = FileUtils.fileUtils.getAccelData(this);
        String[] lines = accelValue.split("\n");

        for (String line : lines)
            System.out.println("Lines: " + line);

        //Separate header
        String userId = lines[0];
        String username = lines[1];
        String vehicle = lines[2];
        String status = lines[3];
        WindowData.window.saveHeader(userId, username, vehicle, status);

        //Get body
        ArrayList<String> body = new ArrayList<>();
        for (int i = 5; i < lines.length; i++)
        {
            body.add(lines[i]);
        }
        WindowData.window.saveData(body, 50);
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
                randomForest();
            }
        });
    }

    private void calculateFunctions() {

        double count = WindowData.window.getSize();

        for (int i = 0; i < count; i++) {
            SensorFunctions.shared.saveMean(i);
            SensorFunctions.shared.saveGravity(i);
            SensorFunctions.shared.saveAccels(i);
            SensorFunctions.shared.saveRMS(i);
            SensorFunctions.shared.saveVariance(i);
            SensorFunctions.shared.saveRelativeFeature(i);
            SensorFunctions.shared.saveSMA(i);
            SensorFunctions.shared.saveHjorthFeatures(i);
            SensorFunctions.shared.saveFourier(i);
        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        1);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.

                initData();
                FileUtils.fileUtils.writeTitle();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    initData();
                    FileUtils.fileUtils.writeTitle();

                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void randomForest() {
        WekaUtils.shared.classifyByRandomForest(this);
    }
}
