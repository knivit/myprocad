package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.viewcontroller.NotesController;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public class NotesPanel extends JPanel implements Printable {
    private JTextArea textArea;

    public NotesPanel(NotesController notesController) {
        super(new BorderLayout());

        textArea = new JTextArea(5, 20);
        textArea.setLineWrap(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 15));

        // must save test on focus lost, else menus doesn't work (Print etc)
        textArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                notesController.updateNotes();
            }
        });
        JScrollPane scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);
    }

    public String getText() { return textArea.getText(); }

    public void setText(String text) {
        textArea.setText(text);
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
