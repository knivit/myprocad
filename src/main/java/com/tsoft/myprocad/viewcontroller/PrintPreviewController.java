package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.PrintPreviewPanel;
import com.tsoft.myprocad.swing.PrintableComponent;
import com.tsoft.myprocad.swing.dialog.DialogButton;

public class PrintPreviewController {
    private final PrintableComponent printableComponent;
    private PrintPreviewPanel printPreviewPanel;

    public PrintPreviewController(PrintableComponent printableComponent) {
        this.printableComponent = printableComponent;
    }

    public void displayView() {
        if (printPreviewPanel == null) printPreviewPanel = new PrintPreviewPanel(printableComponent);
        printPreviewPanel.displayView(L10.get(L10.PRINT_PREVIEW_TITLE), DialogButton.CLOSE);
    }
}
