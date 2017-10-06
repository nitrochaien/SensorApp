package namdv.sensorapp.utils.file;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import namdv.sensorapp.Constant;

/**
 * Created by namdv on 5/30/17.
 */

public class FileUtils {
    public static FileUtils fileUtils = new FileUtils();

    public boolean createdModel() {
        return new File(Constant.ROOT_MODEL).exists();
    }

    public void createModelFolder() {
        File f = new File(Constant.ROOT_MODEL);
        if (f.exists() || f.isDirectory())
            return;
        f.mkdirs();
    }

    private void writeToFile(String text, String fileName, boolean isLast) {
        File dir = new File(Constant.ROOT);
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
        writeToFile(textToWrite, Constant.ACCEL_FUNCS_FILE_NAME, false);
    }

    public void writeLastData(String textToWrite) {
        writeToFile(textToWrite, Constant.ACCEL_FUNCS_FILE_NAME, true);
    }

    public void writeToBikeFile(String textToWrite) {
        writeToFile(textToWrite, Constant.BIKE_FILE_NAME, false);
    }

    public void writeLastDataBikeFile(String textToWrite) {
        writeToFile(textToWrite, Constant.BIKE_FILE_NAME, true);
    }

    public void writeToCarFile(String textToWrite) {
        writeToFile(textToWrite, Constant.CAR_FILE_NAME, false);
    }

    public void writeLastDataCarFile(String textToWrite) {
        writeToFile(textToWrite, Constant.CAR_FILE_NAME, true);
    }

    public void writeToMotoFile(String textToWrite) {
        writeToFile(textToWrite, Constant.MOTO_FILE_NAME, false);
    }

    public void writeLastDataMotoFile(String textToWrite) {
        writeToFile(textToWrite, Constant.MOTO_FILE_NAME, true);
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

    public void writeAccelTitles() {
        writeToCalculatedDataFile("meanX" + "," + "meanY" + "," + "meanZ" + "," + "meanXYZ" + "," +
                "variance" + "," + "averageGravity" + "," + "horizontalAccels" + "," + "verticalAccels" + "," +
                "RMS" + "," + "relative" + "," + "sma" + "," + "horizontalEnergy" + "," + "verticalEnergy" + "," +
                "vectorSVM" + "," + "dsvm" + "," + "dsvmByRMS" + "," + "activity" + "," + "mobility" + "," + "complexity" + "," +
                "fourier" + "," + "xFFTEnergy" + "," + "yFFTEnergy" + "," + "zFFTEnergy" + "," + "meanFFTEnergy" + "," +
                "xFFTEntropy" + "," + "yFFTEntropy" + "," + "zFFTEntropy" + "," + "meanFFTEntropy" + "," + "devX" + "," +
                "devY" + "," + "devZ");

        writeLastData("vehicle");
    }

    public void writeBikeTitles() {
        writeToBikeFile("meanX" + "," + "meanY" + "," + "meanZ" + "," + "meanXYZ" + "," +
                "variance" + "," + "averageGravity" + "," + "horizontalAccels" + "," + "verticalAccels" + "," +
                "RMS" + "," + "relative" + "," + "sma" + "," + "horizontalEnergy" + "," + "verticalEnergy" + "," +
                "vectorSVM" + "," + "dsvm" + "," + "dsvmByRMS" + "," + "activity" + "," + "mobility" + "," + "complexity" + "," +
                "fourier" + "," + "xFFTEnergy" + "," + "yFFTEnergy" + "," + "zFFTEnergy" + "," + "meanFFTEnergy" + "," +
                "xFFTEntropy" + "," + "yFFTEntropy" + "," + "zFFTEntropy" + "," + "meanFFTEntropy" + "," + "devX" + "," +
                "devY" + "," + "devZ");

        writeLastDataBikeFile("vehicle" + ",status");
    }

    public void writeCarTitles() {
        writeToCarFile("meanX" + "," + "meanY" + "," + "meanZ" + "," + "meanXYZ" + "," +
                "variance" + "," + "averageGravity" + "," + "horizontalAccels" + "," + "verticalAccels" + "," +
                "RMS" + "," + "relative" + "," + "sma" + "," + "horizontalEnergy" + "," + "verticalEnergy" + "," +
                "vectorSVM" + "," + "dsvm" + "," + "dsvmByRMS" + "," + "activity" + "," + "mobility" + "," + "complexity" + "," +
                "fourier" + "," + "xFFTEnergy" + "," + "yFFTEnergy" + "," + "zFFTEnergy" + "," + "meanFFTEnergy" + "," +
                "xFFTEntropy" + "," + "yFFTEntropy" + "," + "zFFTEntropy" + "," + "meanFFTEntropy" + "," + "devX" + "," +
                "devY" + "," + "devZ");

        writeLastDataCarFile("vehicle" + ",status");
    }

    public void writeMotoTitles() {
        writeToMotoFile("meanX" + "," + "meanY" + "," + "meanZ" + "," + "meanXYZ" + "," +
                "variance" + "," + "averageGravity" + "," + "horizontalAccels" + "," + "verticalAccels" + "," +
                "RMS" + "," + "relative" + "," + "sma" + "," + "horizontalEnergy" + "," + "verticalEnergy" + "," +
                "vectorSVM" + "," + "dsvm" + "," + "dsvmByRMS" + "," + "activity" + "," + "mobility" + "," + "complexity" + "," +
                "fourier" + "," + "xFFTEnergy" + "," + "yFFTEnergy" + "," + "zFFTEnergy" + "," + "meanFFTEnergy" + "," +
                "xFFTEntropy" + "," + "yFFTEntropy" + "," + "zFFTEntropy" + "," + "meanFFTEntropy" + "," + "devX" + "," +
                "devY" + "," + "devZ");

        writeLastDataMotoFile("vehicle" + ",status");
    }

    public String getMyModelPath(Context context) {
        File f = new File(context.getCacheDir() + "/my_model.model");
        if (!f.exists()) try {
            InputStream is = context.getAssets().open("my_model.model");
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
