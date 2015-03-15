package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.LevelMark;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.swing.SelectionPaintInfo;
import java.awt.BasicStroke;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class LevelMarkComponent {
    private static final float BORDER_STROKE_WIDTH = 1f;

    private PlanPanel planPanel;

    private static GeneralPath shape = new GeneralPath();

    static {
        shape.moveTo(-150, 0);
        shape.lineTo(150, 0);
        shape.moveTo(0, 0);
        shape.lineTo(-30, -30);
        shape.moveTo(0, 0);
        shape.lineTo(30, -30);
        shape.moveTo(0, 0);
        shape.lineTo(0, -150);
        shape.lineTo(-300, -150);
    }

    public LevelMarkComponent(PlanPanel planPanel) {
        this.planPanel = planPanel;
    }

    public void paintLevelMarks(Graphics2D g2D, ItemList<LevelMark> levelMarks, ItemList selectedItems, SelectionPaintInfo selectionPaintInfo) {
        Font previousFont = g2D.getFont();

        for (LevelMark levelMark : levelMarks) {
            AffineTransform previousTransform = g2D.getTransform();
            g2D.translate(levelMark.getX(), levelMark.getY());
            g2D.rotate(Math.toRadians(levelMark.getRotation().getAngle()));

            // Draw selection border
            if (PlanPanel.PaintMode.PAINT.equals(selectionPaintInfo.paintMode) && selectedItems.contains(levelMark)) {
                g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
                g2D.setStroke(selectionPaintInfo.selectionOutlineStroke);
                g2D.draw(shape);
            }

            g2D.setPaint(selectionPaintInfo.foregroundColor);
            g2D.setStroke(new BasicStroke(getStrokeWidth(selectionPaintInfo.paintMode) / selectionPaintInfo.scale));
            g2D.draw(shape);

            FontMetrics lengthFontMetrics = planPanel.getFontMetrics(levelMark.getFont());
            Rectangle2D tb = lengthFontMetrics.getStringBounds(levelMark.getText(), g2D);

            int textX = (int)-Math.round(tb.getWidth() / 2) - 150;
            int textY = -200;

            g2D.setFont(levelMark.getFont());
            g2D.setPaint(selectionPaintInfo.backgroundColor);
            g2D.fill(new Rectangle(textX, textY - (int)tb.getHeight(), (int)tb.getWidth(), (int)tb.getHeight()));
            g2D.setPaint(selectionPaintInfo.foregroundColor);
            g2D.drawString(levelMark.getText(), textX, textY);

            g2D.setTransform(previousTransform);
        }
        g2D.setFont(previousFont);
    }

    public float getStrokeWidth(PlanPanel.PaintMode paintMode) {
        if (paintMode == PlanPanel.PaintMode.PRINT) return BORDER_STROKE_WIDTH;
        return BORDER_STROKE_WIDTH;
    }

    public <T> void levelMarkChanged() {
        planPanel.revalidate();
    }

    public void levelMarkListChanged() {
        planPanel.revalidate();
    }
}
