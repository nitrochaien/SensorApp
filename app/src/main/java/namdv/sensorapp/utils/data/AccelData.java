package namdv.sensorapp.utils.data;

/**
 * Created by namdv on 9/3/17.
 */

public class AccelData
{
    public String vehicle;
    public String status;
    public WindowData data;

    public String getStatus() {
        String[] split = status.split(":");
        String info = split[1].trim();
        if (info.equals("Dec")) info = "Mov";
        return info;
    }

    public String getVehicle() {
        String[] split = vehicle.split(":");
        return split[1].trim();
    }
}