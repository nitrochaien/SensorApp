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
    Button btnCalFunc;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_value);

        initView();
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
    }

    private void calculateFunctions() {
        String accelValue = FileUtils.fileUtils.getAccelData(this);
        String[] lines = accelValue.split("\n");
        saveData(lines);

        //calculate functions
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(0);

        //function 7
        double xRMS = SensorFunctions.mean.getMeanX(data);
        System.out.print("xRMS: " + xRMS + "\n");

        //function 8
        ArrayList<Double> gravity = new ArrayList<>();
        for (SimpleAccelData acc : data) {
            double value = SensorFunctions.mean.getSquareRootXYZ(acc);
            System.out.print("gravity value: " + value + "\n");
            gravity.add(value);
        }

        //function 9 & 14
        double mean = SensorFunctions.mean.getMean(data);
        System.out.print("mean: " + mean + "\n");

        //function 10
        double windowGravity = SensorFunctions.rms.getWindowsAvarageGravity(0);
        System.out.print("window gravity: " + windowGravity + "\n");

        //function 11
        ArrayList<Double> horizontalAccels = new ArrayList<>();
        for (SimpleAccelData acc : data) {
            double horizontalAccel = SensorFunctions.rms.getHorizontalAcceleration(acc);
            System.out.print("HorizontalAccel: " + horizontalAccel + "\n");
            horizontalAccels.add(horizontalAccel);
        }

        //function 12
        ArrayList<Double> VerticalAccels = new ArrayList<>();
        for (SimpleAccelData acc : data) {
            double verticalAccel = SensorFunctions.rms.getVerticalAcceleration(acc);
            System.out.print("verticalAccel: " + verticalAccel + "\n");
            VerticalAccels.add(verticalAccel);
        }

        //function 13
        ArrayList<Double> RMSs = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            SimpleAccelData acc = data.get(i);
            double rms = SensorFunctions.rms.getRMS(data, i);
            System.out.print("RMS: " + rms + "\n");
            RMSs.add(rms);
        }
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
