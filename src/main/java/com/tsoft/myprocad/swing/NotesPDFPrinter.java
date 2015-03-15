package com.tsoft.myprocad.swing;

import java.awt.Graphics;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.io.IOException;
import java.io.OutputStream;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Notes;
import com.tsoft.myprocad.util.printer.PrinterUtil;
import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class NotesPDFPrinter {
    private Notes notes;

    public NotesPDFPrinter(Notes notes) {
        this.notes = notes;
    }

    public void write(OutputStream outputStream, String title) throws IOException {
        PageFormat pageFormat = PrinterUtil.getPageFormat(notes.getPageSetup());
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

            NotesPrintableComponent printableComponent = new NotesPrintableComponent(notes);

            int page = 0;
            while (true) {
                PdfTemplate pdfTemplate = pdfContent.createTemplate((float) pageFormat.getWidth(), (float) pageFormat.getHeight());
                Graphics g = pdfTemplate.createGraphicsShapes((float) pageFormat.getWidth(), (float) pageFormat.getHeight());

                int result = printableComponent.print(g, pageFormat, page);
                g.dispose();
                if (result == Printable.NO_SUCH_PAGE) break;

                pdfContent.addTemplate(pdfTemplate, 0, 0);
                pdfDocument.newPage();
                page ++;
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
