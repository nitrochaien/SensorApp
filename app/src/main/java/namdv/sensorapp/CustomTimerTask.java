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
                AcclerometerFunctions functions = new AcclerometerFunctions();
                String meanX = functions.meanX();
                String meanY = functions.meanY();
                String meanZ = functions.meanZ();

                new FileUtils().writeToMediumDataFile(meanX + "\t" + meanY + "\t" + meanZ);
            }
        }).start();
    }
}
