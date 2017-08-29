package namdv.sensorapp.Utils.file;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Debug;
import weka.core.Instances;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    public void classifyByRandomForest(Context context) {
        BufferedReader br;
        int numFolds = 10;
        try {
            br = new BufferedReader(new InputStreamReader(
                    context.getAssets().open("01_04_2017_16_12_UserIDStarting_Admin_Bike_Dec_ROOT_ACCE_DATA.arff")));

            Instances trainData = new Instances(br);
            trainData.setClassIndex(trainData.numAttributes() - 1);
            br.close();
            RandomForest rf = new RandomForest();
            rf.setNumTrees(100);

            Evaluation evaluation = new Evaluation(trainData);
            evaluation.crossValidateModel(rf, trainData, numFolds, new Debug.Random(1));

            System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
            System.out.println(evaluation.toClassDetailsString());
            System.out.println("Results For Class -1- ");
            System.out.println("Precision =  " + evaluation.precision(0));
            System.out.println("Recall =  " + evaluation.recall(0));
            System.out.println("F-measure =  " + evaluation.fMeasure(0));
            System.out.println("Results For Class -2- ");
            System.out.println("Precision =  " + evaluation.precision(1));
            System.out.println("Recall =  " + evaluation.recall(1));
            System.out.println("F-measure =  " + evaluation.fMeasure(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
