import java.util.Observable;
import java.util.Observer;

@SuppressWarnings("deprecation")
public class LegacyCurrentConditionsDisplay implements Observer {
    @Override
    public void update(Observable subject, Object argument) {
        if (subject instanceof LegacyWeatherData) {
            LegacyWeatherData weatherData = (LegacyWeatherData) subject;
            System.out.println(
                "Temp: " + weatherData.getTemperature()
                    + "C, Humidity: " + weatherData.getHumidity() + "%"
            );
        }
    }
}
