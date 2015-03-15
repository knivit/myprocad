package com.tsoft.myprocad.util.printer;

import com.tsoft.myprocad.model.PageSetup;

import java.awt.print.Paper;

public class CustomPaper extends Paper {
    public CustomPaper(PageSetup pageSetup) {
        super();

        // Paper holds values in 1/72 inch
        // PageSetup keeps them in mm
        // Convert mm to 1/72 of an inch
        double w = pageSetup.getPaperWidth() * 25.4 / 72.0;
        double h = pageSetup.getPaperHeight() * 25.4 / 72.0;
        double lm = pageSetup.getPaperLeftMargin() * 25.4 / 72.0;
        double rm = pageSetup.getPaperRightMargin() * 25.4 / 72.0;
        double tm = pageSetup.getPaperTopMargin() * 25.4 / 72.0;
        double bm = pageSetup.getPaperBottomMargin() * 25.4 / 72.0;

        setSize(w, h);
        setImageableArea(lm, tm, w - lm - rm, h - tm - bm);
    }
}
