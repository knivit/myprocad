package com.tsoft.myprocad.swing;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.viewcontroller.ThreadedTaskController;

public class ThreadedTaskPanel extends JPanel {
    private final ThreadedTaskController controller;
    private JLabel taskLabel;
    private JProgressBar taskProgressBar;
    private JDialog dialog;
    private boolean taskRunning;

    public ThreadedTaskPanel(String taskMessage, ThreadedTaskController controller) {
        super(new BorderLayout(5, 5));

        this.controller = controller;
        createComponents(taskMessage);
        layoutComponents();
    }

    /**
     * Creates and initializes components.
     */
    private void createComponents(String taskMessage) {
        this.taskLabel = new JLabel(taskMessage);
        this.taskProgressBar = new JProgressBar();
        this.taskProgressBar.setIndeterminate(true);
    }

    /**
     * Layouts panel components in panel with their labels.
     */
    private void layoutComponents() {
        add(this.taskLabel, BorderLayout.NORTH);
        add(this.taskProgressBar, BorderLayout.SOUTH);
    }

    /**
     * Sets the status of the progress bar shown by this panel as indeterminate.
     * This method may be called from an other thread than EDT.
     */
    public void setIndeterminateProgress() {
        if (EventQueue.isDispatchThread()) {
            this.taskProgressBar.setIndeterminate(true);
        } else {
            // Ensure modifications are done in EDT
            invokeLater(new Runnable() {
                public void run() {
                    setIndeterminateProgress();
                }
            });
        }
    }

    /**
     * Sets the current value of the progress bar shown by this panel.
     * This method may be called from an other thread than EDT.
     */
    public void setProgress(final int value, final int minimum, final int maximum) {
        if (EventQueue.isDispatchThread()) {
            this.taskProgressBar.setIndeterminate(false);
            this.taskProgressBar.setValue(value);
            this.taskProgressBar.setMinimum(minimum);
            this.taskProgressBar.setMaximum(maximum);
        } else {
            // Ensure modifications are done in EDT
            invokeLater(new Runnable() {
                public void run() {
                    setProgress(value, minimum, maximum);
                }
            });
        }
    }

    /**
     * Executes <code>runnable</code> asynchronously in the Event Dispatch Thread.
     * Caution !!! This method may be called from an other thread than EDT.
     */
    public void invokeLater(Runnable runnable) {
        EventQueue.invokeLater(runnable);
    }

    /**
     * Sets the running status of the threaded task.
     * If <code>taskRunning</code> is <code>true</code>, a waiting dialog will be shown.
     */
    public void setTaskRunning(boolean taskRunning) {
        this.taskRunning = taskRunning;

        if (taskRunning && dialog == null) {
            final JButton cancelButton = new JButton(L10.get(L10.CANCEL));

            final JOptionPane optionPane = new JOptionPane(this, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION, null, new Object [] {cancelButton});
            cancelButton.addActionListener(ev -> optionPane.setValue(cancelButton));
            dialog = optionPane.createDialog(null, L10.get(L10.THREADED_TASK_DIALOG_TITLE));

            try {
                // Sleep 200 ms before showing dialog to avoid displaying it
                // when the task doesn't last so long
                Thread.sleep(200);
            } catch (InterruptedException ex) {
            }

            if (controller.isTaskRunning()) {
                dialog.setVisible(true);

                dialog.dispose();
                if (taskRunning && (cancelButton == optionPane.getValue()
                        || new Integer(JOptionPane.CLOSED_OPTION).equals(optionPane.getValue()))) {
                    dialog = null;
                    controller.cancelTask();
                }
            }
        } else if (!taskRunning && this.dialog != null) {
            dialog.setVisible(false);
        }
    }
}
