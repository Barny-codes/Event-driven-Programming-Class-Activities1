import javax.swing.*;
import java.awt.*;

public class Bug2_MissingListener extends JFrame {
    private JLabel countLabel;
    private JButton incrementButton;
    private int count = 0;

    public Bug2_MissingListener() {
        setTitle("Bug 2 - Missing Listener (Fixed)");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new FlowLayout());

        countLabel = new JLabel("Count: 0");
        countLabel.setFont(new Font("Arial", Font.BOLD, 20));

        incrementButton = new JButton("Increment");

        // NEW CHANGE: Added ActionListener to update the counter and UI label on click
        incrementButton.addActionListener(e -> {
            count++;
            countLabel.setText("Count: " + count);
        });

        add(countLabel);
        add(incrementButton);

        setSize(300, 150);
        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new Bug2_MissingListener().setVisible(true);
        });
    }

    /*Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Executing task in thread: " + Thread.currentThread().getName());
    }
};

new Thread(task).start(); */


}