package namdv.sensorapp;

import android.os.Handler;

import java.util.TimerTask;

/**
 * Created by namdv on 6/7/17.
 */

public class CustomTimerTask extends TimerTask
{
    @Override
    public void run()
    {
        new Thread(new Runnable() {
            @Override
            public void run() {
            }
        }).start();
    }
}
