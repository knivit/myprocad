package com.tsoft.myprocad.util;

import javax.swing.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.util.concurrent.Callable;
import java.util.logging.Logger;

public class PrintDialog {
    private static final Logger logger = Logger.getLogger(PrintDialog.class.getName());

    private PrintDialog() { }

    public static Callable<Void> show(Printable printable, PageFormat pageFormat, String title) {
        final PrinterJob printerJob = PrinterJob.getPrinterJob();
        printerJob.setPrintable(printable, pageFormat);

        printerJob.setJobName(title);
        if (printerJob.printDialog()) {
            return () -> {
                try {
                    printerJob.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                    logger.severe("Printer error " + ex.getMessage());
                    JOptionPane.showMessageDialog(null, ex.getMessage(), title, JOptionPane.ERROR_MESSAGE);
                }
                return null;
            };
        }
        return null;
    }
}
