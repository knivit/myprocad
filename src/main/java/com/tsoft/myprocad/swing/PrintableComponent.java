package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.model.PageSetup;
import com.tsoft.myprocad.util.printer.PrinterUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public abstract class PrintableComponent extends JComponent implements Printable {
    private PageSetup pageSetup;

    private int page;
    private int pageCount = -1;

    public PrintableComponent(PageSetup pageSetup) { this.pageSetup = pageSetup; }

    /** Sets the page currently painted by this component */
    public void setPage(int page) {
        if (this.page != page) {
            this.page = page;
            repaint();
        }
    }

    /** Returns the page currently painted by this component */
    public int getPage() {
        return page;
    }

    /** Returns the page count of the home printed by this component */
    public int getPageCount() {
        if (pageCount != -1) {
            return pageCount;
        }

        PageFormat pageFormat = PrinterUtil.getPageFormat(pageSetup);
        BufferedImage dummyImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
        Graphics dummyGraphics = dummyImage.getGraphics();

        // Count pages by printing in a dummy image
        pageCount = 0;
        try {
            while (print(dummyGraphics, pageFormat, pageCount) == Printable.PAGE_EXISTS) {
                pageCount++;
            }
        } catch (PrinterException ex) {
            // There should be no reason that print fails if print is done on a dummy image
            throw new RuntimeException(ex);
        }
        dummyGraphics.dispose();

        return pageCount;
    }

    /** Paints the current page */
    @Override
    protected void paintComponent(Graphics g) {
        try {
            Graphics2D g2D = (Graphics2D)g.create();

            PageFormat pageFormat = PrinterUtil.getPageFormat(pageSetup);
            Insets insets = getInsets();
            double scale = (getWidth() - insets.left - insets.right) / pageFormat.getWidth();
            g2D.scale(scale, scale);
            print(g2D, pageFormat, getPage());
            g2D.dispose();
        } catch (PrinterException ex) {
            throw new RuntimeException(ex);
        }
    }
}
