import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ItemListenerDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ItemListenerDemo::createAndShowGui);
    }

    private static void createAndShowGui() {
        String[] colors = {"Red", "Green", "Blue", "Yellow"};
        JComboBox<String> colorPicker = new JComboBox<>(colors);
        JLabel selection = new JLabel("Selected: Red");

        colorPicker.addItemListener(event -> {
            if (event.getStateChange() == ItemEvent.SELECTED) {
                selection.setText("Selected: " + event.getItem());
            }
        });

        JFrame frame = new JFrame("ItemListener Demo");
        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 20));
        frame.add(new JLabel("Choose a color:"));
        frame.add(colorPicker);
        frame.add(selection);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
