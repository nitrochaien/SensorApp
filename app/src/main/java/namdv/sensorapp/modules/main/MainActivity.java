package namdv.sensorapp.modules.main;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

import namdv.sensorapp.R;
import namdv.sensorapp.Utils.WekaUtils;
import namdv.sensorapp.Utils.data.SimpleAccelData;
import namdv.sensorapp.Utils.data.WindowData;
import namdv.sensorapp.Utils.features.FrequencyStatistic;
import namdv.sensorapp.Utils.features.HjorthStatistics;
import namdv.sensorapp.Utils.features.MeanStatistic;
import namdv.sensorapp.Utils.features.RMSFeature;
import namdv.sensorapp.Utils.features.RelativeFeatures;
import namdv.sensorapp.Utils.features.SMAStatistic;
import namdv.sensorapp.Utils.features.VarianceStatistic;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private SensorManager manager;

    private Button btnCollectData;
    private TextView tvStatus;
    private boolean isCollectingData;

    private static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;

    private MainViewModel model = new MainViewModel();

    //test
    int startIndex = 0;
    int FREQUENCY = 64;
    ArrayList<SimpleAccelData> currentList = new ArrayList<>();

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
        startIndex += FREQUENCY;
        int currentSize = currentList.size();
        if (startIndex < currentSize) {
            String sFunction = calculateFunctions(currentList);
            WekaUtils.shared.testModel(sFunction);
            currentList.clear();
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

    public String calculateFunctions(ArrayList<SimpleAccelData> data) {
        //mean
        double meanX = MeanStatistic.shared.getMeanX(data);
        double meanY = MeanStatistic.shared.getMeanY(data);
        double meanZ = MeanStatistic.shared.getMeanZ(data);
        double meanXYZ = MeanStatistic.shared.getMean(data);

        //gravity
        double totalGravity = 0;
        for (SimpleAccelData acc : data) {
            totalGravity += meanXYZ;
        }
        double averageGravity = data.size() == 0 ? 0 : totalGravity / data.size();

        //accels
        double totalHorizontalAccels = 0;
        for (SimpleAccelData acc : data) {
            double horizontalAccel = RMSFeature.shared.getHorizontalAcceleration(acc);
            totalHorizontalAccels += horizontalAccel;
        }
        double averageHorizontalAccels = data.size() == 0 ? 0 : totalHorizontalAccels / data.size();

        double totalVerticalAccels = 0;
        for (SimpleAccelData acc : data) {
            double verticalAccel = RMSFeature.shared.getVerticalAcceleration(acc);
            totalVerticalAccels += verticalAccel;
        }
        double averageVerticalAccels = data.size() == 0 ? 0 : totalVerticalAccels / data.size();

        //rms
        double totalRMS = 0;
        for (int i = 0; i < data.size(); i++) {
            double RMS = RMSFeature.shared.getRMS(data, i);
            totalRMS += RMS;
        }
        double averageRMS = data.size() == 0 ? 0 : totalRMS / data.size();

        //variance
        double variance = VarianceStatistic.shared.getVariance(data);

        //hjorth
        double activity = HjorthStatistics.shared.getActivity(data);
        double mobility = HjorthStatistics.shared.getMobility(data);
        double complexity = HjorthStatistics.shared.getComplexity(data);

        //relative
        double relative = RelativeFeatures.shared.getRelativeFeature(data);

        //sma
        double SMA = SMAStatistic.sma.getSMA(data);
        double horizontalEnergy = SMAStatistic.sma.getHorizontalEnergy(data);
        double verticalEnergy = SMAStatistic.sma.getVerticalEnergy(data);
        double vectorSVM = SMAStatistic.sma.getVectorSVM(data);
        double dsvm = SMAStatistic.sma.getDSVM(data);
        double dsvmByRMS = SMAStatistic.sma.getDSVMByRMS(data);

        //frequency
        WindowData wData = new WindowData();
        wData.add(data);
        FrequencyStatistic frequency = new FrequencyStatistic(wData);
        double fourier = frequency.getFourier(0,"x");
        double xfftEnergy = frequency.getXFFTEnergy(data);
        double yfftEnergy = frequency.getYFFTEnergy(data);
        double zfftEnergy = frequency.getZFFTEnergy(data);
        double meanfftEnergy = frequency.getMeanFFTEnergy(data);
        double xfftEntropy = frequency.getXFFTEntropy(data);
        double yfftEntropy = frequency.getYFFTEntropy(data);
        double zfftEntropy = frequency.getZFFTEntropy(data);
        double meanfftEntropy = frequency.getMeanFFTEntropy(data);
        double devX = frequency.getStandardDeviationX(0);
        double devY = frequency.getStandardDeviationY(0);
        double devZ = frequency.getStandardDeviationZ(0);

        return meanX + "," + meanY + "," + meanZ + "," + meanXYZ + "," +
                variance + "," +
                averageGravity + "," + averageHorizontalAccels + "," + averageVerticalAccels + "," +
                averageRMS + "," +
                relative + "," +
                SMA + "," +
                horizontalEnergy + "," + verticalEnergy + "," +
                vectorSVM + "," +
                dsvm + "," + dsvmByRMS + "," +
                activity + "," + mobility + "," + complexity + "," +
                fourier + "," +
                xfftEnergy + "," + yfftEnergy + "," + zfftEnergy + "," + meanfftEnergy + "," +
                xfftEntropy + "," + yfftEntropy + "," + zfftEntropy + "," + meanfftEntropy + "," +
                devX + "," + devY + "," + devZ + "," + "?";

    }

    @Override
    public void onClick(View v) {
        if (isCollectingData) {
            manager.unregisterListener(this);
            btnCollectData.setText(R.string.collect_data);
            tvStatus.setText("STOPPED!!");
        } else {
            Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            manager.registerListener(this, sensor, model.sensorDelay());
            btnCollectData.setText(R.string.stop);
            tvStatus.setText("Monitoring...");

            new AnalyzeDataTask().execute();
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

    private class AnalyzeDataTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params)
        {
            model.calculateFunctions();
            return null;
        }

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid)
        {
            super.onPostExecute(aVoid);
        }
    }
}
