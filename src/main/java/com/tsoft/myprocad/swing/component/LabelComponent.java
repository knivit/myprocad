package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.swing.PlanPanel;

import com.tsoft.myprocad.swing.SelectionPaintInfo;
import com.tsoft.myprocad.util.StringUtil;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.List;

public class LabelComponent {
    private Plan plan;
    private PlanPanel planPanel;

    private static final Stroke INDICATOR_STROKE = new BasicStroke(1.5f);
    private static final GeneralPath TEXT_LOCATION_INDICATOR;
    private static final Shape LABEL_CENTER_INDICATOR;

    static {
        // Create a path that draws three arrows going left, right and down
        TEXT_LOCATION_INDICATOR = new GeneralPath();
        TEXT_LOCATION_INDICATOR.append(new Arc2D.Float(-2, 0, 4, 4, 190, 160, Arc2D.CHORD), false);
        TEXT_LOCATION_INDICATOR.moveTo(0, 4);        // Down line
        TEXT_LOCATION_INDICATOR.lineTo(0, 12);
        TEXT_LOCATION_INDICATOR.moveTo(-1.2f, 8.5f); // Down arrow
        TEXT_LOCATION_INDICATOR.lineTo(0f, 11.5f);
        TEXT_LOCATION_INDICATOR.lineTo(1.2f, 8.5f);
        TEXT_LOCATION_INDICATOR.moveTo(2f, 3f);      // Right line
        TEXT_LOCATION_INDICATOR.lineTo(9, 6);
        TEXT_LOCATION_INDICATOR.moveTo(6, 6.5f);     // Right arrow
        TEXT_LOCATION_INDICATOR.lineTo(10, 7);
        TEXT_LOCATION_INDICATOR.lineTo(7.5f, 3.5f);
        TEXT_LOCATION_INDICATOR.moveTo(-2f, 3f);     // Left line
        TEXT_LOCATION_INDICATOR.lineTo(-9, 6);
        TEXT_LOCATION_INDICATOR.moveTo(-6, 6.5f);    // Left arrow
        TEXT_LOCATION_INDICATOR.lineTo(-10, 7);
        TEXT_LOCATION_INDICATOR.lineTo(-7.5f, 3.5f);

        LABEL_CENTER_INDICATOR = new Ellipse2D.Float(-1f, -1f, 2, 2);
    }

    public LabelComponent(PlanPanel planPanel) {
        this.planPanel = planPanel;
        this.plan = planPanel.getPlan();
    }

    public void paintLabels(Graphics2D g2D, List<Item> selectedItems, SelectionPaintInfo selectionPaintInfo) {
        Font previousFont = g2D.getFont();

        try {
            for (Label label : plan.getLevelLabels()) {
                paintLabel(g2D, label, selectionPaintInfo);

                boolean selectedLabel = selectedItems.contains(label);
                if (selectionPaintInfo.paintMode == PlanPanel.PaintMode.PAINT && selectedLabel) {
                    paintSelectionBorder(g2D, label, selectionPaintInfo);
                }
            }
        } finally {
            g2D.setFont(previousFont);
        }
    }

    /**
     * Paints the given text in the rectangle
     */
    private void paintLabel(Graphics2D g2D, Label label, SelectionPaintInfo selectionPaintInfo) {
        if (label.getBorderWidth() > 0) {
            g2D.setColor(new Color(label.getBorderColor()));
            BasicStroke labelStroke = new BasicStroke(label.getBorderWidth() / selectionPaintInfo.scale);
            g2D.setStroke(labelStroke);
            g2D.draw(label.getShape());
        }

        String text = label.getText();
        if (StringUtil.isEmpty(text)) return;

        AffineTransform previousTransform = g2D.getTransform();
        switch (label.getRotation()) {
            case ANGLE_0: { g2D.translate(label.getXStart(), label.getYStart()); break; }
            case ANGLE_90: { g2D.translate(label.getXEnd(), label.getYStart()); break; }
            case ANGLE_180: { g2D.translate(label.getXEnd(), label.getYEnd()); break; }
            case ANGLE_270: { g2D.translate(label.getXStart(), label.getYEnd()); break; }
        }
        g2D.rotate(Math.toRadians(label.getRotation().getAngle()));

        g2D.setFont(label.getFont());
        g2D.setPaint(selectionPaintInfo.foregroundColor);
        FontMetrics fontMetrics = planPanel.getFontMetrics(label.getFont());
        AttributedString attributedString = new AttributedString(text, fontMetrics.getFont().getAttributes());

        // Create a new LineBreakMeasurer from the paragraph
        AttributedCharacterIterator paragraph = attributedString.getIterator();
        int paragraphStart = paragraph.getBeginIndex();
        int paragraphEnd = paragraph.getEndIndex();
        FontRenderContext frc = g2D.getFontRenderContext();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(paragraph, frc);

        // Set break width to width of Component
        float breakWidth = label.getRotation().isVertical() ? (float)label.getYDistance() : (float)label.getXDistance();
        float drawPosY = 0, priorDrawPosY = 0;

        // Set position to the index of the first character in the paragraph
        lineMeasurer.setPosition(paragraphStart);

        // Get lines until the entire paragraph has been displayed
        while (lineMeasurer.getPosition() < paragraphEnd) {
            int next = lineMeasurer.nextOffset(breakWidth);
            int limit = next;
            int charat = text.indexOf('\n', lineMeasurer.getPosition() + 1);
            if (next > (charat - lineMeasurer.getPosition()) && charat != -1){
                limit = charat - lineMeasurer.getPosition();
            }
            TextLayout layout = lineMeasurer.nextLayout(breakWidth, lineMeasurer.getPosition() + limit, false);

            // Compute pen x position. If the paragraph is right-to-left we
            // will align the TextLayouts to the right edge of the panel.
            // Note: this won't occur for the English text in this sample.
            // Note: drawPosX is always where the LEFT of the text is placed.
            float drawPosX = layout.isLeftToRight() ? 0 : breakWidth - layout.getAdvance();

            // Move y-coordinate by the ascent of the layout.
            drawPosY += layout.getAscent();

            // Draw the TextLayout at (drawPosX, drawPosY).
            layout.draw(g2D, drawPosX, drawPosY);

            // Move y-coordinate in preparation for next layout
            priorDrawPosY = drawPosY;
            drawPosY += layout.getDescent() + layout.getLeading();
        }

        // if text doesn't fit, make the area bigger
        label.stopNotifications();
        if (label.getRotation().isVertical()) {
            if (label.getXStart() > (label.getXEnd() - priorDrawPosY)) {
                label.setXStart(label.getXEnd() - (int) Math.ceil(priorDrawPosY));
            }
        } else {
            if (label.getYEnd() < (label.getYStart() + priorDrawPosY)) {
                label.setYEnd(label.getYStart() + (int) Math.ceil(priorDrawPosY));
            }
        }
        label.startNotifications();

        g2D.setTransform(previousTransform);
    }

    private void paintSelectionBorder(Graphics2D g2D, Label label, SelectionPaintInfo selectionPaintInfo) {
        // Draw selection border
        g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
        g2D.setStroke(selectionPaintInfo.selectionOutlineStroke);
        g2D.draw(label.getShape());

        g2D.setPaint(selectionPaintInfo.foregroundColor);
        if (selectionPaintInfo.selectionColor != null) {
            paintTextIndicators(g2D, label, selectionPaintInfo.selectionColor, selectionPaintInfo.scale);
        }
    }

    /**
     * Paints text location and angle indicators at the given coordinates.
     */
    private void paintTextIndicators(Graphics2D g2D, Label label, Paint indicatorPaint, float planScale) {
        g2D.setPaint(indicatorPaint);
        g2D.setStroke(INDICATOR_STROKE);
        AffineTransform previousTransform = g2D.getTransform();
        float scaleInverse = 1 / planScale;
        g2D.translate(label.getXStart(), label.getYStart());
        g2D.scale(scaleInverse, scaleInverse);
        g2D.draw(LABEL_CENTER_INDICATOR);

        FontMetrics fontMetrics = planPanel.getFontMetrics(label.getFont());
        g2D.setTransform(previousTransform);
        g2D.translate(label.getXStart(), label.getYStart());
        g2D.translate(0, -fontMetrics.getAscent());
        g2D.scale(scaleInverse, scaleInverse);
        g2D.setTransform(previousTransform);
    }
}
