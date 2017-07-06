package namdv.sensorapp.Utils;

import java.util.ArrayList;

/**
 * Created by namdv on 6/30/17.
 */

public class VarianceStatistic {
    public static VarianceStatistic shared = new VarianceStatistic();

    public double getVariance(ArrayList<SimpleAccelData> data) {
        //DOCME: function (15)
        double averageValue = MeanStatistic.shared.getMean(data); //DOCME: function (16)
        if (averageValue == -1) return -1;

        double total = 0;
        for (SimpleAccelData acc : data) {
            //TODO: check if sqrtData is correct? (15)
            double sqrtData = MeanStatistic.shared.getSquareRootXYZ(acc);
            total += Math.pow((sqrtData - averageValue), 2);
        }
        return total / data.size();
    }

    public double getStandardDeviation(ArrayList<SimpleAccelData> data) {
        //DOCME: squareroot of function (15)
        double variance = getVariance(data);
        return Math.sqrt(variance);
    }
}
