package namdv.sensorapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by namdv on 5/30/17.
 */

public class FileUtils
{
    public static FileUtils fileUtils = new FileUtils();

    private static final String RAW_DATA_FILE_NAME = "raw_data.txt";
    private static final String CALCULATE_DATA_FILE_NAME = "calculate_data.txt";
    private static final String FOLDER_NAME = "Acclerometer";
    private static final String ACCEL_DATA_FILE_NAME = "accel_data.txt";

    private File getFile(String fileName) {
        File directory = getDirectoryFile();
        if (directory.isDirectory())
            return new File(directory, fileName);

        boolean createdDirectory = directory.mkdirs();
        if (createdDirectory)
        {
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
        File file = getFile(CALCULATE_DATA_FILE_NAME);
        writeToFile(textToWrite, file);
    }

    private void writeToFile(String textToWrite, File file)
    {
        if (file == null)
            return;
        try
        {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
            writer.write(textToWrite);
            writer.write(",");
//            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFunctionResultToFile(String textToWrite) {
        File file = getFile(CALCULATE_DATA_FILE_NAME);
        if (file == null) return;

        try {
            FileWriter fileWriter = new FileWriter(file, true);
            BufferedWriter writer = new BufferedWriter(fileWriter, 1024);
            writer.write(textToWrite);
            writer.write("\t");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getRawData()
    {
        File file = getFile(RAW_DATA_FILE_NAME);
        return getData(file);
    }

    public String getCalculatedData()
    {
        File file = getFile(CALCULATE_DATA_FILE_NAME);
        return getData(file);
    }

    public String getAccelData(Context context) {
        AssetManager asset = context.getAssets();
        try {
            InputStream is = asset.open("accel_data.txt");
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

    private String getData(File file)
    {
        if (file == null)
            return "";

        String value = "";
        try
        {
            InputStream inputStream = new FileInputStream(file);
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            BufferedReader reader = new BufferedReader(streamReader);
            String line;
            do
            {
                line = reader.readLine();
                value += line;
                value += "\n";
            } while (line != null);

            inputStream.close();
            streamReader.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return value;
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
                      String sma, String horizontalEnergy, String vectorSVM, String dsvm,
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
        writeToCalculatedDataFile(vectorSVM);
        writeToCalculatedDataFile(dsvm);
        writeToCalculatedDataFile(activity);
        writeToCalculatedDataFile(mobility);
        writeToCalculatedDataFile(complexity);
    }

    public void write(String fourier,
                      String xFFTEnergy, String yFFTEnergy, String zFFTEnergy,
                      String xFFTEntropy, String yFFTEntropy, String zFFTEntropy,
                      String devX, String devY, String devZ) {
        writeToCalculatedDataFile(fourier);
        writeToCalculatedDataFile(xFFTEnergy);
        writeToCalculatedDataFile(yFFTEnergy);
        writeToCalculatedDataFile(zFFTEnergy);
        writeToCalculatedDataFile(xFFTEntropy);
        writeToCalculatedDataFile(yFFTEntropy);
        writeToCalculatedDataFile(zFFTEntropy);
        writeToCalculatedDataFile(devX);
        writeToCalculatedDataFile(devY);
        writeToCalculatedDataFile(devZ);
    }

    public void writeTitle() {
        write("meanX", "meanY", "meanZ", "meanXYZ",
                "averageGravity",
                "horizontalAccels", "verticalAccels",
                "RMS",
                "variance",
                "relative",
                "sma", "horizontalEnergy", "vectorSVM", "dsvm",
                "activity", "mobility", "complexity");
    }

    public void writeFourier() {

    }
}
