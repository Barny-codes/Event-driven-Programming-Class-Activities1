# Week 2 Automation Instructions for AI Agent

Please set up the entire Week 2 Observer Pattern project structure and files automatically by executing the following steps in the workspace:

1. Create the following directory structure:
   - `week02-observer-pattern/activity1-weather-station/`
   - `week02-observer-pattern/activity2-legacy-observer/`
   - `week02-observer-pattern/activity3-custom-observer/`
   - `week02-observer-pattern/activity4-comparison/`

2. Inside `week02-observer-pattern/activity1-weather-station/`, create and populate these files:
   - `Observer.java`:
     ```java
     public interface Observer {
         void update(float temperature, float humidity, float pressure);
     }
     ```
   - `Subject.java`:
     ```java
     public interface Subject {
         void registerObserver(Observer o);
         void removeObserver(Observer o);
         void notifyObservers();
     }
     ```
   - `WeatherData.java`:
     ```java
     import java.util.ArrayList;
     import java.util.List;
     public class WeatherData implements Subject {
         private List<Observer> observers = new ArrayList<>();
         private float temperature, humidity, pressure;
         public void registerObserver(Observer o) { observers.add(o); }
         public void removeObserver(Observer o) { observers.remove(o); }
         public void notifyObservers() { for (Observer o : observers) o.update(temperature, humidity, pressure); }
         public void setMeasurements(float t, float h, float p) {
             this.temperature = t; this.humidity = h; this.pressure = p;
             notifyObservers();
         }
     }
     ```
   - `CurrentConditionsDisplay.java`:
     ```java
     public class CurrentConditionsDisplay implements Observer {
         public void update(float t, float h, float p) { System.out.println("Temp: " + t + "C, Humidity: " + h + "%"); }
     }
     ```
   - `StatisticsDisplay.java`:
     ```java
     public class StatisticsDisplay implements Observer {
         public void update(float t, float h, float p) { System.out.println("Stats Temp: " + t); }
     }
     ```
   - `ForecastDisplay.java`:
     ```java
     public class ForecastDisplay implements Observer {
         public void update(float t, float h, float p) { System.out.println("Forecast updated."); }
     }
     ```
   - `WeatherStationMain.java`:
     ```java
     public class WeatherStationMain {
         public static void main(String[] args) {
             WeatherData wd = new WeatherData();
             wd.registerObserver(new CurrentConditionsDisplay());
             wd.setMeasurements(25.0f, 65.0f, 1013.0f);
         }
     }
     ```

3. Inside `week02-observer-pattern/activity3-custom-observer/`, create and populate:
   - `DataObserver.java`:
     ```java
     public interface DataObserver { void onDataChanged(int newValue); }
     ```
   - `DataSubject.java`:
     ```java
     public interface DataSubject { void addObserver(DataObserver o); void removeObserver(DataObserver o); void notifyObservers(); }
     ```
   - `DataModel.java`:
     ```java
     import java.util.*;
     public class DataModel implements DataSubject {
         private List<DataObserver> observers = new ArrayList<>();
         private int value;
         public void addObserver(DataObserver o) { observers.add(o); }
         public void removeObserver(DataObserver o) { observers.remove(o); }
         public void notifyObservers() { for (DataObserver o : observers) o.onDataChanged(value); }
         public void setValue(int v) { this.value = v; notifyObservers(); }
     }
     ```
   - `LabelDisplay.java`:
     ```java
     public class LabelDisplay implements DataObserver { public void onDataChanged(int v) { System.out.println("Label shows: " + v); } }
     ```
   - `BarChartDisplay.java`:
     ```java
     public class BarChartDisplay implements DataObserver { public void onDataChanged(int v) { System.out.println("Bar chart height: " + v); } }
     ```
   - `TextLogger.java`:
     ```java
     public class TextLogger implements DataObserver { public void onDataChanged(int v) { System.out.println("LOG: " + v); } }
     ```
   - `RefactoredMain.java`:
     ```java
     public class RefactoredMain {
         public static void main(String[] args) {
             DataModel model = new DataModel();
             model.addObserver(new LabelDisplay());
             model.setValue(10);
         }
     }
     ```