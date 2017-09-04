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

public class FileUtils
{
    public static FileUtils fileUtils = new FileUtils();

    private static final String RAW_DATA_FILE_NAME = "raw_data.txt";
    public static final String ACCEL_FUNCS_FILE_NAME = "accel_funcs.csv";
    public static final String ACCEL_AND_GYRO_FUNCS_FILE_NAME = "accel_and_gyro_funcs.csv";
    private static final String FOLDER_NAME = "Accelerometer";

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

    private File getDirectoryFile()
    {
        File root = Environment.getExternalStorageDirectory();
        return new File(root.getAbsolutePath() + "/" + FOLDER_NAME);
    }

    public void writeToRawDataFile(String textToWrite)
    {
        File file = getFile(RAW_DATA_FILE_NAME);
        writeToFile(textToWrite, file);
    }

    public void writeToCalculatedDataFile(String textToWrite)
    {
        File dir = getDirectoryFile();
        if (dir.exists())
        {
            if (dir.isDirectory())
            {
                File file = new File(dir, ACCEL_FUNCS_FILE_NAME);
                writeToFile(textToWrite, file);
            } else {
                System.out.println("Not a directory");
            }
        }
        else {
            boolean success = dir.mkdirs();
            if (success)
            {
                File file = new File(dir, ACCEL_FUNCS_FILE_NAME);
                writeToFile(textToWrite, file);
            } else
                System.out.println("Cant make directory");
        }
    }

    public void writeLastData(String textToWrite) {
        File dir = getDirectoryFile();
        File file;
        if (dir.exists())
        {
            if (dir.isDirectory())
            {
                file = new File(dir, ACCEL_FUNCS_FILE_NAME);
                try
                {
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
                    writer.append(textToWrite);
                    writer.append("\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        else {
            boolean success = dir.mkdirs();
            if (success)
            {
                file = new File(dir, ACCEL_FUNCS_FILE_NAME);
                try
                {
                    FileWriter fileWriter = new FileWriter(file, true);
                    BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
                    writer.append(textToWrite);
                    writer.append("\n");
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void writeToFile(String textToWrite, File file)
    {
        if (file == null)
            return;
        try
        {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
            writer.append(textToWrite);
            writer.append(",");
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
        } catch (IOException e)
        {
            e.printStackTrace();
            return "";
        }
    }

    private String convertStreamToString(InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public void clearAllRawData()
    {
        File file = getFile(RAW_DATA_FILE_NAME);
        if (file == null)
            return;

        try
        {
            PrintWriter writer = new PrintWriter(file);
            writer.print("");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void write(String meanX, String meanY, String meanZ, String meanXYZ,
                      String averageGravity,
                      String horizontalAccels, String verticalAccels,
                      String RMS,
                      String variance,
                      String relative,
                      String sma, String horizontalEnergy, String verticalEnergy, String vectorSVM, String dsvm, String dsvmByRMS,
                      String activity, String mobility, String complexity) {
        writeToCalculatedDataFile(meanX);
        writeToCalculatedDataFile(meanY);
        writeToCalculatedDataFile(meanZ);
        writeToCalculatedDataFile(meanXYZ);
        writeToCalculatedDataFile(averageGravity);
        writeToCalculatedDataFile(horizontalAccels);
        writeToCalculatedDataFile(verticalAccels);
        writeToCalculatedDataFile(RMS);
        writeToCalculatedDataFile(variance);
        writeToCalculatedDataFile(relative);
        writeToCalculatedDataFile(sma);
        writeToCalculatedDataFile(horizontalEnergy);
        writeToCalculatedDataFile(verticalEnergy);
        writeToCalculatedDataFile(vectorSVM);
        writeToCalculatedDataFile(dsvm);
        writeToCalculatedDataFile(dsvmByRMS);
        writeToCalculatedDataFile(activity);
        writeToCalculatedDataFile(mobility);
        writeToCalculatedDataFile(complexity);
    }

    public void write(String fourier,
                      String xFFTEnergy, String yFFTEnergy, String zFFTEnergy, String meanFFTEnergy,
                      String xFFTEntropy, String yFFTEntropy, String zFFTEntropy, String meanFFTEntropy,
                      String devX, String devY, String devZ) {
        writeToCalculatedDataFile(fourier);
        writeToCalculatedDataFile(xFFTEnergy);
        writeToCalculatedDataFile(yFFTEnergy);
        writeToCalculatedDataFile(zFFTEnergy);
        writeToCalculatedDataFile(meanFFTEnergy);
        writeToCalculatedDataFile(xFFTEntropy);
        writeToCalculatedDataFile(yFFTEntropy);
        writeToCalculatedDataFile(zFFTEntropy);
        writeToCalculatedDataFile(meanFFTEntropy);
        writeToCalculatedDataFile(devX);
        writeToCalculatedDataFile(devY);
        writeToCalculatedDataFile(devZ);
        writeLastData("vehicle");
    }

    private void writeFeaturesTitle() {
        write("meanX", "meanY", "meanZ", "meanXYZ",
                "averageGravity",
                "horizontalAccels", "verticalAccels",
                "RMS",
                "variance",
                "relative", "sma", "horizontalEnergy", "verticalEnergy", "vectorSVM", "dsvm", "dsvmByRMS",
                "activity", "mobility", "complexity");
    }

    private void writeFourierTitle() {
        write("fourier", "xFFTEnergy", "yFFTEnergy", "zFFTEnergy", "meanFFTEnergy",
                "xFFTEntropy", "yFFTEntropy", "zFFTEntropy", "meanFFTEntropy",
                "devX", "devY", "devZ");
    }

    public void writeTitle() {
        writeFeaturesTitle();
        writeFourierTitle();
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
