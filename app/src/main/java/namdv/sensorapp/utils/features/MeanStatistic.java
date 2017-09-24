package namdv.sensorapp.utils.features;

import java.util.ArrayList;

import namdv.sensorapp.utils.data.SimpleAccelData;

/**
 * Created by namdv on 6/30/17.
 */

public class MeanStatistic {
    public static MeanStatistic shared = new MeanStatistic();

    public double getMean(ArrayList<SimpleAccelData> data) {
        //DOCME: function (9) & (14)
        if (data == null || data.size() == 0) return -1;

        int numberOfData = data.size();
        double total = 0;
        for (SimpleAccelData acc : data) {
            double gravity = getSquareRootXYZ(acc);
            total += gravity;
        }
        return total / numberOfData;
    }

    public double getMeanX(ArrayList<SimpleAccelData> data) {
        //DOCME: function (7)
        if (data == null || data.size() == 0) return -1;

        int numberOfData = data.size();
        double total = 0;
        for (SimpleAccelData acc : data) {
            total += acc.getX();
        }
        return total / numberOfData;
    }

    public double getMeanY(ArrayList<SimpleAccelData> data) {
        //DOCME: function (7)
        if (data == null || data.size() == 0) return -1;

        int numberOfData = data.size();
        double total = 0;
        for (SimpleAccelData acc : data) {
            total += acc.getY();
        }
        return total / numberOfData;
    }

    public double getMeanZ(ArrayList<SimpleAccelData> data) {
        //DOCME: function (7)
        if (data == null || data.size() == 0) return -1;

        int numberOfData = data.size();
        double total = 0;
        for (SimpleAccelData acc : data) {
            total += acc.getZ();
        }
        return total / numberOfData;
    }

    public double getSquareRootXYZ(SimpleAccelData acc) {
        //DOCME: function (8)
        return Math.sqrt(Math.pow(acc.getX(),2) + Math.pow(acc.getY(),2) + Math.pow(acc.getZ(),2));
    }

    public double getSquareRootXY(SimpleAccelData acc) {
        return Math.sqrt(Math.pow(acc.getX(),2) + Math.pow(acc.getY(),2));
    }

    public double getSquareRootYZ(SimpleAccelData acc) {
        return Math.sqrt(Math.pow(acc.getY(),2) + Math.pow(acc.getZ(),2));
    }

    public double getSquareRootXZ(SimpleAccelData acc) {
        return Math.sqrt(Math.pow(acc.getX(),2) + Math.pow(acc.getZ(),2));
    }
}
