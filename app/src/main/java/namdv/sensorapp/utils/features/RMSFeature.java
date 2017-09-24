package namdv.sensorapp.utils.features;

import java.util.ArrayList;

import namdv.sensorapp.utils.data.SimpleAccelData;

/**
 * Created by namdv on 6/30/17.
 */

public class RMSFeature {
    public static RMSFeature shared = new RMSFeature();

//    public double getWindowsAvarageGravity(WindowData data) {
//        //DOCME: function (10)
//        //r = current window index
//        int numberOfWindows = data.getSize();
//        if (numberOfWindows == 0) return -1;
//
//        int k = -numberOfWindows + 1;
//        double total = 0;
//        for (int i = k; i <= 0; i++) {
//            double gravity = getAverageGravity(data.getAt(i));
//            total += gravity;
//        }
//        return total / numberOfWindows;
//    }

    public double getHorizontalAcceleration(SimpleAccelData acc) {
        //DOCME: function (11)
        return MeanStatistic.shared.getSquareRootXY(acc);
    }

    public double getVerticalAcceleration(SimpleAccelData acc) {
        //DOCME: function (12)
        return acc.getZ();
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
