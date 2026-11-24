public class WeatherStationMain {
    public static void main(String[] args) {
        WeatherData wd = new WeatherData();
        wd.registerObserver(new CurrentConditionsDisplay());
        wd.setMeasurements(25.0f, 65.0f, 1013.0f);
    }
}
