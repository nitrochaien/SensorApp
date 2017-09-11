package namdv.sensorapp.modules.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import namdv.sensorapp.R;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private SensorManager manager;

    private Button btnCollectData;
    private TextView tvStatus;
    private boolean isCollectingData;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private MainViewModel model = new MainViewModel();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        checkPermission();
    }

    private void initView() {
        btnCollectData = (Button) findViewById(R.id.btn_collect_data);
        tvStatus = (TextView) findViewById(R.id.tv_status);

        btnCollectData.setOnClickListener(this);
    }

    private void initData() {
        defineSensor();
        isCollectingData = false;
    }

    private void defineSensor() {
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        model.writeAcceleromterDataToFile(event);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onClick(View v) {
        if (isCollectingData) {
            manager.unregisterListener(this);
            btnCollectData.setText(R.string.collect_data);
            tvStatus.setText("Done!");
        } else {
            model.writeHeader();
            Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(this, sensor, model.sensorDelay());
            btnCollectData.setText(R.string.stop);
            tvStatus.setText("Collecting data...");
        }
        isCollectingData = !isCollectingData;
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
}
