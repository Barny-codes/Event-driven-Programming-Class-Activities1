import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class LiveSearchDemo {
    private static final List<String> ITEMS = Arrays.asList(
        "ActionListener", "Button", "DocumentListener", "EventObject",
        "ItemListener", "JFrame", "JLabel", "JPanel", "JTextField",
        "MouseListener", "SwingUtilities"
    );

    public static void main(String[] args) {
        SwingUtilities.invokeLater(LiveSearchDemo::createAndShowGui);
    }

    private static void createAndShowGui() {
        JTextField searchField = new JTextField(22);
        DefaultListModel<String> resultsModel = new DefaultListModel<>();
        JList<String> results = new JList<>(resultsModel);
        updateResults("", resultsModel);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            private void search() {
                updateResults(searchField.getText(), resultsModel);
            }

            @Override
            public void insertUpdate(DocumentEvent event) {
                search();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                search();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                search();
            }
        });

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search:"));
        searchPanel.add(searchField);

        JFrame frame = new JFrame("Live Search Demo");
        frame.add(searchPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(results), BorderLayout.CENTER);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(360, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void updateResults(String query, DefaultListModel<String> model) {
        String normalizedQuery = query.trim().toLowerCase(java.util.Locale.ROOT);
        List<String> matches = new ArrayList<>();
        for (String item : ITEMS) {
            if (item.toLowerCase(java.util.Locale.ROOT).contains(normalizedQuery)) {
                matches.add(item);
            }
        }

        model.clear();
        for (String match : matches) {
            model.addElement(match);
        }
    }
}
