package namdv.sensorapp.Utils.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by namdv on 5/30/17.
 */

public class FileUtils {
    public static FileUtils fileUtils = new FileUtils();

    public static final String ACCEL_FUNCS_FILE_NAME = "accel_funcs.csv";
    public static final String RAW_FILE_NAME = "raw_file.csv";
    public static final String ACCEL_AND_GYRO_FUNCS_FILE_NAME = "accel_and_gyro_funcs.csv";
    public static final String ACCEL_FUNCS_FILE_NAME_ARFF = "accel_funcs.arff";
    public static final String ACCEL_AND_GYRO_FUNCS_FILE_NAME_ARFF = "accel_and_gyro_funcs.arff";
    public static final String FOLDER_NAME = "Accelerometer";

    private File getDirectoryFile() {
        File root = Environment.getExternalStorageDirectory();
        return new File(root.getAbsolutePath() + "/" + FOLDER_NAME);
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

    public void writeToRawFile(String textToWrite) {
        try {
            File dir = getDirectoryFile();
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    File file = new File(dir, RAW_FILE_NAME);
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
                    writer.append(textToWrite);
                    writer.close();
                } else {
                    System.out.println("Not a directory");
                }
            }
            else {
                boolean success = dir.mkdirs();
                if (success) {
                    File file = new File(dir, RAW_FILE_NAME);
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
                    writer.append(textToWrite);
                    writer.close();
                } else
                    System.out.println("Cant make directory");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public String getRawData() {
        File root = Environment.getExternalStorageDirectory();
        String filePath = root.getAbsolutePath() + "/" + FileUtils.FOLDER_NAME + "/" + FileUtils.RAW_FILE_NAME;

        String value = "";
        File myFile = new File(filePath);
        try {
            FileInputStream fis = new FileInputStream(myFile);
            byte[] dataArray = new byte[fis.available()];
            while (fis.read(dataArray) != -1) {
                value = new String(dataArray);
            }
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
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
}
