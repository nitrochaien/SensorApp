package namdv.sensorapp.Utils;

import java.util.ArrayList;

/**
 * Created by namdv on 7/1/17.
 */

public class HjorthStatistics {
    public static HjorthStatistics shared = new HjorthStatistics();

    public double getActivity(ArrayList<SimpleAccelData> data) {
        //DOCME: function (34)
        if (data == null || data.size() < 1) return -1;

        double total = 0;
        for (int i = 0; i < data.size()-1; i++) {
            double d = RMSFeature.shared.getRMS(data, i+1) - RMSFeature.shared.getRMS(data, i);
            total += Math.pow(d, 2);
        }
        return total / (data.size() - 1);
    }

    public double getMobility(ArrayList<SimpleAccelData> data) {
        //DOCME: function (35)
        if (data == null || data.size() < 2) return -1;

        double total = 0;
        for (int i = 0; i < data.size() - 2; i++) {
            double d01 = RMSFeature.shared.getRMS(data, i+1) - RMSFeature.shared.getRMS(data, i);
            double d02 = RMSFeature.shared.getRMS(data, i+2) - RMSFeature.shared.getRMS(data, i+1);
            double d1 = d02 - d01;
            total += Math.pow(d1, 2);
        }
        double m1 = total / (data.size() - 2);
        double activityValue = getActivity(data);
        return activityValue > 0 ? Math.sqrt(m1 / activityValue) : -1;
    }

    public double getComplexity(ArrayList<SimpleAccelData> data) {
        //DOCME: function (36)
        if (data == null || data.size() < 3) return -1;

        double total = 0;
        double totalMobility = 0;
        for (int i = 0; i < data.size() - 3; i++) {
            double d01 = RMSFeature.shared.getRMS(data, i+1) - RMSFeature.shared.getRMS(data, i);
            double d02 = RMSFeature.shared.getRMS(data, i+2) - RMSFeature.shared.getRMS(data, i+1);
            double d03 = RMSFeature.shared.getRMS(data, i+3) - RMSFeature.shared.getRMS(data, i+2);
            double d11 = d02 - d01;
            double d21 = d03 - d02;
            double d3 = d21 - d11;
            total += Math.pow(d3, 2);
            totalMobility += Math.pow(d11, 2);
        }
        double m1 = totalMobility / (data.size() - 2);
        double m2 = total / (data.size() - 3);
        double mobilityValue = getMobility(data);

        return mobilityValue > 0 ? Math.sqrt(m2 / m1) : -1;
    }

    public ArrayList<Double> getAllValues(ArrayList<SimpleAccelData> data) {
        //DOCME: An array store values of function (34) (35) (36)
        ArrayList<Double> arrayValue = new ArrayList<>();
        if (data == null || data.size() < 3) return arrayValue;

        double totalActivity = 0;
        double totalMobility = 0;
        double totalComplexity = 0;
        for (int i = 0; i < data.size() - 3; i++) {
            double d01 = RMSFeature.shared.getRMS(data, i+1) - RMSFeature.shared.getRMS(data, i);
            double d02 = RMSFeature.shared.getRMS(data, i+2) - RMSFeature.shared.getRMS(data, i+1);
            double d03 = RMSFeature.shared.getRMS(data, i+3) - RMSFeature.shared.getRMS(data, i+2);
            double d11 = d02 - d01;
            double d21 = d03 - d02;
            double d3 = d21 - d11;
            totalActivity += Math.pow(d01, 2);
            totalMobility += Math.pow(d11, 2);
            totalComplexity += Math.pow(d3, 2);
        }
        double activityValue = totalActivity / (data.size() -1);
        double m1 = totalMobility / (data.size() - 2);
        double m2 = totalComplexity / (data.size() - 3);
        double mobilityValue = activityValue > 0 ? Math.sqrt(m1 / activityValue) : -1;
        double complexityValue = m1 > 0 ? Math.sqrt(m2 / m1) : -1;

        arrayValue.add(activityValue);
        arrayValue.add(mobilityValue);
        arrayValue.add(complexityValue);

        return arrayValue;
    }
}
