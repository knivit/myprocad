package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.util.StringUtil;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class TextDialog extends AbstractDialogPanel {
    private JTextArea ta;

    private boolean convertLF;

    public TextDialog() {
        super(new BorderLayout());

        ta = new JTextArea(20, 60);
        ta.setLineWrap(false);
        ta.setFont(new Font("Monospaced", Font.PLAIN, 15));

        JScrollPane scrollPane = new JScrollPane(ta);
        add(scrollPane, BorderLayout.CENTER);
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return new Dimension(400, 300);
    }

    public String getText() {
        String result = ta.getText();
        if (convertLF) result = StringUtil.replaceAll(ta.getText(), Character.toString('\n'), "\\n");
        return result;
    }

    public void setText(String text) { setText(text, false); }

    public void setText(String text, boolean convertLF) {
        this.convertLF = convertLF;

        if (text != null && convertLF) text = StringUtil.replaceAll(text, "\\n", Character.toString('\n'));
        ta.setText(text);
    }
}
