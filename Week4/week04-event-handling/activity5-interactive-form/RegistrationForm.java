import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class RegistrationForm {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(RegistrationForm::createAndShowGui);
    }

    private static void createAndShowGui() {
        JTextField nameField = new JTextField(20);
        JTextField emailField = new JTextField(20);
        JComboBox<String> courseBox = new JComboBox<>(
            new String[]{"Event-Driven Programming", "Java Programming", "Software Engineering"}
        );

        JPanel fields = new JPanel(new GridLayout(3, 2, 8, 8));
        fields.setBorder(BorderFactory.createEmptyBorder(12, 12, 6, 12));
        fields.add(new JLabel("Full name:"));
        fields.add(nameField);
        fields.add(new JLabel("Email:"));
        fields.add(emailField);
        fields.add(new JLabel("Course:"));
        fields.add(courseBox);

        JButton submitButton = new JButton("Register");
        submitButton.addActionListener(event -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            if (name.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please enter both your name and email address.",
                    "Missing information",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            if (!email.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
                JOptionPane.showMessageDialog(
                    null,
                    "Please enter a valid email address.",
                    "Invalid email",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                null,
                "Registration received for " + name + " in " + courseBox.getSelectedItem() + ".",
                "Registration complete",
                JOptionPane.INFORMATION_MESSAGE
            );
        });

        JButton clearButton = new JButton("Clear");
        clearButton.addActionListener(event -> {
            nameField.setText("");
            emailField.setText("");
            courseBox.setSelectedIndex(0);
            nameField.requestFocusInWindow();
        });

        JPanel buttons = new JPanel();
        buttons.add(submitButton);
        buttons.add(clearButton);

        JFrame frame = new JFrame("Course Registration");
        frame.add(fields, BorderLayout.CENTER);
        frame.add(buttons, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
