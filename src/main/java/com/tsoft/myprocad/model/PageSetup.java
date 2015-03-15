package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.printer.PaperSize;

import java.io.IOException;

public class PageSetup implements JsonSerializable {
    public enum PrintScale {
        ONE_PAGE(1, null, L10.get(L10.PAPER_PRINT_SCALE_ONE_PAGE)),
        ONE_TO_2(2, 1f/2000, L10.get(L10.PAPER_PRINT_SCALE_ONE_TO_2)),
        ONE_TO_ONE(3, 1f/1000, L10.get(L10.PAPER_PRINT_SCALE_ONE_TO_ONE)),
        TWO_TO_ONE(4, 1f/500, L10.get(L10.PAPER_PRINT_SCALE_2_TO_ONE));

        private int id;
        private Float value;
        private String text;

        PrintScale(int id, Float value, String text) {
            this.id = id;
            this.value = value;
            this.text = text;
        }

        public int getId() { return id; }

        @Override
        public String toString() { return text; }

        public static PrintScale findById(int printScaleId) {
            for (PrintScale printScale : values()) {
                if (printScale.getId() == printScaleId) return printScale;
            }
            return null;
        }
    }

    private int paperOrientationId = PaperOrientation.PORTRAIT.getId();
    private int paperSizeId = PaperSize.A4.getId();
    private int paperWidth = PaperSize.A4.getWidth();
    private int paperHeight = PaperSize.A4.getHeight();
    private int paperTopMargin = 10;
    private int paperLeftMargin = 15;
    private int paperBottomMargin = 10;
    private int paperRightMargin = 10;
    private boolean rulersPrinted = true;
    private boolean gridPrinted = true;
    private int printScaleId = PrintScale.ONE_PAGE.getId();

    private transient PaperOrientation paperOrientation;
    private transient PaperSize paperSize;
    private transient PrintScale printScale;

    public PaperOrientation getPaperOrientation() {
        if (paperOrientation == null) paperOrientation = PaperOrientation.findById(paperOrientationId);
        return paperOrientation;
    }

    public void setPaperOrientation(PaperOrientation paperOrientation) {
        paperOrientationId = paperOrientation.getId();
        this.paperOrientation = paperOrientation;
    }

    public PaperSize getPaperSize() {
        if (paperSize == null) paperSize = PaperSize.findById(paperSizeId);
        return paperSize;
    }

    public void setPaperSize(PaperSize paperSize) {
        paperSizeId = paperSize.getId();
        this.paperSize = paperSize;
    }

    public int getPaperWidth() {
        return paperWidth;
    }

    public void setPaperWidth(int paperWidth) {
        this.paperWidth = paperWidth;
    }

    public int getPaperHeight() {
        return paperHeight;
    }

    public void setPaperHeight(int paperHeight) {
        this.paperHeight = paperHeight;
    }

    public int getPaperTopMargin() {
        return paperTopMargin;
    }

    public void setPaperTopMargin(int paperTopMargin) {
        this.paperTopMargin = paperTopMargin;
    }

    public int getPaperLeftMargin() {
        return paperLeftMargin;
    }

    public void setPaperLeftMargin(int paperLeftMargin) {
        this.paperLeftMargin = paperLeftMargin;
    }

    public int getPaperBottomMargin() {
        return paperBottomMargin;
    }

    public void setPaperBottomMargin(int paperBottomMargin) {
        this.paperBottomMargin = paperBottomMargin;
    }

    public int getPaperRightMargin() {
        return paperRightMargin;
    }

    public void setPaperRightMargin(int paperRightMargin) {
        this.paperRightMargin = paperRightMargin;
    }

    public boolean isRulersPrinted() {
        return rulersPrinted;
    }

    public void setRulersPrinted(boolean rulersPrinted) {
        this.rulersPrinted = rulersPrinted;
    }

    public boolean isGridPrinted() {
        return gridPrinted;
    }

    public void setGridPrinted(boolean gridPrinted) {
        this.gridPrinted = gridPrinted;
    }

    public PrintScale getPrintScale() {
        if (printScale == null) printScale = PrintScale.findById(printScaleId);
        return printScale;
    }

    public void setPrintScale(PrintScale printScale) {
        printScaleId = printScale.getId();
        this.printScale = printScale;
    }

    public Float getPrintScaleValue() {
        return getPrintScale().value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PageSetup pageSetup = (PageSetup) o;

        if (gridPrinted != pageSetup.gridPrinted) return false;
        if (paperBottomMargin != pageSetup.paperBottomMargin) return false;
        if (paperHeight != pageSetup.paperHeight) return false;
        if (paperLeftMargin != pageSetup.paperLeftMargin) return false;
        if (paperRightMargin != pageSetup.paperRightMargin) return false;
        if (paperTopMargin != pageSetup.paperTopMargin) return false;
        if (paperWidth != pageSetup.paperWidth) return false;
        if (rulersPrinted != pageSetup.rulersPrinted) return false;
        if (paperOrientation != pageSetup.paperOrientation) return false;
        if (paperSize != pageSetup.paperSize) return false;
        if (printScale != pageSetup.printScale) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = paperOrientation.hashCode();
        result = 31 * result + paperSize.hashCode();
        result = 31 * result + paperWidth;
        result = 31 * result + paperHeight;
        result = 31 * result + paperTopMargin;
        result = 31 * result + paperLeftMargin;
        result = 31 * result + paperBottomMargin;
        result = 31 * result + paperRightMargin;
        result = 31 * result + (rulersPrinted ? 1 : 0);
        result = 31 * result + (gridPrinted ? 1 : 0);
        result = 31 * result + printScale.hashCode();
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("paperOrientationId", paperOrientationId)
                .write("paperSizeId", paperSizeId)
                .write("paperWidth", paperWidth)
                .write("paperHeight", paperHeight)
                .write("paperTopMargin", paperTopMargin)
                .write("paperLeftMargin", paperLeftMargin)
                .write("paperBottomMargin", paperBottomMargin)
                .write("paperRightMargin", paperRightMargin)
                .write("rulersPrinted", rulersPrinted)
                .write("gridPrinted", gridPrinted)
                .write("printScaleId", printScaleId);

    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defInteger("paperOrientationId", ((value) -> paperOrientationId = value))
                .defInteger("paperSizeId", ((value) -> paperSizeId = value))
                .defInteger("paperWidth", ((value) -> paperWidth = value))
                .defInteger("paperHeight", ((value) -> paperHeight = value))
                .defInteger("paperTopMargin", ((value) -> paperTopMargin = value))
                .defInteger("paperLeftMargin", ((value) -> paperLeftMargin = value))
                .defInteger("paperBottomMargin", ((value) -> paperBottomMargin = value))
                .defInteger("paperRightMargin", ((value) -> paperRightMargin = value))
                .defBoolean("rulersPrinted", ((value) -> rulersPrinted = value))
                .defBoolean("gridPrinted", ((value) -> gridPrinted = value))
                .defInteger("printScaleId", ((value) -> printScaleId = value))
                .read();
    }
}
