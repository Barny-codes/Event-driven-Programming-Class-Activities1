import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class ContactListDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Contact List - Custom Renderer");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(400, 400);
            frame.setLocationRelativeTo(null);

            String[] contacts = {
                "Alice - Online",
                "Bob - Offline",
                "Charlie - Online",
                "Diana - Away",
                "Edward - Offline"
            };

            JList<String> contactList = new JList<>(contacts);
            contactList.setFont(new Font("Arial", Font.PLAIN, 14));
            contactList.setVisibleRowCount(10);
            contactList.setCellRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(
                        JList<?> list,
                        Object value,
                        int index,
                        boolean isSelected,
                        boolean cellHasFocus) {
                    javax.swing.JLabel label = (javax.swing.JLabel)
                            super.getListCellRendererComponent(
                                    list, value, index, isSelected, cellHasFocus);
                    String contact = String.valueOf(value);
                    Color statusColor = contact.contains("Online")
                            ? Color.GREEN
                            : contact.contains("Away") ? Color.ORANGE : Color.GRAY;
                    label.setIcon(new ColorDot(statusColor));
                    label.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
                    return label;
                }
            });

            JTextArea info = new JTextArea(3, 30);
            info.setEditable(false);
            info.setLineWrap(true);
            info.setWrapStyleWord(true);
            contactList.addListSelectionListener(event -> {
                if (!event.getValueIsAdjusting()) {
                    String selected = contactList.getSelectedValue();
                    info.setText(selected == null ? "" : "Selected: " + selected);
                }
            });

            frame.add(new JScrollPane(contactList), BorderLayout.CENTER);
            frame.add(new JScrollPane(info), BorderLayout.SOUTH);
            frame.setVisible(true);
        });
    }

    private static final class ColorDot implements Icon {
        private final Color color;

        private ColorDot(Color color) {
            this.color = color;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            graphics.setColor(color);
            graphics.fillOval(x, y, 12, 12);
        }

        @Override
        public int getIconWidth() {
            return 14;
        }

        @Override
        public int getIconHeight() {
            return 14;
        }
    }
}
