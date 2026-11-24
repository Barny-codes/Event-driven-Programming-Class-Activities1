import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class KeyboardMover {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(KeyboardMover::createAndShowGui);
    }

    private static void createAndShowGui() {
        MoverPanel panel = new MoverPanel();
        JFrame frame = new JFrame("Keyboard Mover");
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        panel.requestFocusInWindow();
    }

    private static final class MoverPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private static final int SIZE = 35;
        private static final int STEP = 10;
        private int x = 180;
        private int y = 130;

        private MoverPanel() {
            setPreferredSize(new Dimension(420, 320));
            setBackground(Color.WHITE);
            setFocusable(true);
            bindMovement("LEFT", -STEP, 0);
            bindMovement("RIGHT", STEP, 0);
            bindMovement("UP", 0, -STEP);
            bindMovement("DOWN", 0, STEP);
        }

        private void bindMovement(String key, int deltaX, int deltaY) {
            String actionName = "move-" + key;
            getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
                KeyStroke.getKeyStroke("pressed " + key),
                actionName
            );
            getActionMap().put(actionName, new AbstractAction() {
                private static final long serialVersionUID = 1L;

                @Override
                public void actionPerformed(ActionEvent event) {
                    x = Math.max(0, Math.min(getWidth() - SIZE, x + deltaX));
                    y = Math.max(0, Math.min(getHeight() - SIZE, y + deltaY));
                    repaint();
                }
            });
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(Color.RED);
            graphics.fillRect(x, y, SIZE, SIZE);
        }
    }
}
