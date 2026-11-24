import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DocumentListenerDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DocumentListenerDemo::createAndShowGui);
    }

    private static void createAndShowGui() {
        JTextField input = new JTextField(20);
        JLabel characterCount = new JLabel("Characters: 0");

        input.getDocument().addDocumentListener(new DocumentListener() {
            private void updateCount() {
                characterCount.setText(
                    "Characters: " + input.getDocument().getLength()
                );
            }

            @Override
            public void insertUpdate(DocumentEvent event) {
                updateCount();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                updateCount();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                updateCount();
            }
        });

        JPanel entry = new JPanel(new FlowLayout(FlowLayout.LEFT));
        entry.add(new JLabel("Type here:"));
        entry.add(input);

        JFrame frame = new JFrame("DocumentListener Demo");
        frame.add(entry, BorderLayout.CENTER);
        frame.add(characterCount, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
