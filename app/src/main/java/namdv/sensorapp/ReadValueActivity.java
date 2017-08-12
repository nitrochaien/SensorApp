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
        FileUtils.fileUtils.writeTitle();
        SensorFunctions.shared.saveMean();
        SensorFunctions.shared.saveGravity();
        SensorFunctions.shared.saveAccels();
        SensorFunctions.shared.saveRMS();
        SensorFunctions.shared.saveVariance();
        SensorFunctions.shared.saveRelativeFeature();
        SensorFunctions.shared.saveSMA();
        SensorFunctions.shared.saveHjorthFeatures();
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
