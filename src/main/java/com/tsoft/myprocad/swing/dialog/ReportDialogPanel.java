package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;
import java.awt.*;

public class ReportDialogPanel extends AbstractDialogPanel {
    public ReportDialogPanel(String text) {
        super(new BorderLayout());

        JTextArea ta = new JTextArea(20, 60);
        ta.setLineWrap(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 15));
        ta.setText(text);

        JScrollPane scrollPane = new JScrollPane(ta);

        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public Dimension getDialogPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)Math.round(screenSize.width * 0.7), (int)Math.round(screenSize.height * 0.5));
    }
}
