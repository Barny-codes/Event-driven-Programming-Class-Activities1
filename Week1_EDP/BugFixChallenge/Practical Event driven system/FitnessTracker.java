import javax.swing.*;
import java.awt.*;
import java.util.Date;

public class FitnessTracker extends JFrame {

    private JLabel statusLabel;
    private JTextArea eventLog;
    private JToggleButton workoutToggle;
    private JToggleButton heartMonitorToggle;
    private JButton stepSensorButton;

    private int stepCount = 0;
    private Thread heartRateThread;
    private boolean heartMonitoringActive = false;

    public FitnessTracker() {
        setTitle("FitPulse Tracker Controller");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        // Status Panel (NORTH)
        statusLabel = new JLabel("System Ready | Steps: 0");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 14));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(statusLabel, BorderLayout.NORTH);

        // Event Log (CENTER)
        eventLog = new JTextArea(10, 30);
        eventLog.setEditable(false);
        add(new JScrollPane(eventLog), BorderLayout.CENTER);

        // Control Panel (SOUTH)
        JPanel controlPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        controlPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Workout Mode Toggle
        workoutToggle = new JToggleButton("Workout Mode: STOPPED");
        workoutToggle.addActionListener(e -> {
            if (workoutToggle.isSelected()) {
                workoutToggle.setText("Workout Mode: RECORDING");
                logEvent("Workout session STARTED");
                statusLabel.setText("Workout Active | Steps: " + stepCount);
            } else {
                workoutToggle.setText("Workout Mode: STOPPED");
                logEvent("Workout session PAUSED/STOPPED");
                statusLabel.setText("Workout Stopped | Total Steps: " + stepCount);
            }
        });

        // Heart Rate Monitor Toggle
        heartMonitorToggle = new JToggleButton("Heart Rate Monitor: OFF");
        heartMonitorToggle.addActionListener(e -> {
            if (heartMonitorToggle.isSelected()) {
                heartMonitorToggle.setText("Heart Rate Monitor: ON");
                logEvent("Heart rate telemetry STARTED");
                startHeartRateMonitor();
            } else {
                heartMonitorToggle.setText("Heart Rate Monitor: OFF");
                heartMonitoringActive = false;
                logEvent("Heart rate telemetry STOPPED");
                statusLabel.setText("Heart Rate Monitor Off | Steps: " + stepCount);
            }
        });

        // Step Sensor Simulation Button
        stepSensorButton = new JButton("Simulate Step Taken (+10 Steps)");
        stepSensorButton.addActionListener(e -> {
            stepCount += 10;
            logEvent("Step sensor triggered: +10 steps (Total: " + stepCount + ")");

            if (stepCount >= 100 && stepCount - 10 < 100) {
                logEvent("MILESTONE: Step Goal Reached! (100 Steps)");
                statusLabel.setText("GOAL REACHED! Total Steps: " + stepCount);
                getContentPane().setBackground(new Color(220, 255, 220)); // Highlight success
            } else {
                String workoutState = workoutToggle.isSelected() ? "Workout Active" : "Idle";
                statusLabel.setText(workoutState + " | Steps: " + stepCount);
            }
        });

        controlPanel.add(workoutToggle);
        controlPanel.add(heartMonitorToggle);
        controlPanel.add(stepSensorButton);
        add(controlPanel, BorderLayout.SOUTH);

        setSize(450, 420);
        setLocationRelativeTo(null);
    }

    private void startHeartRateMonitor() {
        heartMonitoringActive = true;
        heartRateThread = new Thread(() -> {
            while (heartMonitoringActive) {
                int bpm = 70 + (int) (Math.random() * 70); // Generates BPM between 70 and 140

                SwingUtilities.invokeLater(() -> {
                    if (heartMonitoringActive) {
                        logEvent("Telemetry update: Heart Rate = " + bpm + " BPM");
                        if (bpm > 130) {
                            logEvent("ALERT: High Heart Rate Detected (" + bpm + " BPM)!");
                            statusLabel.setText("WARNING: High HR (" + bpm + " BPM) | Steps: " + stepCount);
                        } else {
                            String workoutState = workoutToggle.isSelected() ? "Workout Active" : "Tracking";
                            statusLabel.setText(workoutState + " | HR: " + bpm + " BPM | Steps: " + stepCount);
                        }
                    }
                });

                try {
                    Thread.sleep(3000); // Pulse reading every 3 seconds
                } catch (InterruptedException ex) {
                    break;
                }
            }
        });
        heartRateThread.start();
    }

    private void logEvent(String event) {
        String timestamp = new Date().toString();
        eventLog.append("[" + timestamp + "] " + event + "\n");
        eventLog.setCaretPosition(eventLog.getDocument().getLength());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FitnessTracker().setVisible(true);
        });
    }
}