public class CurrentConditionsDisplay implements Observer {
    public void update(float t, float h, float p) { System.out.println("Temp: " + t + "C, Humidity: " + h + "%"); }
}
