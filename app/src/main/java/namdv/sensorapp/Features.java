package namdv.sensorapp;

/**
 * Created by namdv on 10/1/17.
 */

public class Features
{
    public String meanX;
    public String meanY;
    public String meanZ;
    public String meanXYZ;
    public String variance;
    public String averageGravity;
    public String horizontalAccels;
    public String verticalAccels;
    public String RMS;
    public String relative;
    public String sma;
    public String horizontalEnergy;
    public String verticalEnergy;
    public String vectorSVM;
    public String dsvm;
    public String dsvmByRMS;
    public String activity;
    public String mobility;
    public String complexity;
    public String fourier;
    public String xFFTEnergy;
    public String yFFTEnergy;
    public String zFFTEnergy;
    public String meanFFTEnergy;
    public String xFFTEntropy;
    public String yFFTEntropy;
    public String zFFTEntropy;
    public String meanFFTEntropy;
    public String devX;
    public String devY;
    public String devZ;

    public String sumUp() {
        return meanX + "," + meanY + "," + meanZ + "," + meanXYZ + "," + variance + "," +
                averageGravity + "," + horizontalAccels + "," + verticalAccels + "," +
                RMS + "," + relative + "," + sma + "," + horizontalEnergy + "," +
                verticalEnergy + "," + vectorSVM + "," + dsvm + "," + dsvmByRMS + "," + activity + "," +
                mobility + "," + complexity + "," + fourier + "," + xFFTEnergy + "," + yFFTEnergy + "," +
                zFFTEnergy + "," + meanFFTEnergy + "," + xFFTEntropy + "," + yFFTEntropy + "," + zFFTEntropy + "," +
                meanFFTEntropy + "," + devX + "," + devY + "," + devZ;
    }
}
