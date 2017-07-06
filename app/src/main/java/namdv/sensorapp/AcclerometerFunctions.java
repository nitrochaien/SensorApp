package namdv.sensorapp;

import java.util.ArrayList;

/**
 * Created by namdv on 6/4/17.
 */

public class AcclerometerFunctions
{
    public String medium(double x, double y, double z)
    {
        double square = square(x) + square(y) + square(z);
        double squareRoot = Math.sqrt(square);
        return String.valueOf(squareRoot);
    }

    public String meanX()
    {
        ArrayList<String> list = getAllValueOfX();
        if (list.isEmpty())
            return "";

        double total = 0;
        for(String s : list)
        {
            if (s == null)
                continue;

            double value = Double.parseDouble(s);
            total += value;
        }
        double result = total / list.size();
        return String.valueOf(result);
    }

    public String meanY()
    {
        ArrayList<String> list = getAllValueOfY();
        if (list.isEmpty())
            return "";

        double total = 0;
        for(String s : list)
        {
            if (s == null)
                continue;

            double value = Double.parseDouble(s);
            total += value;
        }
        double result = total / list.size();
        return String.valueOf(result);
    }

    public String meanZ()
    {
        ArrayList<String> list = getAllValueOfZ();
        if (list.isEmpty())
            return "";

        double total = 0;
        for(String s : list)
        {
            if (s == null)
                continue;

            double value = Double.parseDouble(s);
            total += value;
        }
        double result = total / list.size();
        return String.valueOf(result);
    }

    private String[] getLine()
    {
        String rawData = new FileUtils().getRawData();
        return rawData.split("\n");
    }

    private ArrayList<String> getAllValueOfX()
    {
        ArrayList<String> list = new ArrayList<>();

        String[] split = getLine();
        for (String line : split)
        {
            String[] lineSplit = line.split("\t");
            if (lineSplit.length > 0)
                list.add(lineSplit[0]);
        }
        return list;
    }

    private ArrayList<String> getAllValueOfY()
    {
        ArrayList<String> list = new ArrayList<>();

        String[] split = getLine();
        for (String line : split)
        {
            String[] lineSplit = line.split("\t");
            if (lineSplit.length > 0)
                list.add(lineSplit[0]);
        }
        return list;
    }

    private ArrayList<String> getAllValueOfZ()
    {
        ArrayList<String> list = new ArrayList<>();

        String[] split = getLine();
        for (String line : split)
        {
            String[] lineSplit = line.split("\t");
            if (lineSplit.length > 0)
                list.add(lineSplit[0]);
        }
        return list;
    }

    private double square(double input)
    {
        return Math.pow(input, 2);
    }
}
