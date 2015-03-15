package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.swing.PlanPrintableComponent;
import com.tsoft.myprocad.util.printer.PrinterUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.io.IOException;
import java.io.OutputStream;

public class PlanPDFPrinter {
    private final Plan plan;
    private final Font defaultFont;

    public PlanPDFPrinter(Plan plan, Font defaultFont) {
        this.plan = plan;
        this.defaultFont = defaultFont;
    }

    public void write(OutputStream outputStream, String title) throws IOException {
        PageFormat pageFormat = PrinterUtil.getPageFormat(plan.getPageSetup());
        Document pdfDocument = new Document(new Rectangle((float)pageFormat.getWidth(), (float)pageFormat.getHeight()));

        try {
            // Get a PDF writer that will write to the given PDF output stream
            PdfWriter pdfWriter = PdfWriter.getInstance(pdfDocument, outputStream);
            pdfDocument.open();

            // Set PDF document description
            pdfDocument.addAuthor(System.getProperty("user.name", ""));
            String pdfDocumentCreator = L10.get(L10.APPLICATION_NAME);
            pdfDocument.addCreator(pdfDocumentCreator);
            pdfDocument.addCreationDate();
            pdfDocument.addTitle(title);

            PdfContentByte pdfContent = pdfWriter.getDirectContent();

            PlanPrintableComponent printableComponent = new PlanPrintableComponent(plan.getController(), defaultFont);

            // Print each page
            for (int page = 0, pageCount = printableComponent.getPageCount(); page < pageCount; page++) {
                PdfTemplate pdfTemplate = pdfContent.createTemplate((float) pageFormat.getWidth(), (float) pageFormat.getHeight());
                Graphics g = pdfTemplate.createGraphicsShapes((float) pageFormat.getWidth(), (float) pageFormat.getHeight());

                printableComponent.print(g, pageFormat, page);

                pdfContent.addTemplate(pdfTemplate, 0, 0);
                g.dispose();

                if (page != pageCount - 1) {
                    pdfDocument.newPage();
                }
            }

            pdfDocument.close();
        } catch (Exception ex) {
            ex.printStackTrace();

            IOException exception = new IOException("Couldn't print to PDF");
            exception.initCause(ex);
            throw exception;
        }
    }
}
