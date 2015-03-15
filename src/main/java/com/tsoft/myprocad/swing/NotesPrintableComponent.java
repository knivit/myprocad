package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.model.Notes;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.Size2DSyntax;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.OrientationRequested;
import javax.swing.JTextArea;

public class NotesPrintableComponent extends PrintableComponent {
    private Notes notes;
    private Printable printable;

    public NotesPrintableComponent(Notes notes) {
        super(notes.getPageSetup());

        this.notes = notes;
        initPrintable();
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        int code = printable.print(graphics, pageFormat, pageIndex);
        return code;
    }

    private void initPrintable() {
        PrintRequestAttributeSet attr = new HashPrintRequestAttributeSet();
        float leftMargin = 15;
        float rightMargin = 10;
        float topMargin = 10;
        float bottomMargin = 10;
        attr.add(OrientationRequested.PORTRAIT);
        attr.add(MediaSizeName.ISO_A4);
        MediaSize mediaSize = MediaSize.ISO.A4;
        float mediaWidth = mediaSize.getX(Size2DSyntax.MM);
        float mediaHeight = mediaSize.getY(Size2DSyntax.MM);
        attr.add(new MediaPrintableArea(leftMargin, topMargin,
                (mediaWidth - leftMargin - rightMargin),
                (mediaHeight - topMargin - bottomMargin), Size2DSyntax.MM));

        JTextArea textArea = new JTextArea(notes.getText());
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 25));
        printable = textArea.getPrintable(null, null);
    }
}
