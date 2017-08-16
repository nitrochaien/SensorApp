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

        FileUtils.fileUtils.writeTitle();
    }

    private void initData() {
        String accelValue = FileUtils.fileUtils.getAccelData(this);
        String[] lines = accelValue.split("\n");
        saveData(lines);

//        String gyroValue = FileUtils.fileUtils.getGyroData(this);
//        String[] line = gyroValue.split("\n");
//        saveData(line);
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
        SensorFunctions.shared.saveMean(0);
        SensorFunctions.shared.saveGravity(0);
        SensorFunctions.shared.saveAccels(0);
        SensorFunctions.shared.saveRMS(0);
        SensorFunctions.shared.saveVariance(0);
        SensorFunctions.shared.saveRelativeFeature(0);
        SensorFunctions.shared.saveSMA(0);
        SensorFunctions.shared.saveHjorthFeatures(0);

//        SensorFunctions.shared.saveMean(1);
//        SensorFunctions.shared.saveGravity(1);
//        SensorFunctions.shared.saveAccels(1);
//        SensorFunctions.shared.saveRMS(1);
//        SensorFunctions.shared.saveVariance(1);
//        SensorFunctions.shared.saveRelativeFeature(1);
//        SensorFunctions.shared.saveSMA(1);
//        SensorFunctions.shared.saveHjorthFeatures(1);
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

    private void calculateFourier() {
        SensorFunctions.shared.saveFourier();
    }
}
