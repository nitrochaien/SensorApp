package namdv.sensorapp;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by namdv on 6/6/17.
 */

public class InfoActivity extends Activity
{
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.info_activity);

        String data = new FileUtils().getRawData();

        tv = (TextView) findViewById(R.id.textView);
        tv.setText(data);
    }
}
