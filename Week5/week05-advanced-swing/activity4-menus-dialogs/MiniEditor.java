import java.awt.BorderLayout;
import java.awt.Font;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JToolBar;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MiniEditor {
    private JFrame frame;
    private JTextArea textArea;
    private JLabel statusBar;
    private JLabel wordCountLabel;
    private File currentFile;
    private boolean modified;
    private boolean loadingDocument;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MiniEditor().createAndShow());
    }

    private void createAndShow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        JMenuBar menuBar = createMenuBar();
        frame.setJMenuBar(menuBar);
        frame.add(createToolBar(), BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        frame.add(new JScrollPane(textArea), BorderLayout.CENTER);

        statusBar = new JLabel("Ready");
        statusBar.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        frame.add(statusBar, BorderLayout.SOUTH);

        textArea.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent event) {
                documentChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent event) {
                documentChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent event) {
                documentChanged();
            }
        });

        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent event) {
                exitEditor();
            }
        });

        updateWordCount();
        updateTitle();
        frame.setVisible(true);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem newItem = new JMenuItem("New");
        JMenuItem openItem = new JMenuItem("Open...");
        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem saveAsItem = new JMenuItem("Save As...");
        JMenuItem exitItem = new JMenuItem("Exit");
        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(saveAsItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu editMenu = new JMenu("Edit");
        JMenuItem cutItem = new JMenuItem("Cut");
        JMenuItem copyItem = new JMenuItem("Copy");
        JMenuItem pasteItem = new JMenuItem("Paste");
        editMenu.add(cutItem);
        editMenu.add(copyItem);
        editMenu.add(pasteItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        newItem.addActionListener(event -> newDocument());
        openItem.addActionListener(event -> openDocument());
        saveItem.addActionListener(event -> saveDocument());
        saveAsItem.addActionListener(event -> saveDocumentAs());
        exitItem.addActionListener(event -> exitEditor());
        cutItem.addActionListener(event -> textArea.cut());
        copyItem.addActionListener(event -> textArea.copy());
        pasteItem.addActionListener(event -> textArea.paste());
        aboutItem.addActionListener(event -> JOptionPane.showMessageDialog(
                frame,
                "Mini Editor v1.0\nBuilt with Java Swing\nWeek 5 Activity 4",
                "About",
                JOptionPane.INFORMATION_MESSAGE));

        return menuBar;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton newButton = new JButton("New");
        JButton openButton = new JButton("Open");
        JButton saveButton = new JButton("Save");
        wordCountLabel = new JLabel("0");

        toolBar.add(newButton);
        toolBar.add(openButton);
        toolBar.add(saveButton);
        toolBar.addSeparator();
        toolBar.add(new JLabel(" Word count: "));
        toolBar.add(wordCountLabel);

        newButton.addActionListener(event -> newDocument());
        openButton.addActionListener(event -> openDocument());
        saveButton.addActionListener(event -> saveDocument());
        return toolBar;
    }

    private void documentChanged() {
        updateWordCount();
        if (!loadingDocument) {
            modified = true;
            updateTitle();
            statusBar.setText("Unsaved changes");
        }
    }

    private void updateWordCount() {
        if (wordCountLabel == null || textArea == null) {
            return;
        }
        String content = textArea.getText().trim();
        wordCountLabel.setText(content.isEmpty()
                ? "0"
                : String.valueOf(content.split("\\s+").length));
    }

    private void updateTitle() {
        if (frame == null) {
            return;
        }
        String name = currentFile == null ? "Untitled" : currentFile.getName();
        frame.setTitle("Mini Editor - " + name + (modified ? " *" : ""));
    }

    private boolean confirmDiscardOrSave() {
        if (!modified) {
            return true;
        }

        int choice = JOptionPane.showConfirmDialog(
                frame,
                "Save changes before continuing?",
                "Unsaved Changes",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.WARNING_MESSAGE);
        if (choice == JOptionPane.YES_OPTION) {
            return saveDocument();
        }
        return choice == JOptionPane.NO_OPTION;
    }

    private void newDocument() {
        if (!confirmDiscardOrSave()) {
            return;
        }
        loadingDocument = true;
        textArea.setText("");
        loadingDocument = false;
        currentFile = null;
        modified = false;
        updateWordCount();
        updateTitle();
        statusBar.setText("New file created");
    }

    private void openDocument() {
        if (!confirmDiscardOrSave()) {
            return;
        }

        JFileChooser chooser = createTextFileChooser();
        if (chooser.showOpenDialog(frame) != JFileChooser.APPROVE_OPTION) {
            return;
        }

        File selectedFile = chooser.getSelectedFile();
        try {
            String content = new String(
                    Files.readAllBytes(selectedFile.toPath()), StandardCharsets.UTF_8);
            loadingDocument = true;
            textArea.setText(content);
            loadingDocument = false;
            currentFile = selectedFile;
            modified = false;
            updateWordCount();
            updateTitle();
            statusBar.setText("Opened: " + currentFile.getName());
        } catch (IOException exception) {
            loadingDocument = false;
            showFileError("Error opening file", exception);
        }
    }

    private boolean saveDocument() {
        if (currentFile == null) {
            return saveDocumentAs();
        }
        return writeCurrentDocument(currentFile);
    }

    private boolean saveDocumentAs() {
        JFileChooser chooser = createTextFileChooser();
        if (chooser.showSaveDialog(frame) != JFileChooser.APPROVE_OPTION) {
            return false;
        }

        File selectedFile = chooser.getSelectedFile();
        if (selectedFile.exists()) {
            int choice = JOptionPane.showConfirmDialog(
                    frame,
                    "Replace the existing file?",
                    "Confirm Save",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
            if (choice != JOptionPane.YES_OPTION) {
                return false;
            }
        }
        if (!writeCurrentDocument(selectedFile)) {
            return false;
        }
        currentFile = selectedFile;
        updateTitle();
        statusBar.setText("Saved as: " + currentFile.getName());
        return true;
    }

    private boolean writeCurrentDocument(File file) {
        try {
            Files.write(file.toPath(), textArea.getText().getBytes(StandardCharsets.UTF_8));
            currentFile = file;
            modified = false;
            updateTitle();
            statusBar.setText("Saved: " + currentFile.getName());
            return true;
        } catch (IOException exception) {
            showFileError("Error saving file", exception);
            return false;
        }
    }

    private void exitEditor() {
        if (confirmDiscardOrSave()) {
            frame.dispose();
        }
    }

    private JFileChooser createTextFileChooser() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Text Files (*.txt)", "txt"));
        return chooser;
    }

    private void showFileError(String title, IOException exception) {
        JOptionPane.showMessageDialog(
                frame,
                title + ":\n" + exception.getMessage(),
                "File Error",
                JOptionPane.ERROR_MESSAGE);
        statusBar.setText(title);
    }
}
