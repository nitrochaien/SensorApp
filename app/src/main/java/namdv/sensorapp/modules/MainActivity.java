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
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import namdv.sensorapp.R;
import namdv.sensorapp.utils.WekaUtils;
import namdv.sensorapp.utils.data.SimpleAccelData;

enum State {
    BEGIN,
    CREATING_MODEL, CREATED_MODEL,
    MONITORING_VEHICLE, MONITORING_ACTIVITY,
    STOPPED
}

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private SensorManager manager;

    private Button btnCreateModel, btnStartMonitoringVehicle, btnStopMonitoringVehicle, btnStopMonitoringActivity;
    private TextView tvStatusCreateModel, tvResultVehicle, tvResultActivity;
    private LinearLayout layoutVehicle, layoutActivity;
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
        btnCreateModel = (Button) findViewById(R.id.btn_create_model);
        btnStartMonitoringVehicle = (Button) findViewById(R.id.btn_start_monitoring_vehicle);
        btnStopMonitoringVehicle = (Button) findViewById(R.id.btn_stop_monitoring_vehicle);
        btnStopMonitoringActivity = (Button) findViewById(R.id.btn_stop_monitoring_activity);
        tvStatusCreateModel = (TextView) findViewById(R.id.tv_status_create_model);
        tvResultVehicle = (TextView) findViewById(R.id.tv_result_vehicle);
        tvResultActivity = (TextView) findViewById(R.id.tv_result_activity);
        layoutVehicle = (LinearLayout) findViewById(R.id.layout_result_vehicle);
        layoutActivity = (LinearLayout) findViewById(R.id.layout_result_activity);

        btnCreateModel.setOnClickListener(this);
        btnStartMonitoringVehicle.setOnClickListener(this);
        btnStopMonitoringVehicle.setOnClickListener(this);
        btnStopMonitoringActivity.setOnClickListener(this);

        layoutVehicle.setVisibility(View.GONE);
        layoutActivity.setVisibility(View.GONE);

        initTextViewStatusCreateModel();
    }

    private void initTextViewStatusCreateModel() {
        
    }

    private void initData() {
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        state = State.BEGIN;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        int currentSize = currentList.size();
        if (currentSize >= Integer.MAX_VALUE) {
            refreshData();
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
        if (v == btnCreateModel)
        {
            if (state == State.BEGIN || state == State.CREATED_MODEL) {
                state = State.CREATING_MODEL;
                new SaveDataTask().execute(createModel);
            }
        }
        else if (v == btnStartMonitoringVehicle)
        {
            //TODO: start monitoring
        }
        else if (v == btnStopMonitoringVehicle) {
            //TODO: stop monitoring, show dialog
        }
        else if (v == btnStopMonitoringActivity) {
            //TODO: stop monitoring
        }
        if (state == State.BEGIN) {

        } else if (state == State.CREATED_MODEL || state == State.STOPPED) {
            tvResultVehicle.setText(R.string.monitoring);
            registerSensor();
            state = State.MONITORING_VEHICLE;
        } else if (state == State.MONITORING) {
            stopMonitoring();
            state = State.STOPPED;
        }
    }

    private void stopMonitoring() {
        manager.unregisterListener(MainActivity.this);
        refreshData();
        WekaUtils.shared.resetPrediction();
        tvStatus.setText(R.string.stopped);
        tvResult.setVisibility(View.INVISIBLE);
    }

    private void refreshData() {
        currentList.clear();
        windowIndex = 1;
        lastIndex = 0;
        windowList.clear();
    }

    private void registerSensor() {
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        manager.registerListener(this, sensor, testModel.sensorDelay());
    }

    private void checkPermission() {
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            }
        } else {
            initData();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
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
            tvStatus.setText(R.string.saving_data);
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
            tvStatus.setText(R.string.creating_model);
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
            tvStatus.setText("Created model!!\n");
            button.setText(R.string.monitor_data);
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
            tvResult.setText("Attempts: " + windowIndex + "\n" + WekaUtils.shared.getPrediction());
            lastIndex = FREQUENCY * windowIndex / 2;
            windowIndex++;

            if (state == State.STOPPED) {
                return;
            }

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    registerSensor();
                }
            }, 100);
        }
    }
}
