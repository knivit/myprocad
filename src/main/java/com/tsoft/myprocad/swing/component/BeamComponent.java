package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.model.Beam;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Pattern;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.swing.SelectionPaintInfo;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class BeamComponent {
    private Plan plan;

    private Map<FillPattern, BufferedImage> patternImagesCache = new HashMap<>();

    public BeamComponent(Plan plan) {
        this.plan = plan;
    }

    public void paintBeams(Graphics2D g2D, SelectionPaintInfo selectionPaintInfo) {
        // Adjust scale to 150 dpi for print
        float beamPaintScale = selectionPaintInfo.scale;
        if (selectionPaintInfo.paintMode == PlanPanel.PaintMode.PRINT) {
            beamPaintScale = beamPaintScale / 72 * 150;
        }

        ItemList<Beam> beams = getDrawableBeamsInSelectedLevel();
        beams.sortByZLevel(true);
        for (Beam beam : beams) {
            fillAndDrawWallsArea(beam, g2D, beamPaintScale);
        }

        paintSelectedBeamsOutline(g2D, selectionPaintInfo);
    }

    private void fillAndDrawWallsArea(Beam beam, Graphics2D g2D, float planScale) {
        Shape shape = beam.getShape();

        // Fill wall's area
        Paint fillPaint = getBeamPaint(beam, planScale);
        g2D.setPaint(fillPaint);
        g2D.fill(shape);

        // Draw wall's border
        g2D.setPaint(beam.getBorderColor());
        g2D.setStroke(new BasicStroke(beam.getBorderWidth() / planScale));
        g2D.draw(shape);
    }

    private void paintSelectedBeamsOutline(Graphics2D g2D, SelectionPaintInfo selectionPaintInfo) {
        ItemList<Beam> selectedBeams = new ItemList<>(plan.getController().getSelectedItems().getBeamsSubList().atLevel(plan));
        paintBeamsOutline(g2D, selectedBeams, selectionPaintInfo);
    }

    /**
     * Paints the outline of walls among <code>items</code> and a resize indicator if
     * <code>items</code> contains only one wall and indicator paint isn't <code>null</code>.
     * Paint selectionOutlinePaint, Stroke selectionOutlineStroke, Paint indicatorPaint, float planScale, Color foregroundColor
     */
    private void paintBeamsOutline(Graphics2D g2D, ItemList<Beam> beams, SelectionPaintInfo selectionPaintInfo) {
        // Draw selection border
        for (Beam beam : beams) {
            g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
            g2D.setStroke(selectionPaintInfo.selectionOutlineStroke);
            g2D.draw(beam.getShape());
        }
    }

    /**
     * Returns the <code>Paint</code> object used to fill walls.
     */
    private Paint getBeamPaint(Beam beam, float planScale) {
        Color backgroundColor = beam.getBackgroundColor();
        Color foregroundColor = beam.getForegroundColor();
        Pattern pattern = beam.getPattern();

        FillPattern beamPattern = new FillPattern(pattern.getId(), backgroundColor, foregroundColor);
        BufferedImage patternImage = patternImagesCache.get(beamPattern);
        if (patternImage == null) {
            patternImage = pattern.getPatternImage(backgroundColor, foregroundColor);
            patternImagesCache.put(beamPattern, patternImage);
        }

        return new TexturePaint(patternImage, new Rectangle2D.Float(0, 0, 10 / planScale, 10 / planScale));
    }

    private ItemList<Beam> getDrawableBeamsInSelectedLevel() {
        ItemList<Beam> beams = plan.getLevelBeams();
        return beams;
    }
}
