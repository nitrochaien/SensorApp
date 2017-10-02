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

import namdv.sensorapp.Constant;
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

    String currentPrediction = "";

    public void createRandomForestModel(String arffPath, String modelPath) {
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

    public void testModelVehicle(String input) {
        try {
            ArrayList<Attribute> attributes = attributeSet();
            String[] split = input.split(",");
            if (split.length == 0)
                return;

            Instances dataRaw = new Instances("accels_func", attributes, 0);
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
            System.out.println("--------------------------");
            System.out.println(dataRaw);
            System.out.println("--------------------------");

            Classifier cls = (RandomForest)SerializationHelper.read(Constant.MODEL_PATH);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

            double predictValue = cls.classifyInstance(dataRaw.instance(0));
            currentPrediction = dataRaw.classAttribute().value((int)predictValue);
            System.out.println("The predicted value is: " + currentPrediction);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void testModelBike(String input) {
        try {
            ArrayList<Attribute> attributes = attributeActivitySet();
            String[] split = input.split(",");
            if (split.length == 0)
                return;

            Instances dataRaw = new Instances("accels_func", attributes, 0);
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
            System.out.println("--------------------------");
            System.out.println(dataRaw);
            System.out.println("--------------------------");

            Classifier cls = (RandomForest)SerializationHelper.read(Constant.BIKE_MODEL_PATH);
            dataRaw.setClassIndex(dataRaw.numAttributes() - 1);

            double predictValue = cls.classifyInstance(dataRaw.instance(0));
            currentPrediction = dataRaw.classAttribute().value((int)predictValue);
            System.out.println("The predicted value is: " + currentPrediction);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public String getPrediction() {
        return currentPrediction;
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

    public ArrayList<Attribute> attributeActivitySet() {
        ArrayList<Attribute> attributes = new ArrayList<>();

        ArrayList<String> vehicles = new ArrayList<>();
        vehicles.add("Car");
        vehicles.add("Bike");

        ArrayList<String> activities = new ArrayList<>();
        activities.add("Moving");
        activities.add("Left");
        activities.add("Dec");
        activities.add("Right");
        activities.add("Stop");

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
        attributes.add(new Attribute("vehicle", vehicles));
        attributes.add(new Attribute("status", activities));

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
