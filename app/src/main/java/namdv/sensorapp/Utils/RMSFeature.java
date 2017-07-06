package namdv.sensorapp.Utils;

import android.util.SparseArray;

import java.util.ArrayList;

/**
 * Created by namdv on 6/30/17.
 */

public class RMSFeature {
    public static RMSFeature shared = new RMSFeature();

    private SparseArray<ArrayList<SimpleAccelData>> windowData;

    public RMSFeature() {
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

    public double getWindowsAvarageGravity(int r) {
        //DOCME: function (10)
        //TODO: Find out definition of 'r'
        int numberOfWindows = windowData.size();
        if (numberOfWindows == 0) return -1;

        int k = r - numberOfWindows + 1;
        double total = 0;
        for (int i = k; i <= r; i++) {
            double gravity = getAverageGravity(i);
            total += gravity;
        }
        return total / numberOfWindows;
    }

    public double getAverageGravity(int index) {
        ArrayList<SimpleAccelData> data = getAt(index);
        return MeanStatistic.shared.getMean(data);
    }

    public double getGravity(SimpleAccelData acc) {
        return MeanStatistic.shared.getSquareRootXYZ(acc);
    }

    public double getHorizontalAcceleration(SimpleAccelData acc) {
        //DOCME: function (11)
        return MeanStatistic.shared.getSquareRootXY(acc);
    }

    public double getVerticalAcceleration(SimpleAccelData acc) {
        //DOCME: function (12)
        int r = 0;
        double windowsAverageGravity = getWindowsAvarageGravity(r);
        return acc.getZ() - windowsAverageGravity;
    }

    public double getRMS(ArrayList<SimpleAccelData> data, int indexOfData) {
        //DOCME: function (13)
        if (data == null || data.size() == 0) return -1;

        double gravity = MeanStatistic.shared.getMean(data);
        SimpleAccelData acc = data.get(indexOfData);
        double sqrt = MeanStatistic.shared.getSquareRootXYZ(acc);

        return sqrt - gravity;
    }
}
