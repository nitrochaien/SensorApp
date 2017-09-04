package namdv.sensorapp.Utils.data;

import android.util.SparseArray;
import java.util.ArrayList;

/**
 * Created by namdv on 7/6/17.
 */

public class WindowData {
    private SparseArray<ArrayList<SimpleAccelData>> windowData;

    public WindowData() {
        if (windowData == null) init();
    }

    private void init() {
        windowData = new SparseArray<>();
    }

    public void add(ArrayList<SimpleAccelData> list) {
        int appendIndex = windowData.size();
        windowData.append(appendIndex, list);
    }

    public ArrayList<SimpleAccelData> getAt(int windowNum) {
        return windowData.get(windowNum);
    }

    public int getSize() {
        return windowData.size();
    }

    public double[] getXValueInWidow(int windowIndex) {
        //DOCME: Get all X values in particular window
        ArrayList<SimpleAccelData> data = windowData.get(windowIndex);
        if (data == null || data.size() == 0) return new double[0];

        int length = data.size();
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            SimpleAccelData item = data.get(i);
            values[i] = item.getX();
        }

        return values;
    }

    public double[] getYValueInWidow(int windowIndex) {
        //DOCME: Get all Y values in particular window
        ArrayList<SimpleAccelData> data = windowData.get(windowIndex);
        if (data == null || data.size() == 0) return new double[0];

        int length = data.size();
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            SimpleAccelData item = data.get(i);
            values[i] = item.getY();
        }

        return values;
    }

    public double[] getZValueInWidow(int windowIndex) {
        //DOCME: Get all Z values in particular window
        ArrayList<SimpleAccelData> data = windowData.get(windowIndex);
        if (data == null || data.size() == 0) return new double[0];

        int length = data.size();
        double[] values = new double[length];
        for (int i = 0; i < length; i++) {
            SimpleAccelData item = data.get(i);
            values[i] = item.getZ();
        }

        return values;
    }

    public double getHammingWindow(int windowIndex) {
        //DOCME: function (38)
        int n = windowData.size();
        if (n == 0) return -1;

        double cos = Math.cos((2*Math.PI*windowIndex) / (n - 1));
        return 0.54 - 0.46 * cos;
    }

    public void saveData(ArrayList<String> input, double frequency) {
        double seconds = 1;
        double numberOfInstancesPerWindow = frequency * seconds;
        double count = 0;

        while (count < input.size()) {
            ArrayList<SimpleAccelData> data = new ArrayList<>();
            double elementCount = count + numberOfInstancesPerWindow;
            double max = elementCount < input.size() ? elementCount : input.size();
            for (int i = (int)count; i < max; i++) {
                String line = input.get(i);
                String[] element = line.split(";");
                String timeStamp = element[0];
                String x = element[1];
                String y = element[2];
                String z = element[3];

//                System.out.println("Window Size: " + getSize());
//                System.out.println("x:" + x);
//                System.out.println("y:" + y);
//                System.out.println("z:" + z);

                SimpleAccelData acc = new SimpleAccelData(timeStamp, x, y, z);
                data.add(acc);
            }
            add(data);
            count += numberOfInstancesPerWindow;
        }
    }
}
