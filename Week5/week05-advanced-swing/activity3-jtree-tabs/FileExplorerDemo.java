import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class FileExplorerDemo {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(FileExplorerDemo::createAndShow);
    }

    private static void createAndShow() {
        JFrame frame = new JFrame("File Explorer Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.setLocationRelativeTo(null);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("My Computer");
        DefaultMutableTreeNode documents = new DefaultMutableTreeNode("Documents");
        documents.add(new DefaultMutableTreeNode("Resume.docx"));
        documents.add(new DefaultMutableTreeNode("CV.pdf"));
        documents.add(new DefaultMutableTreeNode("Notes.txt"));

        DefaultMutableTreeNode pictures = new DefaultMutableTreeNode("Pictures");
        pictures.add(new DefaultMutableTreeNode("Vacation.jpg"));
        pictures.add(new DefaultMutableTreeNode("Family.jpg"));

        DefaultMutableTreeNode projects = new DefaultMutableTreeNode("Projects");
        DefaultMutableTreeNode javaProject = new DefaultMutableTreeNode("JavaApp");
        javaProject.add(new DefaultMutableTreeNode("Main.java"));
        javaProject.add(new DefaultMutableTreeNode("Utils.java"));
        projects.add(javaProject);

        root.add(documents);
        root.add(pictures);
        root.add(projects);

        JTree tree = new JTree(new DefaultTreeModel(root));
        tree.setFont(new Font("Arial", Font.PLAIN, 14));
        tree.setRowHeight(25);

        JTextArea infoArea = new JTextArea(5, 30);
        infoArea.setEditable(false);
        infoArea.setFont(new Font("Arial", Font.PLAIN, 14));
        tree.addTreeSelectionListener(event -> {
            Object selectedObject = tree.getLastSelectedPathComponent();
            if (selectedObject instanceof DefaultMutableTreeNode) {
                DefaultMutableTreeNode selected = (DefaultMutableTreeNode) selectedObject;
                infoArea.setText("You selected: " + selected.getUserObject()
                        + "\nLevel: " + selected.getLevel());
            }
        });

        JTabbedPane tabs = new JTabbedPane();

        JPanel generalTab = new JPanel();
        generalTab.setLayout(new BoxLayout(generalTab, BoxLayout.Y_AXIS));
        generalTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        generalTab.add(new JLabel("General Settings"));
        generalTab.add(Box.createVerticalStrut(15));
        generalTab.add(new JCheckBox("Show hidden files"));
        generalTab.add(new JCheckBox("Auto-save every 5 minutes"));
        generalTab.add(new JCheckBox("Enable notifications"));

        JPanel appearanceTab = new JPanel();
        appearanceTab.setLayout(new BoxLayout(appearanceTab, BoxLayout.Y_AXIS));
        appearanceTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        appearanceTab.add(new JLabel("Theme:"));
        appearanceTab.add(Box.createVerticalStrut(10));
        JRadioButton lightTheme = new JRadioButton("Light theme", true);
        JRadioButton darkTheme = new JRadioButton("Dark theme");
        JRadioButton systemTheme = new JRadioButton("System default");
        ButtonGroup themeChoices = new ButtonGroup();
        themeChoices.add(lightTheme);
        themeChoices.add(darkTheme);
        themeChoices.add(systemTheme);
        appearanceTab.add(lightTheme);
        appearanceTab.add(darkTheme);
        appearanceTab.add(systemTheme);

        JPanel aboutTab = new JPanel(new BorderLayout());
        aboutTab.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JTextArea aboutText = new JTextArea(
                "File Explorer Demo v1.0\n\n"
                + "Built with Java Swing as part of Week 5 practical activities.\n\n"
                + "Components used: JTree and JTabbedPane");
        aboutText.setEditable(false);
        aboutText.setLineWrap(true);
        aboutText.setWrapStyleWord(true);
        aboutTab.add(new JScrollPane(aboutText), BorderLayout.CENTER);

        tabs.addTab("General", generalTab);
        tabs.addTab("Appearance", appearanceTab);
        tabs.addTab("About", aboutTab);

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(tree), tabs);
        splitPane.setDividerLocation(250);

        frame.add(splitPane, BorderLayout.CENTER);
        frame.add(new JScrollPane(infoArea), BorderLayout.SOUTH);
        frame.setVisible(true);
    }
}
