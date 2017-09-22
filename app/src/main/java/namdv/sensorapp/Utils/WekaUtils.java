package namdv.sensorapp.Utils;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import namdv.sensorapp.Utils.file.CSV2Arff;
import namdv.sensorapp.Utils.file.FileUtils;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.SerializationHelper;

/**
 * Created by namdv on 8/28/17.
 */

public class WekaUtils
{
    public static WekaUtils shared = new WekaUtils();

    String root = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FileUtils.FOLDER_NAME;
    String arffPath = root + "/accel_funcs.arff";
    String modelPath = root + "/random_forest.model";
    String testPath = root + "/test.arff";

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
            SerializationHelper.write(modelPath, rf);

//            Evaluation evaluation = new Evaluation(trainData);
//            Random ran = new Random(1);
//            evaluation.crossValidateModel(rf, trainData, numFolds, ran);
//            evaluation.evaluateModel(rf, trainData);
//
//            System.out.println(evaluation.toSummaryString("\nResults\n======\n", true));
//            System.out.println(evaluation.toClassDetailsString());
//            System.out.println(evaluation.toMatrixString());
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

    public void testModel(String input) {
        try {
//            Classifier cModel = new RandomForest();
//            cModel.buildClassifier(isTrainingSet);
//
//            SerializationHelper.write("/some/where/nBayes.model", cModel);

            BufferedReader reader = testData(input);
            if (reader == null) return;

            Instances instances = new Instances(reader);

            Classifier cls = (RandomForest)SerializationHelper.read(modelPath);

            Evaluation eTest = new Evaluation(instances);
            eTest.evaluateModel(cls, instances);
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public BufferedReader testData(String str) {
        byte[] content = str.getBytes();
        InputStream is = null;
        BufferedReader bfReader = null;
        try {
            is = new ByteArrayInputStream(content);
            bfReader = new BufferedReader(new InputStreamReader(is));
            String temp = null;
            while((temp = bfReader.readLine()) != null){
                System.out.println(temp);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                if(is != null) is.close();
            } catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return bfReader;
    }
}
