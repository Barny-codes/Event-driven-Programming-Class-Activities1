import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;
import javax.swing.tree.TreePath;

public class FileManager extends JFrame {
    private static final String PLACEHOLDER = " ";
    private static final DateTimeFormatter MODIFIED_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

    private final File homeDirectory = new File(System.getProperty("user.home")).getAbsoluteFile();
    private final DefaultMutableTreeNode rootNode = new DirectoryNode(homeDirectory);
    private final DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
    private final JTree directoryTree = new JTree(treeModel);
    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[] {"Name", "Type", "Size", "Modified"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    private final JTable fileTable = new JTable(tableModel);
    private final JLabel statusBar = new JLabel();

    private File currentDirectory = homeDirectory;

    public FileManager() {
        super("File Manager");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        rootNode.add(new DefaultMutableTreeNode(PLACEHOLDER));
        directoryTree.setRootVisible(true);
        directoryTree.setShowsRootHandles(true);
        directoryTree.addTreeWillExpandListener(new TreeWillExpandListener() {
            @Override
            public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
                Object node = event.getPath().getLastPathComponent();
                if (node instanceof DirectoryNode) {
                    loadChildren((DirectoryNode) node, false);
                }
            }

            @Override
            public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
            }
        });
        directoryTree.addTreeSelectionListener(event -> {
            Object selected = directoryTree.getLastSelectedPathComponent();
            if (selected instanceof DirectoryNode) {
                File selectedDirectory = ((DirectoryNode) selected).directory;
                if (!selectedDirectory.equals(currentDirectory)) {
                    navigateTo(selectedDirectory);
                }
            }
        });

        fileTable.setFillsViewportHeight(true);
        fileTable.setAutoCreateRowSorter(true);
        fileTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent event) {
                if (event.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(event)) {
                    int row = fileTable.rowAtPoint(event.getPoint());
                    if (row >= 0) {
                        int modelRow = fileTable.convertRowIndexToModel(row);
                        File selectedFile = new File(
                                currentDirectory, tableModel.getValueAt(modelRow, 0).toString());
                        if (selectedFile.isDirectory()) {
                            navigateTo(selectedFile);
                        } else if (selectedFile.isFile()) {
                            JOptionPane.showMessageDialog(
                                    FileManager.this,
                                    selectedFile.getAbsolutePath(),
                                    "File path",
                                    JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            }
        });

        JSplitPane splitPane = new JSplitPane(
                JSplitPane.HORIZONTAL_SPLIT,
                new JScrollPane(directoryTree),
                new JScrollPane(fileTable));
        splitPane.setDividerLocation(280);
        splitPane.setResizeWeight(0.0);

        setJMenuBar(createMenuBar());
        add(createToolBar(), BorderLayout.NORTH);
        add(splitPane, BorderLayout.CENTER);
        add(statusBar, BorderLayout.SOUTH);

        loadCurrentDirectory();
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem refreshItem = new JMenuItem("Refresh");
        refreshItem.addActionListener(event -> refreshCurrentDirectory());
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(event -> System.exit(0));
        fileMenu.add(refreshItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(event -> JOptionPane.showMessageDialog(
                this,
                "A simple file manager for browsing local directories.",
                "About File Manager",
                JOptionPane.INFORMATION_MESSAGE));
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }

    private JToolBar createToolBar() {
        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);

        JButton homeButton = new JButton("Home");
        homeButton.addActionListener(event -> navigateTo(homeDirectory));
        JButton upButton = new JButton("Up Folder");
        upButton.addActionListener(event -> {
            File parent = currentDirectory.getParentFile();
            if (parent != null) {
                navigateTo(parent);
            }
        });
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(event -> refreshCurrentDirectory());

        toolBar.add(homeButton);
        toolBar.add(upButton);
        toolBar.addSeparator();
        toolBar.add(refreshButton);
        return toolBar;
    }

    private void navigateTo(File directory) {
        if (directory == null || !directory.isDirectory()) {
            JOptionPane.showMessageDialog(
                    this,
                    "This directory is unavailable:\n" + directory,
                    "Unable to open directory",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        currentDirectory = directory.getAbsoluteFile();
        loadCurrentDirectory();
        selectTreePath(currentDirectory);
    }

    private void refreshCurrentDirectory() {
        DirectoryNode selectedNode = findTreeNode(currentDirectory);
        if (selectedNode != null) {
            loadChildren(selectedNode, true);
        }
        loadCurrentDirectory();
    }

    private void loadCurrentDirectory() {
        tableModel.setRowCount(0);
        File[] entries;
        try {
            entries = currentDirectory.listFiles();
        } catch (SecurityException exception) {
            updateStatus("Unable to read directory: " + exception.getMessage());
            return;
        }

        if (entries == null) {
            updateStatus("Unable to read directory (access denied or unavailable).");
            return;
        }

        Arrays.sort(entries, Comparator
                .comparing((File file) -> !file.isDirectory())
                .thenComparing(File::getName, String.CASE_INSENSITIVE_ORDER));
        for (File entry : entries) {
            tableModel.addRow(new Object[] {
                entry.getName(),
                getType(entry),
                entry.isDirectory() ? "-" : NumberFormat.getIntegerInstance()
                        .format(entry.length()) + " bytes",
                formatModified(entry.lastModified())
            });
        }
        updateStatus(null);
    }

    private void loadChildren(DirectoryNode node, boolean forceReload) {
        if (node.childrenLoaded && !forceReload) {
            return;
        }

        node.removeAllChildren();
        File[] directories;
        try {
            directories = node.directory.listFiles(File::isDirectory);
        } catch (SecurityException exception) {
            node.childrenLoaded = true;
            treeModel.nodeStructureChanged(node);
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to read subfolders:\n" + exception.getMessage(),
                    "Directory access error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (directories == null) {
            node.childrenLoaded = true;
            treeModel.nodeStructureChanged(node);
            JOptionPane.showMessageDialog(
                    this,
                    "Unable to read subfolders of:\n" + node.directory.getAbsolutePath(),
                    "Directory access error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Arrays.sort(directories, Comparator.comparing(
                File::getName, String.CASE_INSENSITIVE_ORDER));
        for (File directory : directories) {
            DirectoryNode child = new DirectoryNode(directory);
            child.add(new DefaultMutableTreeNode(PLACEHOLDER));
            node.add(child);
        }
        node.childrenLoaded = true;
        treeModel.nodeStructureChanged(node);
    }

    private void selectTreePath(File directory) {
        Path homePath = homeDirectory.toPath().toAbsolutePath().normalize();
        Path directoryPath = directory.toPath().toAbsolutePath().normalize();
        if (!directoryPath.startsWith(homePath)) {
            directoryTree.clearSelection();
            return;
        }

        DirectoryNode node = (DirectoryNode) rootNode;
        TreePath path = new TreePath(rootNode);
        Path relativePath = homePath.relativize(directoryPath);
        for (Path segment : relativePath) {
            loadChildren(node, false);
            File expectedDirectory = new File(node.directory, segment.toString()).getAbsoluteFile();
            DirectoryNode nextNode = findChild(node, expectedDirectory);
            if (nextNode == null) {
                directoryTree.clearSelection();
                return;
            }
            node = nextNode;
            path = path.pathByAddingChild(node);
            directoryTree.expandPath(path.getParentPath());
        }
        directoryTree.setSelectionPath(path);
        directoryTree.scrollPathToVisible(path);
    }

    private DirectoryNode findTreeNode(File directory) {
        Path homePath = homeDirectory.toPath().toAbsolutePath().normalize();
        Path directoryPath = directory.toPath().toAbsolutePath().normalize();
        if (!directoryPath.startsWith(homePath)) {
            return null;
        }

        DirectoryNode node = (DirectoryNode) rootNode;
        for (Path segment : homePath.relativize(directoryPath)) {
            loadChildren(node, false);
            File expectedDirectory = new File(node.directory, segment.toString()).getAbsoluteFile();
            node = findChild(node, expectedDirectory);
            if (node == null) {
                return null;
            }
        }
        return node;
    }

    private DirectoryNode findChild(DirectoryNode parent, File directory) {
        for (int index = 0; index < parent.getChildCount(); index++) {
            Object child = parent.getChildAt(index);
            if (child instanceof DirectoryNode
                    && ((DirectoryNode) child).directory.toPath().toAbsolutePath().normalize()
                            .equals(directory.toPath().toAbsolutePath().normalize())) {
                return (DirectoryNode) child;
            }
        }
        return null;
    }

    private String getType(File file) {
        if (file.isDirectory()) {
            return "Folder";
        }
        String name = file.getName();
        int extensionStart = name.lastIndexOf('.');
        return extensionStart > 0 && extensionStart < name.length() - 1
                ? name.substring(extensionStart + 1).toUpperCase(Locale.getDefault()) + " file"
                : "File";
    }

    private String formatModified(long timestamp) {
        return MODIFIED_FORMAT.format(
                Instant.ofEpochMilli(timestamp).atZone(ZoneId.systemDefault()));
    }

    private void updateStatus(String error) {
        int itemCount = tableModel.getRowCount();
        String itemLabel = itemCount == 1 ? "item" : "items";
        String status = itemCount + " " + itemLabel + " | " + currentDirectory.getAbsolutePath();
        statusBar.setText(error == null ? status : status + " | " + error);
    }

    private static final class DirectoryNode extends DefaultMutableTreeNode {
        private final File directory;
        private boolean childrenLoaded;

        private DirectoryNode(File directory) {
            super(directory);
            this.directory = directory;
            setAllowsChildren(true);
        }

        @Override
        public String toString() {
            String name = directory.getName();
            return name.isEmpty() ? directory.getAbsolutePath() : name;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FileManager().setVisible(true));
    }
}
