package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.model.Application;

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.LayoutManager;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.Locale;
import java.util.function.Supplier;
import javax.swing.*;

public abstract class AbstractDialogPanel extends JPanel {
    private JDialog dialog;
    private Supplier<Boolean> beforeCloseValidator;
    private DialogButton pressedButton;

    public AbstractDialogPanel(LayoutManager layout) {
        super(layout);
    }

    public DialogButton displayView(String title, DialogButton ... buttons) {
        JPanel contentPanel = new JPanel(new BorderLayout());
        JPanel buttonsPanel = createButtonsPanel(buttons);
        contentPanel.add(this, BorderLayout.CENTER);
        contentPanel.add(buttonsPanel, BorderLayout.SOUTH);

        dialog = new JDialog();
        dialog.setModal(true);
        dialog.setTitle(title);
        dialog.setContentPane(contentPanel);
        dialog.applyComponentOrientation(ComponentOrientation.getOrientation(Locale.getDefault()));
        dialog.setResizable(true);
        dialog.setFocusable(true);

        // Pack again because resize decorations may have changed dialog preferred size
        dialog.pack();

        Dimension size = getLastSize(title);
        if (size == null) size = getDialogPreferredSize();
        if (size != null) dialog.setSize(size);
        dialog.setLocationRelativeTo(null);

        // Close on ESC
        dialog.getRootPane().registerKeyboardAction(e -> {
            dialog.setVisible(false);
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_IN_FOCUSED_WINDOW);

        // Set a focus on the first input component
        if (getActiveComponent() != null) {
            SwingUtilities.invokeLater(() -> {
                getActiveComponent().requestFocusInWindow();
            });
        }

        // Show the dialog
        dialog.setVisible(true);

        // for auto-sized windows (with one element, for example) don't store it size
        if (getDialogPreferredSize() != null) setLastSize(title, dialog.getSize());
        dialog.dispose();

        return pressedButton;
    }

    private JPanel createButtonsPanel(DialogButton ... buttons) {
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new BoxLayout(buttonsPanel, BoxLayout.LINE_AXIS));
        buttonsPanel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        buttonsPanel.add(Box.createHorizontalGlue());
        boolean first = true;
        for (DialogButton db : buttons) {
            JButton button = new JButton(db.toString());

            // Only first button (i.e. SAVE, YES, OK etc) triggers the event
            if (first) {
                button.addActionListener(event -> {
                    pressedButton = db;
                    if (beforeCloseValidator != null) {
                        // do not close the Dialog in case the validation failed
                        if (!beforeCloseValidator.get()) return;
                    }
                    dialog.setVisible(false);
                });
            } else {
                button.addActionListener(event -> {
                    pressedButton = db;
                    dialog.setVisible(false);
                });
            }

            buttonsPanel.add(button);
            buttonsPanel.add(Box.createHorizontalStrut(8));
            first = false;
        }

        return buttonsPanel;
    }

    public void setBeforeCloseValidator(Supplier<Boolean> beforeCloseValidator) {
        this.beforeCloseValidator = beforeCloseValidator;
    }

    public JComponent getActiveComponent() {
        return null;
    }

    public Dimension getDialogPreferredSize() {
        return new Dimension(400, 500);
    }

    public void setText(Object value) { }

    private Dimension getLastSize(String title) {
        Dimension size = Application.getInstance().getWindowSize(title);
        if (size == null || !isSizeCorrect(size)) return null;
        return size;
    }

    private void setLastSize(String title, Dimension size) {
        if (isSizeCorrect(size)) Application.getInstance().setWindowSize(title, size);
    }

    private boolean isSizeCorrect(Dimension size) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        if (size.getWidth() < screenSize.getWidth() / 5) return false;
        if (size.getHeight() < screenSize.getHeight() / 5) return false;
        return true;
    }
}
