package com.tsoft.myprocad.swing;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.*;
import javax.swing.border.BevelBorder;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.swing.menu.*;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.ApplicationController;

public class ApplicationPanel extends JFrame {
    private ApplicationController controller;

    private JTabbedPane tabbedPane;

    private JPanel statusPanel;
    private JLabel xStatusLabel;
    private JLabel yStatusLabel;
    private JLabel modeStatusLabel;

    /**
     * Status Panel
     * +---------------------------------------------------------------------+
     * | X:     | Y:     | Mode:                            | <progress bar> |
     * +---------------------------------------------------------------------+
     */

    public ApplicationPanel(ApplicationController controller) {
        super();

        this.controller = controller;

        Container contentPane = getContentPane();
        contentPane.setBackground(SystemColor.controlShadow);

        setJMenuBar(Menu.menuBar);
        contentPane.add(Menu.toolBar, BorderLayout.NORTH);

        statusPanel = new JPanel();
        statusPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        contentPane.add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(400, 20));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));

        JLabel xLabel = new JLabel(L10.get(L10.STATUS_PANEL_X_LABEL));
        statusPanel.add(xLabel);

        xStatusLabel = new JLabel();
        xStatusLabel.setPreferredSize(new Dimension(120, 0));
        statusPanel.add(xStatusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel yLabel = new JLabel(L10.get(L10.STATUS_PANEL_Y_LABEL));
        statusPanel.add(yLabel);

        yStatusLabel = new JLabel();
        yStatusLabel.setPreferredSize(new Dimension(120, 0));
        statusPanel.add(yStatusLabel);
        statusPanel.add(Box.createRigidArea(new Dimension(10, 0)));

        JLabel modeLabel = new JLabel(L10.get(L10.STATUS_PANEL_MODE_LABEL));
        statusPanel.add(modeLabel);

        modeStatusLabel = new JLabel();
        modeStatusLabel.setPreferredSize(new Dimension(120, 0));
        statusPanel.add(modeStatusLabel);
        setStatusPanelVisible(false);

        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        setTitle(MyProCAD.APPLICATION_NAME);
    }

    private void createTabbedPane() {
        if (tabbedPane == null) {
            tabbedPane = new JTabbedPane();
            tabbedPane.setOpaque(true);
            getContentPane().add(tabbedPane);
        }
    }

    private void removeTabbedPane() {
        if (tabbedPane != null) {
            getContentPane().remove(tabbedPane);
            tabbedPane = null;
        }
    }

    public int getSelectedProjectTab() {
        return tabbedPane == null ? -1 : tabbedPane.getSelectedIndex();
    }

    public void addProject(String projectName, ProjectPanel projectPanel) {
        createTabbedPane();

        tabbedPane.addTab(projectName, projectPanel);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    }

    public void afterProjectClose(int tabIndex, boolean noOpenProjects) {
        tabbedPane.removeTabAt(tabIndex);
        if (noOpenProjects) {
            removeTabbedPane();
            ProjectPanelMenu.setVisible(false);
        }

        refresh();
    }

    public void projectChanged(int tabIndex, Project project) {
        String title = (project.isModified() ? "* " : "") + project.getName();
        tabbedPane.setTitleAt(tabIndex, title);
    }

    public void displayView() {
        List<Image> frameImages = new ArrayList<>();
        frameImages.add(new ImageIcon(MyProCAD.class.getResource("resources/icons/myprocad-logo-16x16.png")).getImage());
        frameImages.add(new ImageIcon(MyProCAD.class.getResource("resources/icons/myprocad-logo-32x32.png")).getImage());
        setIconImages(frameImages);

        // Change component orientation
        applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));

        // Compute frame size and location
        computeFrameBounds();
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        // Enable windows to update their content while window resizing
        getToolkit().setDynamicLayout(true);
        setTitle(MyProCAD.APPLICATION_NAME);

        // Add listeners to model and frame
        addFrameListeners(controller);

        // Show frame
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
    }

    // Control frame closing
    private void addFrameListeners(final ApplicationController controller) {
        addWindowListener(new WindowAdapter () {
            @Override
            public void windowClosing(WindowEvent ev) {
                if (controller.canCloseApplication()) controller.exit();
            }
        });
    }

    private void computeFrameBounds() {
        Dimension screenSize = getUserScreenSize();

        setLocationByPlatform(true);
        pack();
        setSize(screenSize.width * 4 / 5, screenSize.height * 4 / 5);
    }

    private Dimension getUserScreenSize() {
        Dimension screenSize = getToolkit().getScreenSize();
        Insets screenInsets = getToolkit().getScreenInsets(getGraphicsConfiguration());
        screenSize.width -= screenInsets.left + screenInsets.right;
        screenSize.height -= screenInsets.top + screenInsets.bottom;
        return screenSize;
    }

    public void showAboutDialog() {
        String messageFormat = L10.get(L10.ABOUT_MESSAGE);
        String aboutVersion = controller.getVersion();
        String message = String.format(messageFormat, aboutVersion, System.getProperty("java.version"));
        JComponent messagePane = createEditorPane(message);
        messagePane.setOpaque(false);

        String title = L10.get(L10.ABOUT_TITLE);
        Icon icon = new ImageIcon(MyProCAD.class.getResource("resources/about.png"));
        JOptionPane.showMessageDialog(this, messagePane, title, JOptionPane.INFORMATION_MESSAGE, icon);
    }

    private JComponent createEditorPane(String message) {
        // Use an uneditable editor pane to let user select text in dialog
        JEditorPane messagePane = new JEditorPane("text/html", message);
        messagePane.setEditable(false);

        return messagePane;
    }

    public SwingTools.SaveAnswer confirmSave(String fileName) {
        String projectName = new File(fileName).getName();
        String message = L10.get(L10.CONFIRM_SAVE_MESSAGE, projectName);

        String title = L10.get(L10.CONFIRM_SAVE_TITLE);
        String save = L10.get(L10.CONFIRM_SAVE_SAVE);
        String doNotSave = L10.get(L10.CONFIRM_SAVE_DO_NOT_SAVE);
        String cancel = L10.get(L10.CONFIRM_SAVE_CANCEL);

        switch (JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object[]{save, doNotSave, cancel}, save)) {
            // Convert showOptionDialog answer to SaveAnswer enum constants
            case JOptionPane.YES_OPTION: return SwingTools.SaveAnswer.SAVE;
            case JOptionPane.NO_OPTION: return SwingTools.SaveAnswer.DO_NOT_SAVE;
            default: return SwingTools.SaveAnswer.CANCEL;
        }
    }

    public boolean confirmExit() {
        String message = L10.get(L10.CONFIRM_EXIT_MESSAGE);
        String title = L10.get(L10.CONFIRM_EXIT_TITLE);
        String quit = L10.get(L10.CONFIRM_EXIT_QUIT);
        String doNotQuit = L10.get(L10.CONFIRM_EXIT_DO_NOT_QUIT);

        return JOptionPane.showOptionDialog(this, message, title,
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, new Object [] {quit, doNotQuit}, doNotQuit) == JOptionPane.YES_OPTION;
    }

    private void refresh() {
        revalidate();
        repaint();
    }

    public void bringToFront() {
        EventQueue.invokeLater(() -> { toFront(); repaint(); });
    }

    public void setXStatusLabel(String text) {
        xStatusLabel.setText(text);
    }

    public void setYStatusLabel(String text) {
        yStatusLabel.setText(text);
    }

    public void setModeStatusLabel(String text) {
        modeStatusLabel.setText(text);
    }

    public void setStatusPanelVisible(boolean isVisible) { statusPanel.setVisible(isVisible); }
}
