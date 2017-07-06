package namdv.sensorapp.Utils;

import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Created by namdv on 7/6/17.
 */

public class WindowData {
    public static WindowData window = new WindowData();
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
}
