package namdv.sensorapp.Utils.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

import namdv.sensorapp.Utils.data.WindowData;

/**
 * Created by namdv on 5/30/17.
 */

public class FileUtils {
    public static FileUtils fileUtils = new FileUtils();

    private static final String RAW_DATA_FILE_NAME = "raw_data.txt";
    public static final String ACCEL_FUNCS_FILE_NAME = "accel_funcs.csv";
    public static final String ACCEL_AND_GYRO_FUNCS_FILE_NAME = "accel_and_gyro_funcs.csv";
    public static final String ACCEL_FUNCS_FILE_NAME_ARFF = "accel_funcs.arff";
    public static final String ACCEL_AND_GYRO_FUNCS_FILE_NAME_ARFF = "accel_and_gyro_funcs.arff";
    public static final String FOLDER_NAME = "Accelerometer";

    private File getFile(String fileName) {
        File directory = getDirectoryFile();
        if (!directory.exists()) {
            if (directory.isDirectory())
                return new File(directory, fileName);
        }

        boolean createdDirectory = directory.mkdirs();
        if (createdDirectory) {
            return new File(directory, fileName);
        }
        return null;
    }

    private File getDirectoryFile() {
        File root = Environment.getExternalStorageDirectory();
        return new File(root.getAbsolutePath() + "/" + FOLDER_NAME);
    }

    public void writeToRawDataFile(String textToWrite) {
        File file = getFile(RAW_DATA_FILE_NAME);
        fileWriter(textToWrite, file, false);
    }

    private void writeToFile(String text, String fileName, boolean isLast) {
        File dir = getDirectoryFile();
        if (dir.exists()) {
            if (dir.isDirectory()) {
                File file = new File(dir, fileName);
                fileWriter(text, file, isLast);
            } else {
                System.out.println("Not a directory");
            }
        }
        else {
            boolean success = dir.mkdirs();
            if (success) {
                File file = new File(dir, fileName);
                fileWriter(text, file, isLast);
            } else
                System.out.println("Cant make directory");
        }
    }

    public void writeToCalculatedDataFile(String textToWrite) {
        writeToFile(textToWrite, ACCEL_FUNCS_FILE_NAME, false);
    }

    public void writeLastData(String textToWrite) {
        writeToFile(textToWrite, ACCEL_FUNCS_FILE_NAME, true);
    }

    private void fileWriter(String textToWrite, File file, boolean isLast) {
        if (file == null) return;
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
            writer.append(textToWrite);
            if (isLast) writer.append("\n");
            else writer.append(",");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getAccelDataInFolder(Context context, String folderName, String fileName) {
        AssetManager asset = context.getAssets();
        try {
            InputStream is = asset.open(folderName + "/" + fileName);
            return convertStreamToString(is);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void clearAllRawData() {
        File file = getFile(RAW_DATA_FILE_NAME);
        if (file == null)
            return;

        try {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFeaturesTitle() {
        writeToCalculatedDataFile("averageGravity");
        writeToCalculatedDataFile("horizontalAccels");
        writeToCalculatedDataFile("verticalAccels");
        writeToCalculatedDataFile("RMS");
        writeToCalculatedDataFile("relative");
        writeToCalculatedDataFile("sma");
        writeToCalculatedDataFile("horizontalEnergy");
        writeToCalculatedDataFile("verticalEnergy");
        writeToCalculatedDataFile("vectorSVM");
        writeToCalculatedDataFile("dsvm");
        writeToCalculatedDataFile("dsvmByRMS");
        writeToCalculatedDataFile("activity");
        writeToCalculatedDataFile("mobility");
        writeToCalculatedDataFile("complexity");
    }

    private void writeFourierTitle() {
        writeToCalculatedDataFile("fourier");
        writeToCalculatedDataFile("xFFTEnergy");
        writeToCalculatedDataFile("yFFTEnergy");
        writeToCalculatedDataFile("zFFTEnergy");
        writeToCalculatedDataFile("meanFFTEnergy");
        writeToCalculatedDataFile("xFFTEntropy");
        writeToCalculatedDataFile("yFFTEntropy");
        writeToCalculatedDataFile("zFFTEntropy");
        writeToCalculatedDataFile("meanFFTEntropy");
        writeToCalculatedDataFile("devX");
        writeToCalculatedDataFile("devY");
        writeToCalculatedDataFile("devZ");

        //vehicle
        writeLastData("vehicle");
    }

    public void writeAccelTitle() {
        writeFeaturesTitle();
        writeFourierTitle();
    }

    public void writeAllTitles() {
        writeToCalculatedDataFile("meanX");
        writeToCalculatedDataFile("meanY");
        writeToCalculatedDataFile("meanZ");
        writeToCalculatedDataFile("meanXYZ");
        writeToCalculatedDataFile("variance");

        writeAccelTitle();
    }

    public String getAssetFilePath(Context context, String fileName) {
        File f = new File(context.getCacheDir() + fileName);
        if (!f.exists()) try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            FileOutputStream fos = new FileOutputStream(f);
            fos.write(buffer);
            fos.close();
        } catch (Exception e) { throw new RuntimeException(e); }

        return f.getPath();
    }
}
