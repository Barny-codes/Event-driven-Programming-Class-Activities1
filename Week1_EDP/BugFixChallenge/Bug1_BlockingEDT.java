import javax.swing.*;

public class Bug1_BlockingEDT extends JFrame {
    private JLabel statusLabel;
    private JButton processButton;

    public Bug1_BlockingEDT() {
        setTitle("Bug 1 - Blocking EDT (Fixed)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new java.awt.FlowLayout());

        statusLabel = new JLabel("Ready");
        processButton = new JButton("Process");

        processButton.addActionListener(e -> {
            // Update UI immediately to show progress
            statusLabel.setText("Processing...");
            processButton.setEnabled(false);

            // Offload long-running task to a background thread
            new Thread(() -> {
                try {
                    Thread.sleep(5000); // 5-second task
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                // Safely update the GUI on the EDT once the task completes
                SwingUtilities.invokeLater(() -> {
                    statusLabel.setText("Done!");
                    processButton.setEnabled(true);
                });
            }).start();
        });

        add(statusLabel);
        add(processButton);
        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Bug1_BlockingEDT().setVisible(true);
        });
    }
}