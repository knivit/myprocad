package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.swing.PlanPanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JViewport;
import javax.swing.UIManager;

public class PlanGridComponent {
    private PlanPanel planPanel;

    public PlanGridComponent(PlanPanel planPanel) {
        this.planPanel = planPanel;
    }

    public void paintGrid(Graphics2D g2D, float paintScale) {
        float gridSize = PlanRulerComponent.getGridSize(paintScale, 1);
        float mainGridSize = PlanRulerComponent.getMainGridSize(paintScale, 1);

        float xMin, yMin;
        float xMax, yMax;
        Rectangle2D planBounds = planPanel.getPlanBounds();
        if (planPanel.getParent() instanceof JViewport) {
            Rectangle viewRectangle = ((JViewport)planPanel.getParent()).getViewRect();
            xMin = planPanel.convertXPixelToModel(viewRectangle.x - 1);
            yMin = planPanel.convertYPixelToModel(viewRectangle.y - 1);
            xMax = planPanel.convertXPixelToModel(viewRectangle.x + viewRectangle.width);
            yMax = planPanel.convertYPixelToModel(viewRectangle.y + viewRectangle.height);
        } else {
            xMin = (float)planBounds.getMinX() - PlanPanel.MARGIN;
            yMin = (float)planBounds.getMinY() - PlanPanel.MARGIN;
            xMax = planPanel.convertXPixelToModel(planPanel.getWidth());
            yMax = planPanel.convertYPixelToModel(planPanel.getHeight());
        }

        paintGridLines(g2D, paintScale, xMin, xMax, yMin, yMax, gridSize, mainGridSize);
    }

    /**
     * Paints background grid lines from <code>xMin</code> to <code>xMax</code>
     * and <code>yMin</code> to <code>yMax</code>.
     */
    private void paintGridLines(Graphics2D g2D, float gridScale, float xMin, float xMax, float yMin, float yMax, float gridSize, float mainGridSize) {
        g2D.setColor(UIManager.getColor("controlShadow"));
        g2D.setStroke(new BasicStroke(0.5f / gridScale));

        // Draw vertical lines
        for (double x = (int)(xMin / gridSize) * gridSize; x < xMax; x += gridSize) {
            g2D.draw(new Line2D.Double(x, yMin, x, yMax));
        }

        // Draw horizontal lines
        for (double y = (int)(yMin / gridSize) * gridSize; y < yMax; y += gridSize) {
            g2D.draw(new Line2D.Double(xMin, y, xMax, y));
        }

        if (mainGridSize != gridSize) {
            g2D.setStroke(new BasicStroke(1.5f / gridScale, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));

            // Draw main vertical lines
            for (double x = (int)(xMin / mainGridSize) * mainGridSize; x < xMax; x += mainGridSize) {
                g2D.draw(new Line2D.Double(x, yMin, x, yMax));
            }

            // Draw positive main horizontal lines
            for (double y = (int)(yMin / mainGridSize) * mainGridSize; y < yMax; y += mainGridSize) {
                g2D.draw(new Line2D.Double(xMin, y, xMax, y));
            }
        }
    }

    public void print(Graphics2D g2D, float printScale, Rectangle2D printedItemBounds, boolean isGridPrinted, boolean isRulersPrinted) {
        float gridSize = PlanRulerComponent.getGridSize(printScale, 2); // 100f = 10 cm
        float mainGridSize = PlanRulerComponent.getMainGridSize(printScale, 2); // 1000f = 1m

        if (isGridPrinted) printGridLines(g2D, printScale, printedItemBounds, gridSize, mainGridSize);
        if (isRulersPrinted) printGridRulers(g2D, printScale, printedItemBounds, mainGridSize);
    }

    private void printGridLines(Graphics2D g2D, float printScale, Rectangle2D printedItemBounds, float gridSize, float mainGridSize) {
        g2D.setColor(UIManager.getColor("controlShadow"));
        g2D.setStroke(new BasicStroke(0.5f / printScale));

        float xMin = (float)printedItemBounds.getMinX();
        float yMin = (float)printedItemBounds.getMinY();
        float xMax = (float)printedItemBounds.getMaxX();
        float yMax = (float)printedItemBounds.getMaxY();

        // Draw vertical lines
        for (double x = (int)(xMin / gridSize) * gridSize; x < xMax; x += gridSize) {
            g2D.draw(new Line2D.Double(x, yMin, x, yMax));
        }

        // Draw horizontal lines
        for (double y = (int)(yMin / gridSize) * gridSize; y < yMax; y += gridSize) {
            g2D.draw(new Line2D.Double(xMin, y, xMax, y));
        }

        if (mainGridSize != gridSize) {
            g2D.setColor(UIManager.getColor("controlShadow").darker());

            // Draw main vertical lines
            float width = 1.5f / printScale;
            g2D.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            for (double x = (int)(xMin / mainGridSize) * mainGridSize; x < xMax; x += mainGridSize) {
                g2D.draw(new Line2D.Double(x, yMin, x, yMax));
            }

            // Draw positive main horizontal lines
            width = width * (yMax - yMin) / (xMax - xMin);
            g2D.setStroke(new BasicStroke(width, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            for (double y = (int)(yMin / mainGridSize) * mainGridSize; y < yMax; y += mainGridSize) {
                g2D.draw(new Line2D.Double(xMin, y, xMax, y));
            }
        }
    }

    private void printGridRulers(Graphics2D g2D, float printScale, Rectangle2D printedItemBounds, float mainGridSize) {
        g2D.setColor(UIManager.getColor("TextField.foreground"));
        Font labelsFont = planPanel.getFont().deriveFont(8f);
        FontMetrics metrics = planPanel.getFontMetrics(labelsFont);
        g2D.setFont(labelsFont);
        int fontAscent = metrics.getAscent();
        float mainTickSize = (fontAscent + 6) / printScale;

        AffineTransform previousTransform = g2D.getTransform();

        float xMin = (float)printedItemBounds.getMinX();
        float yMin = (float)printedItemBounds.getMinY();
        float xMax = (float)printedItemBounds.getMaxX();
        float yMax = (float)printedItemBounds.getMaxY();

        // Horizontal
        for (double x = ((int)(xMin / mainGridSize) + 1) * mainGridSize; x < xMax; x += mainGridSize) {
            String text = Long.toString(Math.round(x / 1000));

            g2D.translate(x, yMin);
            g2D.scale(2 / printScale, 2 / printScale);
            g2D.drawString(text, 3, fontAscent - 2);
            g2D.setTransform(previousTransform);

            g2D.translate(x, yMax - mainTickSize);
            g2D.scale(2 / printScale, 2 / printScale);
            g2D.drawString(text, 3, fontAscent - 2);
            g2D.setTransform(previousTransform);
        }

        // Vertical
        for (double y = ((int)(yMin / mainGridSize)) * mainGridSize; y < yMax; y += mainGridSize) {
            g2D.translate(xMin, y + mainTickSize);
            g2D.scale(2 / printScale, 2 / printScale);
            g2D.rotate(-Math.PI / 2);
            g2D.drawString(Long.toString(Math.round(y / 1000)), 0, fontAscent);
            g2D.setTransform(previousTransform);
        }
    }
}
