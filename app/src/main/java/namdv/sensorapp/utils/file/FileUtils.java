package namdv.sensorapp.utils.file;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
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
        return new File(Constant.ROOT).exists();
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

    public void writeToRawFile(String textToWrite) {
        try {
            File dir = new File(Constant.ROOT);
            if (dir.exists()) {
                if (dir.isDirectory()) {
                    File file = new File(dir, Constant.RAW_FILE_NAME);
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
                    File file = new File(dir, Constant.RAW_FILE_NAME);
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
        writeToFile(textToWrite, Constant.ACCEL_FUNCS_FILE_NAME, true);
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
        String filePath = Constant.RAW_PATH;

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

    public void writeAllTitles() {
        writeToCalculatedDataFile("meanX");
        writeToCalculatedDataFile("meanY");
        writeToCalculatedDataFile("meanZ");
        writeToCalculatedDataFile("meanXYZ");
        writeToCalculatedDataFile("variance");
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

        writeLastData("vehicle");
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
