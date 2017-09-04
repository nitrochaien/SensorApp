package namdv.sensorapp.Utils.file;

/**
 * Created by namdv on 9/3/17.
 */

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class CSV2Arff {
    /**
     * takes 2 arguments:
     * - CSV input file
     * - ARFF output file
     */

    public static CSV2Arff shared = new CSV2Arff();

    public void convert(String[] args){
        if (args.length != 2) {
            System.out.println("\nUsage: CSV2Arff <input.csv> <output.arff>\n");
            System.exit(1);
        }

        try {
            // load CSV
            CSVLoader loader = new CSVLoader();
            loader.setSource(new File(args[0]));
            Instances data = loader.getDataSet();

            // save ARFF
            ArffSaver saver = new ArffSaver();
            saver.setInstances(data);
            saver.setFile(new File(args[1]));
            saver.writeBatch();

        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
