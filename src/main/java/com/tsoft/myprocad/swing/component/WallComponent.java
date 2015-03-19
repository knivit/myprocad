package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Pattern;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.model.WallList;
import com.tsoft.myprocad.model.WallPattern;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.swing.SelectionPaintInfo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class WallComponent {
    private static final float WALL_STROKE_WIDTH = 1.5f; // mm

    private Plan plan;

    private Map<WallPattern, BufferedImage>  patternImagesCache = new HashMap<>();

    public WallComponent(Plan plan) {
        this.plan = plan;
    }

    public void paintWalls(Graphics2D g2D, SelectionPaintInfo selectionPaintInfo) {
        // Adjust scale to 150 dpi for print
        float wallPaintScale = selectionPaintInfo.scale;
        if (selectionPaintInfo.paintMode == PlanPanel.PaintMode.PRINT) {
            wallPaintScale = wallPaintScale / 72 * 150;
        }

        ItemList<Wall> walls = getDrawableWallsInSelectedLevel();
        walls.sortByZLevel(true);
        for (Wall wall : walls) {
            fillAndDrawWallsArea(wall, g2D, wallPaintScale);
        }

        paintForceBorderWalls(g2D, selectionPaintInfo);
        paintSelectedWallsOutline(g2D, selectionPaintInfo);
    }

    private void fillAndDrawWallsArea(Wall wall, Graphics2D g2D, float planScale) {
        Shape shape = wall.getShape();

        // Fill wall's area
        Paint fillPaint = getWallPaint(wall, planScale);
        g2D.setPaint(fillPaint);
        g2D.fill(shape);

        // Draw wall's border
        Paint borderColor = new Color(wall.getBorderColor());
        g2D.setPaint(borderColor);
        g2D.setStroke(new BasicStroke(wall.getBorderWidth() / planScale));
        g2D.draw(shape);
    }

    // Draw borders for walls with 'Always show borders' on
    private void paintForceBorderWalls(Graphics2D g2D, SelectionPaintInfo selectionPaintInfo) {
        WallList showBordersWalls = new WallList();
        for (Wall wall : getDrawableWallsInSelectedLevel()) {
            if (wall.isAlwaysShowBorders()) showBordersWalls.add(wall);
        }

        Color selectionColor = selectionPaintInfo.selectionColor;
        SelectionPaintInfo bordersPaintInfo = new SelectionPaintInfo(selectionPaintInfo.scale, selectionPaintInfo.paintMode);
        bordersPaintInfo.selectionOutlinePaint = new Color(selectionColor.getRed(), selectionColor.getGreen(), selectionColor.getBlue(), 192);
        bordersPaintInfo.selectionOutlineStroke = new BasicStroke(getStrokeWidth(selectionPaintInfo.paintMode) / selectionPaintInfo.scale);
        bordersPaintInfo.foregroundColor = selectionPaintInfo.foregroundColor;
        paintWallsOutline(g2D, showBordersWalls, bordersPaintInfo);
    }

    private void paintSelectedWallsOutline(Graphics2D g2D, SelectionPaintInfo selectionPaintInfo) {
        WallList selectedWalls = new WallList(plan.getController().getSelectedItems().getWallsSubList().atLevel(plan));
        paintWallsOutline(g2D, selectedWalls, selectionPaintInfo);
    }

    /**
     * Paints the outline of walls among <code>items</code> and a resize indicator if
     * <code>items</code> contains only one wall and indicator paint isn't <code>null</code>.
     * Paint selectionOutlinePaint, Stroke selectionOutlineStroke, Paint indicatorPaint, float planScale, Color foregroundColor
     */
    private void paintWallsOutline(Graphics2D g2D, WallList walls, SelectionPaintInfo selectionPaintInfo) {
        // Draw selection border
        for (Wall wall : walls) {
            g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
            g2D.setStroke(selectionPaintInfo.selectionOutlineStroke);
            g2D.draw(wall.getShape());
        }
    }

    /**
     * Returns the <code>Paint</code> object used to fill walls.
     */
    private Paint getWallPaint(Wall wall, float planScale) {
        int backgroundColor = wall.getBackgroundColor();
        int foregroundColor = wall.getForegroundColor();
        Pattern pattern = wall.getPattern();

        WallPattern wallPattern = new WallPattern(pattern.getId(), backgroundColor, foregroundColor);
        BufferedImage patternImage = patternImagesCache.get(wallPattern);
        if (patternImage == null) {
            patternImage = pattern.getPatternImage(backgroundColor, foregroundColor);
            patternImagesCache.put(wallPattern, patternImage);
        }

        return new TexturePaint(patternImage, new Rectangle2D.Float(0, 0, 10 / planScale, 10 / planScale));
    }

    /** Returns the walls that belong to the selected plan's level */
    private WallList getDrawableWallsInSelectedLevel() {
        WallList walls = plan.getLevelWalls();
        return walls;
    }

    public float getStrokeWidth(PlanPanel.PaintMode paintMode) {
        if (paintMode == PlanPanel.PaintMode.PRINT) return WALL_STROKE_WIDTH;
        return WALL_STROKE_WIDTH;
    }
}
