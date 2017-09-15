package namdv.sensorapp.Utils.file;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.SparseInstance;
import weka.core.converters.ArffLoader;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToWordVector;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FileUtils.FOLDER_NAME;
    String arffPath = root + "/accel_funcs.arff";
    String modelPath = root + "/j48.model";

    public void classifyByRandomForest() {
        BufferedReader br;
        int numFolds = 10;

        try {
            br = new BufferedReader(new FileReader(arffPath));

            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);

            RandomForest rf = new RandomForest();
            rf.setNumTrees(50);
            rf.buildClassifier(trainData);

            Evaluation evaluation = new Evaluation(trainData);
            Random ran = new Random(1);
            evaluation.crossValidateModel(rf, trainData, numFolds, ran);
            evaluation.evaluateModel(rf, trainData);

            System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
            System.out.println(evaluation.toClassDetailsString());
            System.out.println(evaluation.toMatrixString());

            Classifier cls = new J48();
            cls.buildClassifier(trainData);
            SerializationHelper.write(modelPath, cls);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void convert() {
        //convert accel
        File root1 = Environment.getExternalStorageDirectory();
        String rootPath1 = root1.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME_ARFF;
        String[] inputs1 = new String[2];
        inputs1[0] = root1.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.ACCEL_FUNCS_FILE_NAME;
        inputs1[1] = rootPath1;
        CSV2Arff.shared.convert(inputs1);
    }

    public void predict() {
        try {
            Classifier cls = (Classifier) SerializationHelper.read(modelPath);

            BufferedReader datafile = new BufferedReader(new FileReader(arffPath)); //read training data

            Instances data = new Instances(datafile);
            data.setClassIndex(data.numAttributes() - 1);

            Filter filter = new StringToWordVector(50);//keep 50 words
            filter.setInputFormat(data);
            Instances filteredData = Filter.useFilter(data, filter);

            // rebuild classifier
            cls.buildClassifier(filteredData);


            String testInstance= "Text that I want to use as an unseen instance and predict whether it's positive or negative";
            System.out.println(">create test instance");
            ArrayList<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute("text"));


            // Add class attribute.
            ArrayList<String> classValues = new ArrayList<>();
            classValues.add("Bike");
            classValues.add("Car");

            attributes.add(new Attribute("Tone", classValues));
            // Create dataset with initial capacity of 100, and set index of class.
            Instances tests = new Instances("test istance", attributes, 100);
            tests.setClassIndex(tests.numAttributes() - 1);

            Instance test = new DenseInstance(2);
            // Set value for message attribute
            Attribute messageAtt = tests.attribute("text");
            test.setValue(messageAtt, messageAtt.addStringValue(testInstance));

            test.setDataset(tests);

            Filter filter2 = new StringToWordVector(50);
            filter2.setInputFormat(tests);
            Instances filteredTests = Filter.useFilter(tests, filter2);

            System.out.println(">train Test filter using training data");
            Standardize sfilter = new Standardize(); //Match the number of attributes between src and dest.
            sfilter.setInputFormat(filteredData);  // initializing the filter with training set
            filteredTests = Filter.useFilter(filteredData, sfilter);    // create new test set

            ArffSaver saver = new ArffSaver(); //save test data to ARFF file
            saver.setInstances(filteredTests);
            File unseenFile = new File ("Tweets/unseen.ARFF");
            saver.setFile(unseenFile);
            saver.writeBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
