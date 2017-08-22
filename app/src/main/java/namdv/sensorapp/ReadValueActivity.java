package namdv.sensorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

import namdv.sensorapp.Utils.SensorFunctions;
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

        for (String line : lines)
            System.out.println("Lines: " + line);

        //Separate header
        String userId = lines[0];
        String username = lines[1];
        String vehicle = lines[2];
        String status = lines[3];
        FileUtils.fileUtils.writeHeader(userId, username, vehicle, status);

        //Get body
        ArrayList<String> body = new ArrayList<>();
        for (int i = 5; i < lines.length; i++)
        {
            body.add(lines[i]);
        }
        WindowData.window.saveData(body);

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
        }
    }

    private void calculateFourier() {
        SensorFunctions.shared.saveFourier();
    }
}
