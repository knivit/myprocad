package com.tsoft.myprocad.util.printer;

import com.tsoft.myprocad.l10n.L10;

public enum PaperSize {
    A4(1, 2100, 2970, L10.get(L10.PAPER_SIZE_A4)),
    A3(2, 2970, 4200, L10.get(L10.PAPER_SIZE_A3)),
    A2(3, 4200, 5940, L10.get(L10.PAPER_SIZE_A2)),
    Custom(4, 1, 1, L10.get(L10.PAPER_SIZE_CUSTOM));

    private int id;
    private int width;
    private int height;
    private String text;

    PaperSize(int id, int width, int height, String text) {
        this.id = id;
        this.width = width;
        this.height = height;
        this.text = text;
    }

    public int getId() { return id; }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    @Override
    public String toString() { return text; }

    public static PaperSize findById(int paperSizeId) {
        for (PaperSize paperSize : values()) {
            if (paperSize.getId() == paperSizeId) return paperSize;
        }
        return null;
    }
}
