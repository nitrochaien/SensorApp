package namdv.sensorapp.Utils.features;

import java.util.ArrayList;

import namdv.sensorapp.Utils.data.SimpleAccelData;

/**
 * Created by namdv on 7/10/17.
 */

public class SMAStatistic {
    public static SMAStatistic sma = new SMAStatistic();

    public double getSMA(ArrayList<SimpleAccelData> data) {
        //DOCME: function (28)
        if (data == null || data.size() < 1) return -1;

        double lastTimeStamp = data.get(data.size()-1).getTimestamp();
        double firstTimeStamp = data.get(1).getTimestamp();
        double windowWidth =  lastTimeStamp - firstTimeStamp;
        if (windowWidth == 0) return -1;

        double total = 0;
        for (int i = 1; i < data.size(); i++) {
            SimpleAccelData a = data.get(i);
            SimpleAccelData a_1 = data.get(i-1);

            double firstArg = Math.abs(a_1.getX()) + Math.abs(a.getX())
                    + Math.abs(a_1.getY()) + Math.abs(a.getY())
                    + Math.abs(a_1.getZ()) + Math.abs(a.getZ());
            double secondArg = a.getTimestamp() - a_1.getTimestamp();
            total += firstArg * secondArg;
        }
        return 1 / (2*windowWidth) * total;
    }

    public double getVerticalEnergy(ArrayList<SimpleAccelData> data) {
        if (data == null || data.size() < 1) return -1;

        double total = 0;
        for (int i = 1; i < data.size(); i++) {
            SimpleAccelData item = data.get(i);
            SimpleAccelData item_1 = data.get(i-1);
            double av = RMSFeature.shared.getVerticalAcceleration(item);
            double av_1 = RMSFeature.shared.getVerticalAcceleration(item_1);
            double firstArg = Math.abs(av) + Math.abs(av_1);
            double secondArg = item.getTimestamp() - item_1.getTimestamp();

            total += firstArg * secondArg;
        }
        return total / 2;
    }

    public double getHorizontalEnergy(ArrayList<SimpleAccelData> data) {
        //DOCME: function (29)
        if (data == null || data.size() < 1) return -1;

        double total = 0;
        for (int i = 1; i < data.size(); i++) {
            SimpleAccelData item = data.get(i);
            SimpleAccelData item_1 = data.get(i-1);
            double av = RMSFeature.shared.getHorizontalAcceleration(item);
            double av_1 = RMSFeature.shared.getHorizontalAcceleration(item_1);
            double firstArg = Math.abs(av) + Math.abs(av_1);
            double secondArg = item.getTimestamp() - item_1.getTimestamp();

            total += firstArg * secondArg;
        }
        return total / 2;
    }

    public double getVectorSVM(ArrayList<SimpleAccelData> data) {
        //DOCME: function (30)
        if (data == null || data.size() == 0) return -1;

        double total = 0;
        for (SimpleAccelData item : data) {
            total += MeanStatistic.shared.getSquareRootXYZ(item);
        }
        return total / data.size();
    }

    public double getDSVMByRMS(ArrayList<SimpleAccelData> data) {
        //DOCME: function (32)
        double lastTimeStamp = data.get(data.size()-1).getTimestamp();
        double firstTimeStamp = data.get(1).getTimestamp();
        double windowWidth =  lastTimeStamp - firstTimeStamp;
        if (windowWidth == 0) return -1;

        double total = 0;
        for (int i = 1; i < data.size() - 1; i++) {
            double a = RMSFeature.shared.getRMS(data, i);
            double a_1 = RMSFeature.shared.getRMS(data, i + 1);
            double minus_a = a_1 - a;

            double a_i = RMSFeature.shared.getRMS(data, i - 1);
            double minus_a_i = a - a_i;
            double firstArg = Math.abs(minus_a_i + minus_a);
            double secondArg = data.get(i).getTimestamp() - data.get(i - 1).getTimestamp();

            total += firstArg * secondArg;
        }
        return total / (2 * windowWidth);
    }

    public double getDSVM(ArrayList<SimpleAccelData> data) {
        //DOCME: function (33)
        if (data == null ||  data.size() < 1) return -1;

        double total = 0;
        for (int i = 1; i < data.size(); i++) {
            SimpleAccelData item = data.get(i);
            SimpleAccelData item_1 = data.get(i - 1);
            double ax = Math.abs(item.getX() - item_1.getX());
            double ay = Math.abs(item.getY() - item_1.getY());
            double az = Math.abs(item.getZ() - item_1.getZ());

            total += ax + ay + az;
        }

        return total / data.size();
    }
}
