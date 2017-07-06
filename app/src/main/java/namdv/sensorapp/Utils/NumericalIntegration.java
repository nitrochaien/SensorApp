package namdv.sensorapp.Utils;

import org.apache.commons.math3.analysis.integration.RombergIntegrator;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.FastFourierTransformer;

import java.util.ArrayList;

/**
 * Created by namdv on 7/6/17.
 */

//Tich phan so
public class NumericalIntegration {
    public static NumericalIntegration integration = new NumericalIntegration();

    private ArrayList<Integer> getInterationCounts(int indexOfWindow) {
        ArrayList<Integer> values = new ArrayList<>();
        ArrayList<SimpleAccelData> data = WindowData.window.getAt(indexOfWindow);
        if (data == null || data.isEmpty()) return values;

        int t1 = (int)data.get(0).getTimestamp();
        int t2 = (int)data.get(data.size()-1).getTimestamp();
        values.add(t1);
        values.add(t2);
        return values;
    }

    //DOCME: Function (25)
    public double getX(int indexOfWindow) {
        ArrayList<Integer> interationValues = getInterationCounts(indexOfWindow);
        if (interationValues.size() < 2) return -1;

        int t1 = interationValues.get(0);
        int t2 = interationValues.get(1);
        RombergIntegrator integral = new RombergIntegrator(t1,t2);
        return -1;
    }
}
