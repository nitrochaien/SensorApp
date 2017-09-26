package namdv.sensorapp.modules;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import namdv.sensorapp.R;
import namdv.sensorapp.utils.WekaUtils;
import namdv.sensorapp.utils.data.SimpleAccelData;

enum State {
    BEGIN, CREATED_MODEL, MONITORING, STOPPED
}

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private SensorManager manager;

    private Button button;
    private TextView tvStatus, tvResult;
    private State state;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private TestModelHelper testModel = new TestModelHelper();
    private CreateModelHelper createModel = new CreateModelHelper(this);
    private String sFunction = "";

    //test
    int FREQUENCY = 50;
    int windowIndex = 1;
    int lastIndex = 0;
    ArrayList<SimpleAccelData> currentList = new ArrayList<>();
    ArrayList<SimpleAccelData> windowList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPermission();
    }

    private void initView() {
        button = (Button) findViewById(R.id.btn);
        button.setOnClickListener(this);
        tvStatus = (TextView) findViewById(R.id.tv_status);
        tvResult = (TextView) findViewById(R.id.tv_result);
    }

    private void initData() {
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        state = State.BEGIN;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int currentSize = currentList.size();
        if (currentSize >= Integer.MAX_VALUE / 2) {
            stopMonitoring();
            return;
        }

        int newIndex = FREQUENCY * windowIndex;
        if (currentSize > newIndex) {
            windowList.clear();
            windowList.addAll(currentList.subList(lastIndex, newIndex));
            new MonitoringDataTask().execute();
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            Date date = new Date();
            Long diffTime = date.getTime();
            float fTime = diffTime.floatValue();

            String timeStamp = String.valueOf(fTime);
            String x = String.valueOf(event.values[0]);
            String y = String.valueOf(event.values[1]);
            String z = String.valueOf(event.values[2]);

            SimpleAccelData accelData = new SimpleAccelData(timeStamp, x, y, z);
            currentList.add(accelData);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if (state == State.BEGIN) {
            new SaveDataTask().execute(createModel);
        } else if (state == State.CREATED_MODEL || state == State.STOPPED) {
            tvStatus.setText("Monitoring...");
            Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(this, sensor, testModel.sensorDelay());
            state = State.MONITORING;
        } else if (state == State.MONITORING) {
            stopMonitoring();
            state = State.STOPPED;
        }
    }

    private void stopMonitoring() {
        manager.unregisterListener(MainActivity.this);
        currentList.clear();
        windowIndex = 1;
        lastIndex = 0;
        windowList.clear();
        WekaUtils.shared.resetPrediction();
        tvStatus.setText("STOPPED!!");
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
            initData();
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

                    initData();
                }
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private class SaveDataTask extends AsyncTask <CreateModelHelper, Void, Void>
    {
        @Override
        protected Void doInBackground(CreateModelHelper... params)
        {
            CreateModelHelper helper = params[0];
            if (helper != null) {
                helper.saveData();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            tvStatus.setText("Saving data... Please wait...");
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            new SaveModelTask().execute(createModel);
        }
    }

    private class SaveModelTask extends AsyncTask <CreateModelHelper, Void, Void>
    {
        @Override
        protected Void doInBackground(CreateModelHelper... params)
        {
            CreateModelHelper helper = params[0];
            if (helper != null) {
                helper.calculateAccel();
            }
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            tvStatus.setText("Creating model... Please wait...");
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            tvStatus.setText("Created model!!\n");
            button.setText("Monitor data");
            state = State.CREATED_MODEL;
        }
    }

    private class MonitoringDataTask extends AsyncTask <Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params)
        {
            WekaUtils.shared.testModel(sFunction);
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            manager.unregisterListener(MainActivity.this);
            sFunction = testModel.calculateFunctions(windowList);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            tvResult.setText(WekaUtils.shared.getPrediction() + "" + windowIndex);
            lastIndex = FREQUENCY * windowIndex;
            windowIndex++;

            if (state == State.STOPPED) {
                return;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
                    manager.registerListener(MainActivity.this, sensor, testModel.sensorDelay());
                }
            }, 100);
        }
    }
}
