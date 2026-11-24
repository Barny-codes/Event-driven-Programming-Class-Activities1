import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class FirstButton {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FirstButton::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("First ActionListener");
        JButton button = new JButton("Click me");
        JLabel message = new JLabel("The button has not been clicked yet.");

        button.addActionListener(event -> message.setText("Button clicked!"));

        frame.setLayout(new FlowLayout(FlowLayout.CENTER, 12, 20));
        frame.add(button);
        frame.add(message);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
