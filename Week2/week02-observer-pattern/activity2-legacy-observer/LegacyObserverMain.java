public class LegacyObserverMain {
    public static void main(String[] args) {
        LegacyWeatherData weatherData = new LegacyWeatherData();
        weatherData.addObserver(new LegacyCurrentConditionsDisplay());
        weatherData.setMeasurements(25.0f, 65.0f, 1013.0f);
    }
}
