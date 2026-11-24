import java.util.*;
public class DataModel implements DataSubject {
    private List<DataObserver> observers = new ArrayList<>();
    private int value;
    public void addObserver(DataObserver o) { observers.add(o); }
    public void removeObserver(DataObserver o) { observers.remove(o); }
    public void notifyObservers() { for (DataObserver o : observers) o.onDataChanged(value); }
    public void setValue(int v) { this.value = v; notifyObservers(); }
}
