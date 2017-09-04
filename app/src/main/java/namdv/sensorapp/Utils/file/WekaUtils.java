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
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.filters.Filter;
import weka.filters.supervised.attribute.AttributeSelection;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    public void classifyByRandomForest(Context context) {
        BufferedReader br;
        int numFolds = 10;
        String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/calculate_data.arff";

        try {
            br = new BufferedReader(new FileReader(root));

            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);

            RandomForest rf = new RandomForest();
            rf.setNumTrees(50);

            Evaluation evaluation = new Evaluation(trainData);
            evaluation.crossValidateModel(rf, trainData, numFolds, new Random(1));

            System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
            System.out.println(evaluation.toClassDetailsString());
            System.out.println(evaluation.toMatrixString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
