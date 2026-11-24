import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class DrawingApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(DrawingApp::createAndShowGui);
    }

    private static void createAndShowGui() {
        DrawingPanel drawingPanel = new DrawingPanel();
        JButton clearButton = new JButton("Clear drawing");
        clearButton.addActionListener(event -> drawingPanel.clearDrawing());

        JFrame frame = new JFrame("Mouse Drawing App");
        frame.add(drawingPanel, BorderLayout.CENTER);
        frame.add(clearButton, BorderLayout.SOUTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static final class DrawingPanel extends JPanel {
        private static final long serialVersionUID = 1L;
        private final List<List<Point>> strokes = new ArrayList<>();

        private DrawingPanel() {
            setBackground(Color.WHITE);
            setPreferredSize(new Dimension(600, 400));

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent event) {
                    strokes.add(new ArrayList<>());
                    strokes.get(strokes.size() - 1).add(event.getPoint());
                    repaint();
                }

                @Override
                public void mouseDragged(MouseEvent event) {
                    if (!strokes.isEmpty()) {
                        strokes.get(strokes.size() - 1).add(event.getPoint());
                        repaint();
                    }
                }
            };
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }

        private void clearDrawing() {
            strokes.clear();
            repaint();
        }

        @Override
        protected void paintComponent(Graphics graphics) {
            super.paintComponent(graphics);
            graphics.setColor(Color.BLUE);
            for (List<Point> stroke : strokes) {
                for (int i = 1; i < stroke.size(); i++) {
                    Point previous = stroke.get(i - 1);
                    Point current = stroke.get(i);
                    graphics.drawLine(previous.x, previous.y, current.x, current.y);
                }
                if (stroke.size() == 1) {
                    Point point = stroke.get(0);
                    graphics.fillOval(point.x - 2, point.y - 2, 4, 4);
                }
            }
        }
    }
}
