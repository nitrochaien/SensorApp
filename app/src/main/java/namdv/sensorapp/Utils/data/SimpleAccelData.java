/**
 * PreprocessAndComputeFeatures Project
 * Created by
 * Phan Doan Cuong
 * Android Dev
 * VMGames Company
 * cuongphank58@gmail.com
 * 0989906612
 * on 01/03/2017.
 */
package namdv.sensorapp.Utils.data;

public class SimpleAccelData {
    private float timestamp;
    private double x;
    private double y;
    private double z;

    public SimpleAccelData(String timestamp, String x, String y, String z) {
        this.timestamp = Float.parseFloat(timestamp);
        this.x = Double.parseDouble(x.replace(',', '.'));
        this.y = Double.parseDouble(y.replace(',', '.'));
        this.z = Double.parseDouble(z.replace(',', '.'));
    }
    public SimpleAccelData(float timestamp, Double x, Double y, Double z) {
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
        this.z = z;
    }
   
    public float getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(float timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getZ() {
        return z;
    }

    public void setZ(double z) {
        this.z = z;
    }
}
