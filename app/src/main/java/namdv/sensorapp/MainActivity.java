package namdv.sensorapp;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener
{
    private static String TAG = MainActivity.class.getSimpleName();

    private SensorManager manager;
    private Sensor accelerometer;

    private long lastUpdate;
    private float lastX, lastY, lastZ;

    private TextView tv_x, tv_y, tv_z;
    private Button btnGetInfo, btnBegin, btnStop;

    private int count = 0;

    private FileUtils fileUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        defineSensor();
        defineTextView();
        defineButton();
        defineFileUtils();
    }

    private void defineSensor()
    {
        manager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        accelerometer = manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    private void defineTextView()
    {
        tv_x = (TextView) findViewById(R.id.tv_x);
        tv_y = (TextView) findViewById(R.id.tv_y);
        tv_z = (TextView) findViewById(R.id.tv_z);
    }

    private void defineButton()
    {
        btnGetInfo = (Button) findViewById(R.id.button);
        btnBegin = (Button) findViewById(R.id.buttonBegin);
        btnStop = (Button) findViewById(R.id.buttonStop);
        btnStop.setOnClickListener(this);
        btnBegin.setOnClickListener(this);
        btnGetInfo.setOnClickListener(this);
    }

    private void defineFileUtils()
    {
        fileUtils = new FileUtils();
        fileUtils.clearAllRawData();
    }

    private void generateTimer()
    {
//        timer = new Timer();
//        TimerTask task = new CustomTimerTask();
//        timer.scheduleAtFixedRate(task, 0, 6000);
    }

    private void cancelTimer()
    {
//        timer.cancel();
//        timer = null;
    }

    private void registerSensorListener()
    {
        manager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        generateTimer();
    }

    private void unRegisterSensorListener()
    {
        manager.unregisterListener(this);
        cancelTimer();
    }

    @Override
    public void onSensorChanged(SensorEvent event)
    {
        if (isAccelerometer(event.sensor))
        {
            if (isLongEnough())
            {
                updateTime();

                lastX = event.values[0];
                lastY = event.values[1];
                lastZ = event.values[2];

                updateTextView();
                saveRawDataToFile();

                count++;

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {
    }

    @Override
    public void onClick(View v)
    {
        if (v == btnGetInfo)
        {
            getInfo();
        }
        else if (v == btnBegin)
        {
            registerSensorListener();
        }
        else if (v == btnStop)
        {
            unRegisterSensorListener();
        }
    }

    private boolean isAccelerometer(Sensor sensor)
    {
        return sensor.getType() == Sensor.TYPE_ACCELEROMETER;
    }

    private boolean isLongEnough()
    {
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUpdate) > 100;
    }

    private void updateTime()
    {
        lastUpdate = System.currentTimeMillis();
    }

    private void updateTextView()
    {
        String xValue = getString(R.string.x) + lastX;
        String yValue = getString(R.string.y) + lastY;
        String zValue = getString(R.string.z) + lastZ;
        tv_x.setText(xValue);
        tv_y.setText(yValue);
        tv_z.setText(zValue);
    }

    private void saveRawDataToFile()
    {
        String content = lastX + "\t" + lastY + "\t" + lastZ;
        fileUtils.writeToRawDataFile(content);
        Log.d(TAG, "Write: " + content);
    }

    private void getInfo()
    {
        Intent intent = new Intent(this, InfoActivity.class);
        changeScreen(intent);
    }

    private void changeScreen(Intent intent)
    {
        startActivity(intent);
    }
}
