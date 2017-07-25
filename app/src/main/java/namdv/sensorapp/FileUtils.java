package namdv.sensorapp;

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

/**
 * Created by namdv on 5/30/17.
 */

public class FileUtils
{
    private static final String RAW_DATA_FILE_NAME = "raw_data.txt";
    private static final String CALCULATE_DATA_TXT = "calculate_data.txt";
    private static final String FOLDER_NAME = "Acclerometer";

    public File getRawDataFile()
    {
        File directory = getDirectoryFile();
        if (directory.isDirectory())
            return new File(directory, RAW_DATA_FILE_NAME);

        boolean createdDirectory = directory.mkdirs();
        if (createdDirectory)
        {
            return new File(directory, RAW_DATA_FILE_NAME);
        }
        return null;
    }

    public File getSampleRawDataFile() {
        return null;
    }

    public File getCalculatedDataFile()
    {
        File directory = getDirectoryFile();
        if (directory.isDirectory())
            return new File(directory, CALCULATE_DATA_TXT);

        boolean createdDirectory = directory.mkdirs();
        if (createdDirectory)
        {
            return new File(directory, CALCULATE_DATA_TXT);
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
        File file = getRawDataFile();
        writeToFile(textToWrite, file);
    }

    public void writeToCalculatedDataFile(String textToWrite)
    {
        File file = getCalculatedDataFile();
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
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeFunctionResultToFile(String textToWrite) {
        File file = getCalculatedDataFile();
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
        File file = getRawDataFile();
        return getData(file);
    }

    public String getCalculatedData()
    {
        File file = getCalculatedDataFile();
        return getData(file);
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
        File file = getRawDataFile();
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
}
