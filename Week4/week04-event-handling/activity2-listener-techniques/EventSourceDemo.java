import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.EventListener;
import java.util.EventObject;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class EventSourceDemo {
    private final EventSource source = new EventSource();
    private final JLabel status = new JLabel("No custom event received.", JLabel.CENTER);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new EventSourceDemo().createAndShowGui());
    }

    private void createAndShowGui() {
        source.addEventListener(event ->
            status.setText("Received custom event: " + event.getMessage())
        );

        JButton firstButton = new JButton("Send first event");
        firstButton.addActionListener(event -> source.fireEvent("First button clicked"));

        JButton secondButton = new JButton("Send second event");
        secondButton.addActionListener(event -> source.fireEvent("Second button clicked"));

        JPanel buttons = new JPanel(new FlowLayout());
        buttons.add(firstButton);
        buttons.add(secondButton);

        JFrame frame = new JFrame("Custom Event Source");
        frame.add(status, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(440, 140);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    @FunctionalInterface
    private interface CustomEventListener extends EventListener {
        void onCustomEvent(CustomEvent event);
    }

    private static final class CustomEvent extends EventObject {
        private static final long serialVersionUID = 1L;
        private final String message;

        private CustomEvent(Object source, String message) {
            super(source);
            this.message = message;
        }

        private String getMessage() {
            return message;
        }
    }

    private static final class EventSource {
        private final java.util.List<CustomEventListener> listeners =
            new java.util.ArrayList<>();

        private void addEventListener(CustomEventListener listener) {
            listeners.add(listener);
        }

        private void fireEvent(String message) {
            CustomEvent event = new CustomEvent(this, message);
            for (CustomEventListener listener : new java.util.ArrayList<>(listeners)) {
                listener.onCustomEvent(event);
            }
        }
    }
}
