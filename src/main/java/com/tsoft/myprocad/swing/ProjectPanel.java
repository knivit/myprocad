package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Folder;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.viewcontroller.ProjectController;

import java.awt.*;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import javax.swing.*;

public class ProjectPanel extends JPanel {
    private ProjectController projectController;
    private JComboBox folderComboBox;
    private JTabbedPane tabbedPane;

    private ItemListener folderListener = (e -> {
        final Object selectedItem = e.getItem();

        // itemStateChanged fires two times (by JComboBox.selectedItemChanged):
        // first, for deselected item, second, for a selected
        // we need only one of them
        if (e.getStateChange() == ItemEvent.DESELECTED) return;
        if (selectedItem == null) return;

        // invoke separately to prevent hanging under a debugger
        EventQueue.invokeLater(() -> projectController.project.setActiveFolder((Folder) selectedItem));
    });

    public ProjectPanel(ProjectController projectController) {
        super(new BorderLayout());
        this.projectController = projectController;

        JLabel label = new JLabel(L10.get(L10.FOLDER_LABEL));

        folderComboBox = new JComboBox();
        folderComboBox.setFocusable(false);
        folderComboBox.setPreferredSize(new Dimension(30, 0));
        folderComboBox.addItemListener(folderListener);

        JPanel levelPanel = new JPanel();
        levelPanel.setLayout(new BoxLayout(levelPanel, BoxLayout.LINE_AXIS));
        levelPanel.setBorder(BorderFactory.createEmptyBorder(10, 6, 10, 8));
        levelPanel.add(label);
        levelPanel.add(Box.createRigidArea(new Dimension(8, 0)));
        levelPanel.add(folderComboBox);
        levelPanel.add(Box.createHorizontalGlue());

        tabbedPane = new JTabbedPane();
        tabbedPane.setOpaque(true);

        tabbedPane.addChangeListener(l -> {
            projectController.afterTabChanged();

            // move focus to view panel
            Component selected = tabbedPane.getSelectedComponent();
            if (selected != null) {
                if (selected instanceof JSplitPane) {
                    JSplitPane splitPane = (JSplitPane) selected;
                    splitPane.getRightComponent().requestFocus();
                } else selected.requestFocus();
            }
        });

        add(levelPanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);

        JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);
    }

    public int getSelectedTabIndex() {
        return tabbedPane.getSelectedIndex();
    }

    public void selectTab(int index) {
        tabbedPane.setSelectedIndex(index < 0 || index >= tabbedPane.getTabCount() ? 0 : index);
    }

    public void selectTab(String tabName) {
        int index = tabbedPane.indexOfTab(tabName);
        if (index != -1) selectTab(index);
    }

    public void addTab(String tabName, JComponent panel) {
        tabbedPane.addTab(tabName, panel);
    }

    public void setCurrentTabName(String name) {
        int index = tabbedPane.getSelectedIndex();
        tabbedPane.setTitleAt(index, name);
    }

    public void removeAllTabs() {
        tabbedPane.removeAll();
    }

    public void removeTab(String tabName) {
        int index = tabbedPane.indexOfTab(tabName);
        if (index != -1) tabbedPane.removeTabAt(index);
    }

    public void fillFolderComboBox(Project project) {
        folderComboBox.removeItemListener(folderListener);
        try {
            folderComboBox.removeAllItems();
            project.getFolders().stream().forEach(folderComboBox::addItem);
            folderComboBox.setSelectedItem(project.getActiveFolder());
        } finally {
            folderComboBox.addItemListener(folderListener);
        }
    }

    public void moveTabTo(String name, int newIndex) {
        int oldIndex = tabbedPane.indexOfTab(name);
        if (oldIndex == newIndex) return;

        Component panel = tabbedPane.getComponentAt(oldIndex);
        tabbedPane.removeTabAt(oldIndex);
        if (oldIndex < newIndex) tabbedPane.insertTab(name, null, panel, null, newIndex);
        else tabbedPane.insertTab(name, null, panel, null, newIndex);
    }
}
