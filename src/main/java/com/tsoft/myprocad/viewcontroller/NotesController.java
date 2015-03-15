package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Notes;
import com.tsoft.myprocad.swing.*;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.NotesPanelMenu;
import com.tsoft.myprocad.util.ContentManager;
import com.tsoft.myprocad.util.PrintDialog;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.util.printer.PrinterUtil;

import javax.swing.*;
import java.awt.print.PageFormat;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Callable;

public class NotesController implements ProjectItemController {
    private ProjectController projectController;
    private Notes notes;
    private NotesPanel notesPanel;
    private boolean isInitialized;

    public static NotesController createNotesController(Notes notes) {
        NotesController notesController = new NotesController(notes);
        notesController.notesPanel = new NotesPanel(notesController);
        return notesController;
    }

    private NotesController(Notes notes) {
        this.notes = notes;
    }

    @Override
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }

    @Override
    public void afterOpen() {
        if (isInitialized) return;
        isInitialized = true;

        notesPanel.setText(notes.getText());
    }

    public void updateNotes() {
        String text = notesPanel.getText();
        if (text.equals(notes.getText())) return;

        notes.setText(text);
        projectController.project.setModified(true);
    }

    @Override
    public void beforeClose() {
        beforeDeactivation();
    }

    @Override
    public void beforeDeactivation() {
        NotesPanelMenu.setVisible(false);
    }

    @Override
    public void afterActivation() {
        NotesPanelMenu.setVisible(true);
    }

    @Override
    public JComponent getParentComponent() {
        return notesPanel;
    }

    private void printNotesToPDF() {
        String fileName = String.format("%s", notes.getName());
        final String pdfName = SwingTools.showSaveDialog(fileName, ContentManager.ContentType.PDF);
        if (pdfName == null) return;

        ThreadedTaskController task = new ThreadedTaskController(L10.get(L10.PRINT_TO_PDF_MESSAGE));
        task.execute(() -> {
            try (OutputStream outputStream = new FileOutputStream(pdfName)) {
                NotesPDFPrinter pdfPrinter = new NotesPDFPrinter(notes);
                pdfPrinter.write(outputStream, fileName);
            }
            return null;
        });
    }

    private void printPreviewNotes() {
        PrintableComponent printableComponent = new NotesPrintableComponent(notes);
        new PrintPreviewController(printableComponent).displayView();
    }

    private void addNotes() { projectController.addProjectItem(); }

    private void deleteNotes() { projectController.deleteProjectItem(notes); }

    public Callable<Void> showPrintDialog() {
        PageFormat pageFormat = PrinterUtil.getPageFormat(notes.getPageSetup());
        PrintDialog.show(new NotesPrintableComponent(notes), pageFormat, notes.getName());
        return null;
    }

    @Override
    public boolean doMenuAction(Menu menu) {
        if (Menu.ADD_NOTES.equals(menu)) { addNotes(); return true; }
        if (Menu.DELETE_NOTES.equals(menu)) { deleteNotes(); return true; }

        if (Menu.NOTES_PRINT_TO_PDF.equals(menu)) { printNotesToPDF(); return true; }
        if (Menu.NOTES_PRINT_PREVIEW.equals(menu)) { printPreviewNotes(); return true; }
        if (Menu.NOTES_PRINT.equals(menu)) { showPrintDialog(); return true; }
        return false;
    }

    @Override
    public void materialListChanged() {
        //
    }
}
