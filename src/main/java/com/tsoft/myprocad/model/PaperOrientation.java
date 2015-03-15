package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

public enum PaperOrientation {
    PORTRAIT(1, L10.get(L10.PAPER_ORIENTATION_PORTRAIT)),
    LANDSCAPE(2, L10.get(L10.PAPER_ORIENTATION_LANDSCAPE)),
    REVERSE_LANDSCAPE(3, L10.get(L10.PAPER_ORIENTATION_REVERSE_LANDSCAPE));

    private int id;
    private String text;

    PaperOrientation(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() { return id; }

    @Override
    public String toString() { return text; }

    public static PaperOrientation findById(int paperOrientationId) {
        for (PaperOrientation paperOrientation : values()) {
            if (paperOrientation.getId() == paperOrientationId) return paperOrientation;
        }
        return null;
    }
}
