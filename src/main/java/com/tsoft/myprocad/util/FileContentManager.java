package com.tsoft.myprocad.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileFilter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import com.tsoft.myprocad.l10n.L10;

/**
 * Content manager for files with Swing file choosers.
 */
public class FileContentManager implements ContentManager {
    private Map<ContentType, File> lastDirectories = new HashMap<>();

    public FileContentManager() { }

    /**
     * Returns the file filters available for a given content type.
     * This method may be overridden to add some file filters to existing content types
     * or to define the filters of a user defined content type.
     */
    protected FileFilter[] getFileFilter(final ContentType contentType) {
        FileFilter contentTypeFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                // Accept directories and files
                return file.isDirectory() || file.getName().toLowerCase().endsWith(contentType.getDefaultFileExtension());
            }

            @Override
            public String getDescription() {
                        return contentType.getDescription();
                    }
        };

        return new FileFilter[] { contentTypeFilter };
    }

    /**
     * Returns <code>true</code> if the file path in parameter is accepted
     * for <code>contentType</code>.
     */
    public boolean isAcceptable(String contentPath, ContentType contentType) {
        for (FileFilter filter : getFileFilter(contentType)) {
            if (filter.accept(new File(contentPath))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns <code>true</code> if the given content type is for directories.
     */
    protected boolean isDirectory(ContentType contentType) {
        return false;
    }

    /**
     * Returns the file path chosen by user with an open file dialog.
     * @return the file path or <code>null</code> if user canceled its choice.
     */
    public String showOpenDialog(String dialogTitle, ContentType contentType) {
        // Use native file dialog under Mac OS X
        if (OperatingSystem.isMacOSX() && !isDirectory(contentType)) {
            return showFileDialog(dialogTitle, contentType, null, false);
        } else {
            return showFileChooser(dialogTitle, contentType, null, false);
        }
    }

    /**
     * Returns the file path chosen by user with a save file dialog.
     * If this file already exists, the user will be prompted whether
     * he wants to overwrite this existing file.
     * @return the chosen file path or <code>null</code> if user canceled its choice.
     */
    public String showSaveDialog(String dialogTitle, ContentType contentType, String path) {
        String defaultExtension = contentType.getDefaultFileExtension();
        if (path != null) {
            // If path has an extension, remove it and build a path that matches contentType
            int extensionIndex = path.lastIndexOf('.');
            if (extensionIndex != -1) {
                path = path.substring(0, extensionIndex);
                if (defaultExtension != null) {
                    path += defaultExtension;
                }
            }
        }

        String savedPath;
        // Use native file dialog under Mac OS X
        if (OperatingSystem.isMacOSX() && !isDirectory(contentType)) {
            savedPath = showFileDialog(dialogTitle, contentType, path, true);
        } else {
            savedPath = showFileChooser(dialogTitle, contentType, path, true);
        }

        boolean addedExtension = false;
        if (savedPath != null) {
            if (defaultExtension != null) {
                if (!savedPath.toLowerCase().endsWith(defaultExtension)) {
                    savedPath += defaultExtension;
                    addedExtension = true;
                }
            }

            // If no extension was added to file under Mac OS X,
            // FileDialog already asks to user if he wants to overwrite savedName
            if (OperatingSystem.isMacOSX()
                    && !addedExtension) {
                return savedPath;
            }
            if (!isDirectory(contentType)) {
                // If the file exists, prompt user if he wants to overwrite it
                File savedFile = new File(savedPath);
                if (savedFile.exists() && !confirmOverwrite(savedFile.getName())) {
                    return showSaveDialog(dialogTitle, contentType, savedPath);
                }
            }
        }
        return savedPath;
    }

    /**
     * Displays an AWT open file dialog.
     */
    private String showFileDialog(String dialogTitle, final ContentType  contentType, String path, boolean save) {
        FileDialog fileDialog = new FileDialog(JOptionPane.getFrameForComponent(null));

        // Set selected file
        if (save && path != null) {
            fileDialog.setFile(new File(path).getName());
        }

        // Set supported files filter
        fileDialog.setFilenameFilter(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return isAcceptable(new File(dir, name).toString(), contentType);
            }
        });

        // Update directory
        File directory = getLastDirectory(contentType);
        if (directory != null) {
            fileDialog.setDirectory(directory.toString());
        }
        if (save) {
            fileDialog.setMode(FileDialog.SAVE);
        } else {
            fileDialog.setMode(FileDialog.LOAD);
        }

        if (dialogTitle == null) {
            dialogTitle = getFileDialogTitle(save);
        }
        fileDialog.setTitle(dialogTitle);

        fileDialog.setVisible(true);

        String selectedFile = fileDialog.getFile();
        // If user chose a file
        if (selectedFile != null) {
            // Retrieve directory for future calls
            directory = new File(fileDialog.getDirectory());
            // Store current directory
            setLastDirectory(contentType, directory);
            // Return selected file
            return directory + File.separator + selectedFile;
        }

        return null;
    }

    /**
     * Returns the last directory used for the given content type.
     * @return the last directory for <code>contentType</code> or the default last directory
     *         if it's not set. If <code>contentType</code> is <code>null</code>, the
     *         returned directory will be the default last one or <code>null</code> if it's not set yet.
     */
    protected File getLastDirectory(ContentType contentType) {
        File directory = lastDirectories.get(contentType);
        if (directory == null) {
            directory = lastDirectories.get(null);
        }
        return directory;
    }

    /**
     * Stores the last directory for the given content type.
     */
    protected void setLastDirectory(ContentType contentType, File directory) {
        lastDirectories.put(contentType, directory);

        // Store default last directory in null content
        lastDirectories.put(null, directory);
    }

    /**
     * Displays a Swing open file chooser.
     */
    private String showFileChooser(String dialogTitle, ContentType contentType, String path, boolean save) {
        final JFileChooser fileChooser;
        if (isDirectory(contentType)) {
            fileChooser = new DirectoryChooser();
        } else {
            fileChooser = new JFileChooser();
        }
        if (dialogTitle == null) {
            dialogTitle = getFileDialogTitle(save);
        }
        fileChooser.setDialogTitle(dialogTitle);

        // Update directory
        File directory = getLastDirectory(contentType);
        if (directory != null && directory.exists()) {
            fileChooser.setCurrentDirectory(directory);
            if (isDirectory(contentType)) {
                fileChooser.setSelectedFile(directory);
            }
        }

        // Set selected file
        if (save && path != null && (directory == null || !isDirectory(contentType))) {
            fileChooser.setSelectedFile(new File(path));
        }

        // Set supported files filter
        FileFilter acceptAllFileFilter = fileChooser.getAcceptAllFileFilter();
        fileChooser.addChoosableFileFilter(acceptAllFileFilter);
        FileFilter [] contentFileFilters = getFileFilter(contentType);
        for (FileFilter filter : contentFileFilters) {
            fileChooser.addChoosableFileFilter(filter);
        }

        // If there's only one file filter, select it
        if (contentFileFilters.length == 1) {
            fileChooser.setFileFilter(contentFileFilters [0]);
        } else {
            fileChooser.setFileFilter(acceptAllFileFilter);
        }

        int option;
        if (isDirectory(contentType)) {
            option = fileChooser.showDialog(null, L10.get(L10.SELECT_DIALOG));
        } else if (save) {
            option = fileChooser.showSaveDialog(null);
        } else {
            option = fileChooser.showOpenDialog(null);
        }

        if (option == JFileChooser.APPROVE_OPTION) {
            // Retrieve last directory for future calls
            if (isDirectory(contentType)) {
                directory = fileChooser.getSelectedFile();
            } else {
                directory = fileChooser.getCurrentDirectory();
            }

            // Store last directory
            setLastDirectory(contentType, directory);

            // Return selected file
            return fileChooser.getSelectedFile().toString();
        } else {
            return null;
        }
    }

    /**
     * Returns default file dialog title.
     */
    protected String getFileDialogTitle(boolean save) {
        if (save) {
            return L10.get(L10.SAVE_DIALOG);
        } else {
            return L10.get(L10.OPEN_DIALOG);
        }
    }

    /**
     * Displays a dialog that let user choose whether he wants to overwrite
     * file <code>path</code> or not.
     * @return <code>true</code> if user confirmed to overwrite.
     */
    protected boolean confirmOverwrite(String path) {
        // Retrieve displayed text in buttons and message
        String message = L10.get(L10.CONFIRM_OVERWRITE_MESSAGE, path);
        String title = L10.get(L10.CONFIRM_DIALOG);
        String replace = L10.get(L10.CONFIRM_OVERWRITE_OVERWRITE);
        String cancel = L10.get(L10.CONFIRM_OVERWRITE_CANCEL);

        return JOptionPane.showOptionDialog(null,
                message, title, JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object [] {replace, cancel}, cancel) == JOptionPane.OK_OPTION;
    }

    /**
     * A file chooser dedicated to directory choice.
     */
    private static class DirectoryChooser extends JFileChooser {
        private Executor               fileSystemViewExecutor;
        private JTree                  directoriesTree;
        private TreeSelectionListener  treeSelectionListener;
        private PropertyChangeListener selectedFileListener;
        private Action                 createDirectoryAction;

        public DirectoryChooser() {
            setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            this.fileSystemViewExecutor = Executors.newSingleThreadExecutor();
            this.directoriesTree = new JTree(new DefaultTreeModel(new DirectoryNode()));
            this.directoriesTree.setRootVisible(false);
            this.directoriesTree.setEditable(false);
            this.directoriesTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
            this.directoriesTree.setCellRenderer(new DefaultTreeCellRenderer() {
                @Override
                public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                              boolean leaf, int row, boolean hasFocus) {
                    DirectoryNode node = (DirectoryNode)value;
                    File file = (File)node.getUserObject();
                    super.getTreeCellRendererComponent(tree, DirectoryChooser.this.getName(file),
                            selected, expanded, leaf, row, hasFocus);
                    setIcon(DirectoryChooser.this.getIcon(file));
                    if (!node.isWritable()) {
                        setForeground(Color.GRAY);
                    }
                    return this;
                }
            });
            this.treeSelectionListener = new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent ev) {
                    TreePath selectionPath = directoriesTree.getSelectionPath();
                    if (selectionPath != null) {
                        DirectoryNode selectedNode = (DirectoryNode)selectionPath.getLastPathComponent();
                        setSelectedFile((File)selectedNode.getUserObject());
                    }
                }
            };
            this.directoriesTree.addTreeSelectionListener(this.treeSelectionListener);

            this.selectedFileListener = new PropertyChangeListener() {
                public void propertyChange(PropertyChangeEvent ev) {
                    showSelectedFile();
                }
            };
            addPropertyChangeListener(SELECTED_FILE_CHANGED_PROPERTY, this.selectedFileListener);

            this.directoriesTree.addTreeExpansionListener(new TreeExpansionListener() {
                public void treeCollapsed(TreeExpansionEvent ev) {
                    if (ev.getPath().isDescendant(directoriesTree.getSelectionPath())) {
                        // If selected node becomes hidden select not hidden parent
                        removePropertyChangeListener(SELECTED_FILE_CHANGED_PROPERTY, selectedFileListener);
                        directoriesTree.setSelectionPath(ev.getPath());
                        addPropertyChangeListener(SELECTED_FILE_CHANGED_PROPERTY, selectedFileListener);
                    }
                }

                public void treeExpanded(TreeExpansionEvent ev) {
                }
            });

            // Create an action used to create additional directories
            final String newDirectoryText = UIManager.getString("FileChooser.win32.newFolder");
            this.createDirectoryAction = new AbstractAction(newDirectoryText) {
                public void actionPerformed(ActionEvent ev) {
                    String newDirectoryNameBase = OperatingSystem.isWindows() || OperatingSystem.isMacOSX()
                            ? newDirectoryText
                            : UIManager.getString("FileChooser.other.newFolder");
                    String newDirectoryName = newDirectoryNameBase;
                    // Search a new directory name that doesn't exist
                    DirectoryNode parentNode = (DirectoryNode)directoriesTree.getLastSelectedPathComponent();
                    File parentDirectory = (File)parentNode.getUserObject();
                    for (int i = 2; new File(parentDirectory, newDirectoryName).exists(); i++) {
                        newDirectoryName = newDirectoryNameBase;
                        if (OperatingSystem.isWindows() || OperatingSystem.isMacOSX()) {
                            newDirectoryName += " ";
                        }
                        newDirectoryName += i;
                    }
                    newDirectoryName = (String)JOptionPane.showInputDialog(DirectoryChooser.this,
                            L10.get(L10.CREATE_FOLDER),
                            newDirectoryText, JOptionPane.QUESTION_MESSAGE, null, null, newDirectoryName);
                    if (newDirectoryName != null) {
                        File newDirectory = new File(parentDirectory, newDirectoryName);
                        if (!newDirectory.mkdir()) {
                            String newDirectoryErrorText = UIManager.getString("FileChooser.newFolderErrorText");
                            JOptionPane.showMessageDialog(DirectoryChooser.this,
                                    newDirectoryErrorText, newDirectoryErrorText, JOptionPane.ERROR_MESSAGE);
                        } else {
                            parentNode.updateChildren(parentNode.getChildDirectories());
                            ((DefaultTreeModel)directoriesTree.getModel()).nodeStructureChanged(parentNode);
                            setSelectedFile(newDirectory);
                        }
                    }
                }
            };

            setSelectedFile(getFileSystemView().getHomeDirectory());
        }

        /**
         * Selects the given directory or its parent if it's a file.
         */
        @Override
        public void setSelectedFile(File file) {
            if (file != null && file.isFile()) {
                file = file.getParentFile();
            }
            super.setSelectedFile(file);
        }

        /**
         * Shows asynchronously the selected file in the directories tree,
         * filling the parents siblings hierarchy if necessary.
         */
        private void showSelectedFile() {
            final File selectedFile = getSelectedFile();
            if (selectedFile != null) {
                final DirectoryNode rootNode = (DirectoryNode)this.directoriesTree.getModel().getRoot();
                this.fileSystemViewExecutor.execute(new Runnable() {
                    public void run() {
                        try {
                            EventQueue.invokeAndWait(new Runnable() {
                                public void run() {
                                    createDirectoryAction.setEnabled(false);
                                    if (directoriesTree.isShowing()) {
                                        directoriesTree.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                                    }
                                }
                            });
                            File cononicalFile = selectedFile.getCanonicalFile();
                            // Search parents of the selected file
                            List<File> parentsAndFile = new ArrayList<File>();
                            for (File file = cononicalFile;
                                 file != null;
                                 file = getFileSystemView().getParentDirectory(file)) {
                                parentsAndFile.add(0, file);
                            }
                            // Build path of tree nodes
                            final List<DirectoryNode> pathToFileNode = new ArrayList<DirectoryNode>();
                            DirectoryNode node = rootNode;
                            pathToFileNode.add(node);
                            for (final File file : parentsAndFile) {
                                final File [] childDirectories = node.isLoaded()
                                        ? null
                                        : node.getChildDirectories();
                                // Search in a child of the node has a user object equal to file
                                final DirectoryNode currentNode = node;
                                EventQueue.invokeAndWait(new Runnable() {
                                    public void run() {
                                        if (!currentNode.isLoaded()) {
                                            currentNode.updateChildren(childDirectories);
                                            ((DefaultTreeModel)directoriesTree.getModel()).nodeStructureChanged(currentNode);
                                        }
                                        for (int i = 0, n = currentNode.getChildCount(); i < n; i++) {
                                            DirectoryNode child = (DirectoryNode)currentNode.getChildAt(i);
                                            if (file.equals(child.getUserObject())) {
                                                pathToFileNode.add(child);
                                                break;
                                            }
                                        }
                                    }
                                });
                                node = pathToFileNode.get(pathToFileNode.size() - 1);
                                if (currentNode == node) {
                                    // Give up since file wasn't found
                                    break;
                                }
                            }

                            if (pathToFileNode.size() > 1) {
                                final TreePath path = new TreePath(pathToFileNode.toArray(new TreeNode [pathToFileNode.size()]));
                                EventQueue.invokeAndWait(new Runnable() {
                                    public void run() {
                                        directoriesTree.removeTreeSelectionListener(treeSelectionListener);
                                        directoriesTree.expandPath(path);
                                        directoriesTree.setSelectionPath(path);
                                        directoriesTree.scrollRowToVisible(directoriesTree.getRowForPath(path));
                                        directoriesTree.addTreeSelectionListener(treeSelectionListener);
                                    }
                                });
                            }

                        } catch (IOException ex) {
                            // Ignore directories that can't be found
                        } catch (InterruptedException ex) {
                            // Give up if interrupted
                        } catch (InvocationTargetException ex) {
                            ex.printStackTrace();
                        } finally {
                            EventQueue.invokeLater(new Runnable() {
                                public void run() {
                                    createDirectoryAction.setEnabled(directoriesTree.getSelectionCount() > 0
                                            && ((DirectoryNode)directoriesTree.getSelectionPath().getLastPathComponent()).isWritable());
                                    directoriesTree.setCursor(Cursor.getDefaultCursor());
                                }
                            });
                        }
                    }
                });
            }
        }

        @Override
        public int showDialog(Component parent, final String approveButtonText) {
            final JButton createDirectoryButton = new JButton(this.createDirectoryAction);
            final JButton approveButton = new JButton(approveButtonText);
            Object cancelOption = UIManager.get("FileChooser.cancelButtonText");
            Object [] options;
            if (OperatingSystem.isMacOSX()) {
                options = new Object [] {approveButton, cancelOption, createDirectoryButton};
            } else {
                options = new Object [] {createDirectoryButton, approveButton, cancelOption};
            }
            // Display chooser in a resizable dialog
            final JOptionPane optionPane = new JOptionPane(SwingTools.createScrollPane(this.directoriesTree),
                    JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, options, approveButton);
            final JDialog dialog = optionPane.createDialog(SwingUtilities.getRootPane(parent), getDialogTitle());
            dialog.setResizable(true);
            dialog.pack();
            if (this.directoriesTree.getSelectionCount() > 0) {
                this.directoriesTree.scrollPathToVisible(this.directoriesTree.getSelectionPath());
                boolean validDirectory = ((DirectoryNode)this.directoriesTree.getSelectionPath().getLastPathComponent()).isWritable();
                approveButton.setEnabled(validDirectory);
                createDirectoryAction.setEnabled(validDirectory);
            }
            this.directoriesTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent ev) {
                    TreePath selectedPath = ev.getPath();
                    boolean validDirectory = selectedPath != null
                            && ((DirectoryNode)ev.getPath().getLastPathComponent()).isWritable();
                    approveButton.setEnabled(validDirectory);
                    createDirectoryAction.setEnabled(validDirectory);
                }
            });
            approveButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ev) {
                    optionPane.setValue(approveButtonText);
                    dialog.setVisible(false);
                }
            });
            dialog.setMinimumSize(dialog.getPreferredSize());
            dialog.setVisible(true);
            dialog.dispose();
            if (approveButtonText.equals(optionPane.getValue())) {
                return JFileChooser.APPROVE_OPTION;
            } else {
                return JFileChooser.CANCEL_OPTION;
            }
        }

        /**
         * Directory nodes which children are loaded when needed.
         */
        private class DirectoryNode extends DefaultMutableTreeNode {
            private boolean loaded;
            private boolean writable;

            private DirectoryNode() {
                super(null);
            }

            private DirectoryNode(File file) {
                super(file);
                this.writable = file.canWrite();
            }

            public boolean isWritable() {
                return this.writable;
            }

            @Override
            public int getChildCount() {
                if (!this.loaded) {
                    this.loaded = true;
                    return updateChildren(getChildDirectories());
                } else {
                    return super.getChildCount();
                }
            }

            public File [] getChildDirectories() {
                File [] childFiles = getUserObject() == null
                        ? getFileSystemView().getRoots()
                        : getFileSystemView().getFiles((File)getUserObject(), true);
                if (childFiles != null) {
                    List<File> childDirectories = new ArrayList<File>(childFiles.length);
                    for (File childFile : childFiles) {
                        if (isTraversable(childFile)) {
                            childDirectories.add(childFile);
                        }
                    }
                    return childDirectories.toArray(new File [childDirectories.size()]);
                } else {
                    return new File [0];
                }
            }

            public boolean isLoaded() {
                return this.loaded;
            }

            public int updateChildren(File [] childDirectories) {
                if (this.children == null) {
                    this.children = new Vector<>(childDirectories.length);
                }
                synchronized (this.children) {
                    removeAllChildren();
                    for (File childFile : childDirectories) {
                        add(new DirectoryNode(childFile));
                    }
                    return childDirectories.length;
                }
            }
        }
    }
}
