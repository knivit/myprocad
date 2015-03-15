package com.tsoft.myprocad.util.printer;

import com.tsoft.myprocad.model.PageSetup;

import java.awt.print.PageFormat;
import java.awt.print.Paper;

public class PrinterUtil {
    private PrinterUtil() { }

    public static PageFormat getPageFormat(PageSetup pageSetup) {
        PageFormat pageFormat = new PageFormat();
        switch (pageSetup.getPaperOrientation()) {
            case PORTRAIT :
                pageFormat.setOrientation(PageFormat.PORTRAIT);
                break;
            case LANDSCAPE :
                pageFormat.setOrientation(PageFormat.LANDSCAPE);
                break;
            case REVERSE_LANDSCAPE :
                pageFormat.setOrientation(PageFormat.REVERSE_LANDSCAPE);
                break;
        }

        Paper paper = new CustomPaper(pageSetup);
        pageFormat.setPaper(paper);
        return pageFormat;
    }
}
