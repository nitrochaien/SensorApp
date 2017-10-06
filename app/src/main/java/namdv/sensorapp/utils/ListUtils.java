package namdv.sensorapp.utils;

import java.util.ArrayList;

import namdv.sensorapp.utils.data.SimpleAccelData;

/**
 * Created by namdv on 10/2/17.
 */

public class ListUtils
{
    public static ListUtils shared = new ListUtils();

    public ArrayList<SimpleAccelData> preDataProcessor(ArrayList<SimpleAccelData> data) {
        int size = data.size();
        boolean sizeIsPowerOfTwo = (size & (size - 1)) == 0;

        if (!sizeIsPowerOfTwo) {
            int newSize = convertToNearestPowerOfTwo(size);
            addZeroValues(data, newSize);
        }
        return data;
    }

    void addZeroValues(ArrayList<SimpleAccelData> data, int newSize) {
        int numberOfZeros = newSize - data.size();
        for (int i = 0; i < numberOfZeros; i++) {
            SimpleAccelData acc = new SimpleAccelData("0","0","0","0");
            data.add(acc);
        }
    }

    int convertToNearestPowerOfTwo(int x) {
        x = x - 1;
        x |= x >> 1;
        x |= x >> 2;
        x |= x >> 4;
        x |= x >> 8;
        x |= x >> 16;
        return x + 1;
    }
}
