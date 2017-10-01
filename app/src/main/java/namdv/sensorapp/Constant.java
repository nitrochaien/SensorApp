package namdv.sensorapp;

import android.os.Environment;

import namdv.sensorapp.utils.file.FileUtils;

/**
 * Created by namdv on 9/29/17.
 */

public class Constant
{
    public static final String ACCEL_FUNCS_FILE_NAME = "accel_funcs.csv";
    public static final String RAW_FILE_NAME = "raw_file.csv";
    public static final String ACCEL_FUNCS_FILE_NAME_ARFF = "accel_funcs.arff";
    private static final String FOLDER_NAME = "Accelerometer";
    private static final String MODEL_FOLDER_NAME = "Models";

    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME;
    public static final String ROOT_MODEL = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/" + MODEL_FOLDER_NAME;
    public static final String ARFF_PATH = ROOT + "/" + ACCEL_FUNCS_FILE_NAME_ARFF;
    public static final String CSV_PATH = ROOT + "/" + ACCEL_FUNCS_FILE_NAME;
    public static final String RAW_PATH = ROOT + "/" + RAW_FILE_NAME;
    public static final String MODEL_PATH = ROOT_MODEL + "/random_forest.model";
}
