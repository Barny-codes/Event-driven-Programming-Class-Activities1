import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class PropertyChangeDemo {
    private final PropertyChangeSupport propertyChanges =
        new PropertyChangeSupport(this);
    private int value;

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChanges.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChanges.removePropertyChangeListener(listener);
    }

    public void setValue(int newValue) {
        int oldValue = value;
        value = newValue;
        propertyChanges.firePropertyChange("value", oldValue, newValue);
    }

    public static void main(String[] args) {
        PropertyChangeDemo demo = new PropertyChangeDemo();
        demo.addPropertyChangeListener(event ->
            System.out.println(
                event.getPropertyName() + " changed from "
                    + event.getOldValue() + " to " + event.getNewValue()
            )
        );

        demo.setValue(10);
        demo.setValue(20);
    }
}
