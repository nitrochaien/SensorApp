package namdv.sensorapp;

import android.os.Environment;

/**
 * Created by namdv on 9/29/17.
 */

public class Constant
{
    public static final String ACCEL_FUNCS_FILE_NAME = "accel_funcs.csv"; //name of vehicle file
    public static final String ACCEL_FUNCS_FILE_NAME_ARFF = "accel_funcs.arff"; //name of vehicle file
    public static final String BIKE_FILE_NAME = "bike.csv"; //name of bike activity file
    public static final String BIKE_FILE_NAME_ARFF = "bike.arff"; //name of bike activity file
    public static final String CAR_FILE_NAME = "car.csv"; //name of car activity file
    public static final String CAR_FILE_NAME_ARFF = "car.arff"; //name of car activity file
    public static final String MOTO_FILE_NAME = "moto.csv"; //name of moto activity file
    public static final String MOTO_FILE_NAME_ARFF = "moto.arff"; //name of moto activity file
    private static final String FOLDER_NAME = "Accelerometer";
    private static final String MODEL_FOLDER_NAME = "Models";

    public static final String ROOT = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME;
    public static final String ROOT_MODEL = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + FOLDER_NAME + "/" + MODEL_FOLDER_NAME;
    public static final String ARFF_PATH = ROOT + "/" + ACCEL_FUNCS_FILE_NAME_ARFF;
    public static final String CSV_PATH = ROOT + "/" + ACCEL_FUNCS_FILE_NAME;
    public static final String BIKE_PATH_CSV = ROOT + "/" + BIKE_FILE_NAME;
    public static final String BIKE_PATH_ARFF = ROOT + "/" + BIKE_FILE_NAME_ARFF;
    public static final String CAR_PATH_CSV = ROOT + "/" + CAR_FILE_NAME;
    public static final String CAR_PATH_ARFF = ROOT + "/" + CAR_FILE_NAME_ARFF;
    public static final String MOTO_PATH_CSV = ROOT + "/" + MOTO_FILE_NAME;
    public static final String MOTO_PATH_ARFF = ROOT + "/" + MOTO_FILE_NAME_ARFF;
    public static final String MODEL_PATH = ROOT_MODEL + "/random_forest.model";
    public static final String BIKE_MODEL_PATH = ROOT_MODEL + "/bike_model.model";
    public static final String CAR_MODEL_PATH = ROOT_MODEL + "/car_model.model";
    public static final String MOTO_MODEL_PATH = ROOT_MODEL + "/moto_model.model";
}
