import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

public class StudentTableDemo {
    private static int nextStudentId = 107;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StudentTableDemo::createAndShow);
    }

    private static void createAndShow() {
        JFrame frame = new JFrame("Student Database - JTable");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(700, 500);
        frame.setLocationRelativeTo(null);

        String[] columns = {"ID", "Name", "Age", "Grade", "GPA"};
        Object[][] data = {
            {101, "Alice Johnson", 20, "A", 3.85},
            {102, "Bob Smith", 22, "B", 3.20},
            {103, "Charlie Brown", 19, "A-", 3.70},
            {104, "Diana Prince", 21, "B+", 3.40},
            {105, "Edward Norton", 23, "C", 2.50},
            {106, "Fiona Green", 20, "A", 3.95}
        };

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1 || column == 3;
            }
        };

        JTable table = new JTable(model);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 14));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable source,
                    Object value,
                    boolean isSelected,
                    boolean hasFocus,
                    int row,
                    int column) {
                Component cell = super.getTableCellRendererComponent(
                        source, value, isSelected, hasFocus, row, column);
                if (!isSelected && value instanceof Number
                        && ((Number) value).doubleValue() < 3.0) {
                    cell.setForeground(Color.RED);
                } else if (!isSelected) {
                    cell.setForeground(source.getForeground());
                }
                return cell;
            }
        });

        JButton addButton = new JButton("Add Student");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear All");
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        addButton.addActionListener(event -> addStudent(frame, model));
        deleteButton.addActionListener(event -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow < 0) {
                JOptionPane.showMessageDialog(
                        frame, "Please select a row to delete.",
                        "No Selection", JOptionPane.WARNING_MESSAGE);
                return;
            }
            int modelRow = table.convertRowIndexToModel(selectedRow);
            int confirmation = JOptionPane.showConfirmDialog(
                    frame, "Delete the selected student?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                model.removeRow(modelRow);
            }
        });
        clearButton.addActionListener(event -> {
            int confirmation = JOptionPane.showConfirmDialog(
                    frame, "Clear all students?", "Confirm",
                    JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                model.setRowCount(0);
            }
        });

        frame.add(new JScrollPane(table), BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    private static void addStudent(JFrame frame, DefaultTableModel model) {
        String name = JOptionPane.showInputDialog(frame, "Enter student name:");
        if (name == null) {
            return;
        }
        name = name.trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame, "Student name cannot be empty.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ageText = JOptionPane.showInputDialog(frame, "Enter age:");
        if (ageText == null) {
            return;
        }
        String grade = JOptionPane.showInputDialog(frame, "Enter grade (e.g., A, B+):");
        if (grade == null) {
            return;
        }
        grade = grade.trim();
        if (grade.isEmpty()) {
            JOptionPane.showMessageDialog(
                    frame, "Grade cannot be empty.",
                    "Input Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String gpaText = JOptionPane.showInputDialog(frame, "Enter GPA (0.0 to 4.0):");
        if (gpaText == null) {
            return;
        }

        try {
            int age = Integer.parseInt(ageText.trim());
            double gpa = Double.parseDouble(gpaText.trim());
            if (age < 1 || gpa < 0.0 || gpa > 4.0) {
                throw new NumberFormatException("Age or GPA is outside the valid range.");
            }
            model.addRow(new Object[] {nextStudentId++, name, age, grade, gpa});
        } catch (NumberFormatException exception) {
            JOptionPane.showMessageDialog(
                    frame,
                    "Enter a positive whole-number age and a GPA from 0.0 to 4.0.",
                    "Input Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
