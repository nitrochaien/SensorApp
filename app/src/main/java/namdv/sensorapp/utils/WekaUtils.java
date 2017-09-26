package namdv.sensorapp.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import namdv.sensorapp.utils.file.CSV2Arff;
import namdv.sensorapp.utils.file.FileUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.Utils;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FileUtils.FOLDER_NAME;
    String arffPath = root + "/accel_funcs.arff";
    String modelPath = root + "/random_forest.model";

    ArrayList<String> predictions = new ArrayList<>();

    public void createRandomForestModel() {
        BufferedReader br;
        int numFolds = 10;

        try {
            br = new BufferedReader(new FileReader(arffPath));
            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);

            RandomForest rf = new RandomForest();
            rf.setNumTrees(50);
            rf.buildClassifier(trainData);
            SerializationHelper.write(modelPath, rf);

            Evaluation evaluation = new Evaluation(trainData);
            Random ran = new Random(1);
            evaluation.crossValidateModel(rf, trainData, numFolds, ran);
            evaluation.evaluateModel(rf, trainData);

            System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
            System.out.println(evaluation.toClassDetailsString());
            System.out.println(evaluation.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convert() {
        File root = Environment.getExternalStorageDirectory();
        String rootPath1 = root.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME_ARFF;
        String[] inputs1 = new String[2];
        inputs1[0] = root.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME;
        inputs1[1] = rootPath1;
        CSV2Arff.shared.convert(inputs1);
    }

    public void testModel(String input) {
        try {
            ArrayList<Attribute> attributes = attributeSet();
            String[] split = input.split(",");
            if (split.length == 0)
                return;

            Instances dataRaw = new Instances("accels_func", attributes, 0);
//            System.out.println("Before adding any instance");
//            System.out.println("--------------------------");
//            System.out.println(dataRaw);
//            System.out.println("--------------------------");
            double[] value = new double[dataRaw.numAttributes()];
            for (int i = 0; i < split.length; i++) {
                if (i == split.length - 1) {
                    value[i] = Utils.missingValue();
                } else {
                    String val = split[i];
                    float in = isNumeric(val) ? Float.parseFloat(val) : 0;
                    value[i] = in;
                }
            }
            dataRaw.add(new DenseInstance(1.0, value));

//            System.out.println("After adding a instance");
//            System.out.println("--------------------------");
//            System.out.println(dataRaw);
//            System.out.println("--------------------------");

            Classifier cls = (RandomForest)SerializationHelper.read(modelPath);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);
            double predictValue = cls.classifyInstance(dataRaw.instance(0));
            String prediction = dataRaw.classAttribute().value((int)predictValue);
            System.out.println("The predicted value of instance " +
                    Integer.toString(0) +
                    ": " + prediction);
            predictions.add(prediction);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getPrediction() {
        Map<String, Integer> stringsCount = new HashMap<>();
        for(String predict : predictions)
        {
            if (predict.length() > 0) {
                predict = predict.toLowerCase();
                Integer count = stringsCount.get(predict);
                if(count == null) count = 0;
                count++;
                stringsCount.put(predict, count);
            }
        }
        Map.Entry<String,Integer> mostRepeated = null;
        for(Map.Entry<String, Integer> e: stringsCount.entrySet())
        {
            if(mostRepeated == null || mostRepeated.getValue()<e.getValue())
                mostRepeated = e;
        }
        try {
            return mostRepeated != null ? mostRepeated.getKey() : null;
        } catch (NullPointerException e) {
            System.out.println("Cannot find most popular value at the List. Maybe all strings are empty");
            return "";
        }
    }

    public void resetPrediction() {
        predictions.clear();
    }

    public ArrayList<Attribute> attributeSet() {
        ArrayList<Attribute> attributes = new ArrayList<>();

        ArrayList<String> classifyVal = new ArrayList<>();
        classifyVal.add("Car");
        classifyVal.add("Bike");

        attributes.add(new Attribute("meanX"));
        attributes.add(new Attribute("meanY"));
        attributes.add(new Attribute("meanZ"));
        attributes.add(new Attribute("meanXYZ"));
        attributes.add(new Attribute("variance"));
        attributes.add(new Attribute("averageGravity"));
        attributes.add(new Attribute("averageHorizontalAccels"));
        attributes.add(new Attribute("averageVerticalAccels"));
        attributes.add(new Attribute("averageRMS"));
        attributes.add(new Attribute("relative"));
        attributes.add(new Attribute("SMA"));
        attributes.add(new Attribute("horizontalEnergy"));
        attributes.add(new Attribute("verticalEnergy"));
        attributes.add(new Attribute("vectorSVM"));
        attributes.add(new Attribute("dsvm"));
        attributes.add(new Attribute("dsvmByRMS"));
        attributes.add(new Attribute("activity"));
        attributes.add(new Attribute("mobility"));
        attributes.add(new Attribute("complexity"));
        attributes.add(new Attribute("fourier"));
        attributes.add(new Attribute("xfftEnergy"));
        attributes.add(new Attribute("yfftEnergy"));
        attributes.add(new Attribute("zfftEnergy"));
        attributes.add(new Attribute("meanfftEnergy"));
        attributes.add(new Attribute("xfftEntropy"));
        attributes.add(new Attribute("yfftEntropy"));
        attributes.add(new Attribute("zfftEntropy"));
        attributes.add(new Attribute("meanfftEntropy"));
        attributes.add(new Attribute("devX"));
        attributes.add(new Attribute("devY"));
        attributes.add(new Attribute("devZ"));
        attributes.add(new Attribute("vehicle", classifyVal));

        return attributes;
    }

    private static boolean isNumeric(String str)
    {
        try
        {
            double d = Float.parseFloat(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }
}
