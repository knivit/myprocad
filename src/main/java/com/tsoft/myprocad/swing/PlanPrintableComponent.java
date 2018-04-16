package com.tsoft.myprocad.swing;

import com.tsoft.myprocad.model.PageSetup;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.util.printer.PrinterUtil;
import com.tsoft.myprocad.viewcontroller.PlanController;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.PageFormat;
import java.util.HashSet;
import java.util.Set;

/**
 * A printable component used to print or preview the furniture, the plan
 * and the 3D view of a home.
 */
public class PlanPrintableComponent extends PrintableComponent {
    class PagePrintData {
        public Graphics g;
        public Graphics2D g2D;
        public PageFormat pageFormat;
        public int pageIndex;
        public int pageExists;
    }

    private final PlanController planController;
    private final Font defaultFont;
    private Set<Integer> printablePages = new HashSet<>();

    /**
     * Creates a printable component that will print or display the
     * furniture view, the plan view and 3D view of the <code>home</code>
     * managed by <code>controller</code>.
     */
    public PlanPrintableComponent(PlanController planController, Font defaultFont) {
        super(planController.getPlan().getPageSetup());

        this.planController = planController;
        this.defaultFont = defaultFont;
    }

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        PagePrintData printData = new PagePrintData();
        printData.g = graphics;
        printData.g2D = (Graphics2D)graphics;
        printData.pageFormat = pageFormat;
        printData.pageIndex = pageIndex;

        printPage(printData);
        return printData.pageExists;
    }

    private void printPage(PagePrintData printData) {
        Graphics2D g2D = printData.g2D;
        g2D.setFont(defaultFont);
        g2D.setColor(Color.WHITE);
        g2D.fill(new Rectangle2D.Double(0, 0, printData.pageFormat.getWidth(), printData.pageFormat.getHeight()));
        printData.pageExists = NO_SUCH_PAGE;

        // Try to print next plan view page
        PlanPanel planPanel = planController.planPanel;
        printData.pageExists = planPanel.print(printData.g2D, printData.pageFormat, printData.pageIndex);
        if (printData.pageExists == PAGE_EXISTS && !printablePages.contains(printData.pageIndex)) {
            printablePages.add(printData.pageIndex);
        }
    }

    /**
     * Returns the preferred size of this component according to paper orientation and size
     * of home print attributes.
     */
    @Override
    public Dimension getPreferredSize() {
        Plan plan = planController.getPlan();
        PageSetup pageSetup = plan.getPageSetup();

        PageFormat pageFormat = PrinterUtil.getPageFormat(pageSetup);
        double maxSize = Math.max(pageFormat.getWidth(), pageFormat.getHeight());
        Insets insets = getInsets();
        return new Dimension((int)(pageFormat.getWidth() / maxSize * 400) + insets.left + insets.right,
                (int)(pageFormat.getHeight() / maxSize * 400) + insets.top + insets.bottom);
    }
}