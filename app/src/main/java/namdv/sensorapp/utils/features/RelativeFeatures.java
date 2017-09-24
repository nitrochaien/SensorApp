package namdv.sensorapp.utils.features;

import java.util.ArrayList;

import namdv.sensorapp.utils.data.SimpleAccelData;

/**
 * Created by namdv on 7/1/17.
 */

public class RelativeFeatures {
    public static RelativeFeatures shared = new RelativeFeatures();

    public double getRelativeFeature(ArrayList<SimpleAccelData> data) {
        //DOCME: function (21)
        double numerator = getNumerator(data);
        double denominator = getDenominator(data);

        return denominator != 0 ? numerator / denominator : -1;
    }

    private double getNumerator(ArrayList<SimpleAccelData> data) {
        double meanX = MeanStatistic.shared.getMeanX(data);
        double meanY = MeanStatistic.shared.getMeanY(data);
        double total = 0;
        for (SimpleAccelData acc : data) {
            total += (acc.getX() - meanX)*(acc.getY() - meanY);
        }
        return total;
    }

    private double getDenominator(ArrayList<SimpleAccelData> data) {
        double meanX = MeanStatistic.shared.getMeanX(data);
        double meanY = MeanStatistic.shared.getMeanY(data);
        double totalX = 0;
        double totalY = 0;
        for (SimpleAccelData acc : data) {
            totalX += Math.pow((acc.getX() - meanX), 2);
            totalY += Math.pow((acc.getY() - meanY), 2);
        }

        return Math.sqrt(totalX + totalY);
    }
}
