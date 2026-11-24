import java.awt.GridLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ListenerDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ListenerDemo::createAndShowGui);
    }

    private static void createAndShowGui() {
        JFrame frame = new JFrame("ActionListener Techniques");
        JLabel message = new JLabel("Choose a listener example.", JLabel.CENTER);
        JPanel buttons = new JPanel(new GridLayout(1, 3, 8, 0));

        JButton anonymousButton = new JButton("Anonymous");
        anonymousButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent event) {
                message.setText("Handled by an anonymous ActionListener.");
            }
        });

        JButton lambdaButton = new JButton("Lambda");
        lambdaButton.addActionListener(event ->
            message.setText("Handled by a lambda ActionListener.")
        );

        JButton methodButton = new JButton("Method reference");
        methodButton.addActionListener(event ->
            showMessage(message, "Handled by a named method.")
        );

        buttons.add(anonymousButton);
        buttons.add(lambdaButton);
        buttons.add(methodButton);
        frame.add(message, java.awt.BorderLayout.NORTH);
        frame.add(buttons, java.awt.BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(520, 140);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void showMessage(JLabel label, String text) {
        label.setText(text);
    }
}
