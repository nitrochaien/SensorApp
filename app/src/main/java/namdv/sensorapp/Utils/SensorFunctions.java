package namdv.sensorapp.Utils;

/**
 * Created by namdv on 7/15/17.
 */

public class SensorFunctions {
    public static FrequencyStatistic frequency = new FrequencyStatistic();
    public static HjorthStatistics hjorth = new HjorthStatistics();
    public static MeanStatistic mean = new MeanStatistic();
    public static NumericalIntegration numerical = new NumericalIntegration();
    public static RMSFeature rms = new RMSFeature();
    public static SMAStatistic sma = new SMAStatistic();
    public static VarianceStatistic variance = new VarianceStatistic();
}
