package namdv.sensorapp.Utils.file;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
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
import weka.core.Instances;
import weka.core.SerializationHelper;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    public void classifyByRandomForest() {
        BufferedReader br;
        int numFolds = 10;
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FileUtils.FOLDER_NAME;
        String arffPath = root + "/accel_funcs.arff";
        String modelPath = root + "/j48.model";

        try {
            br = new BufferedReader(new FileReader(arffPath));

            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);

            RandomForest rf = new RandomForest();
            rf.setNumTrees(50);

            Evaluation evaluation = new Evaluation(trainData);
            Random ran = new Random(1);
            evaluation.crossValidateModel(rf, trainData, numFolds, ran);

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
}
