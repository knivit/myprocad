package com.tsoft.myprocad.swing;

import java.awt.Color;
import java.awt.Paint;
import java.awt.Stroke;

public class SelectionPaintInfo {
    public PlanPanel.PaintMode paintMode;
    public float scale;
    public Color selectionColor;
    public Color backgroundColor;
    public Color foregroundColor;
    public Paint selectionOutlinePaint;
    public Stroke selectionOutlineStroke;
    public Stroke dimensionLinesSelectionOutlineStroke;
    public Stroke locationFeedbackStroke;

    public SelectionPaintInfo(float scale, PlanPanel.PaintMode paintMode) {
        this.scale = scale;
        this.paintMode = paintMode;
    }
}
