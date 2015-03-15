package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.viewcontroller.ApplicationController;

import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.text.NumberFormat;

/**
 * A component displaying the plan horizontal or vertical ruler associated to this plan.
 */
public class PlanRulerComponent extends JComponent {
    private PlanPanel planPanel;
    private int orientation;
    private Point mouseLocation;

    public PlanRulerComponent(PlanPanel planPanel, int orientation) {
        this.orientation = orientation;
        this.planPanel = planPanel;

        setOpaque(true);
        setFocusable(false);

        // Use same font as tool tips
        setFont(UIManager.getFont("ToolTip.font"));
        addMouseListeners();
    }

    /**
     * Adds a mouse listener to this ruler that stores current mouse location.
     */
    private void addMouseListeners() {
        MouseInputListener mouseInputListener = new MouseInputAdapter() {
            @Override
            public void mouseDragged(MouseEvent ev) {
                mouseLocation = ev.getPoint();
                repaint();
            }

            @Override
            public void mouseMoved(MouseEvent ev) {
                mouseLocation = ev.getPoint();
                repaint();
            }

            @Override
            public void mouseEntered(MouseEvent ev) {
                mouseLocation = ev.getPoint();
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent ev) {
                mouseLocation = null;
                repaint();
            }
        };

        planPanel.addMouseListener(mouseInputListener);
        planPanel.addMouseMotionListener(mouseInputListener);
        addAncestorListener(new AncestorListener() {
            public void ancestorAdded(AncestorEvent ev) {
                removeAncestorListener(this);
                if (getParent() instanceof JViewport) {
                    ((JViewport)getParent()).addChangeListener(ev1 -> {
                        mouseLocation = MouseInfo.getPointerInfo().getLocation();
                        SwingUtilities.convertPointFromScreen(mouseLocation, planPanel);
                        repaint();
                    });
                }
            }

            public void ancestorRemoved(AncestorEvent ev) {
            }

            public void ancestorMoved(AncestorEvent ev) {
            }
        });
    }

    @Override
    public Dimension getPreferredSize() {
        if (isPreferredSizeSet()) {
            return super.getPreferredSize();
        }

        Insets insets = getInsets();
        Rectangle2D planBounds = planPanel.getPlanBounds();
        FontMetrics metrics = getFontMetrics(getFont());
        int ruleHeight = metrics.getAscent() + 6;
        float scale = planPanel.getPlan().getScale();

        if (orientation == SwingConstants.HORIZONTAL) {
            return new Dimension(Math.round(((float)planBounds.getWidth() + PlanPanel.MARGIN * 2) * scale) + insets.left + insets.right, ruleHeight);
        }

        return new Dimension(ruleHeight, Math.round(((float)planBounds.getHeight() + PlanPanel.MARGIN * 2) * scale) + insets.top + insets.bottom);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2D = (Graphics2D)g.create();
        paintBackground(g2D);
        Insets insets = getInsets();

        // Clip component to avoid drawing in empty borders
        g2D.clipRect(insets.left, insets.top,
                getWidth() - insets.left - insets.right,
                getHeight() - insets.top - insets.bottom);

        // Change component coordinates system to plan system
        Rectangle2D planBounds = planPanel.getPlanBounds();
        float paintScale = planPanel.getPlan().getScale();
        g2D.translate(insets.left + (PlanPanel.MARGIN - planBounds.getMinX()) * paintScale,
                insets.top + (PlanPanel.MARGIN - planBounds.getMinY()) * paintScale);
        g2D.scale(paintScale, paintScale);
        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Paint component contents
        paintRuler(g2D, paintScale);
        g2D.dispose();
    }

    private void paintBackground(Graphics2D g2D) {
        if (isOpaque()) {
            g2D.setColor(getBackground());
            g2D.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    private void paintRuler(Graphics2D g2D, float rulerScale) {
        float gridSize = getGridSize(rulerScale, 1);
        float mainGridSize = getMainGridSize(rulerScale, 1);

        float xMin;
        float yMin;
        float xMax;
        float yMax;
        float xRulerBase;
        float yRulerBase;
        Rectangle2D planBounds = planPanel.getPlanBounds();
        boolean leftToRightOriented = getComponentOrientation().isLeftToRight();
        if (getParent() instanceof JViewport) {
            Rectangle viewRectangle = ((JViewport)getParent()).getViewRect();
            xMin = planPanel.convertXPixelToModel(viewRectangle.x - 1);
            yMin = planPanel.convertYPixelToModel(viewRectangle.y - 1);
            xMax = planPanel.convertXPixelToModel(viewRectangle.x + viewRectangle.width);
            yMax = planPanel.convertYPixelToModel(viewRectangle.y + viewRectangle.height);
            xRulerBase = leftToRightOriented
                    ? planPanel.convertXPixelToModel(viewRectangle.x + viewRectangle.width - 1)
                    : planPanel.convertXPixelToModel(viewRectangle.x);
            yRulerBase = planPanel.convertYPixelToModel(viewRectangle.y + viewRectangle.height - 1);
        } else {
            xMin = (float)planBounds.getMinX() - PlanPanel.MARGIN;
            yMin = (float)planBounds.getMinY() - PlanPanel.MARGIN;
            xMax = planPanel.convertXPixelToModel(getWidth() - 1);
            yRulerBase = yMax = planPanel.convertYPixelToModel(getHeight() - 1);
            xRulerBase = leftToRightOriented ? xMax : xMin;
        }

        FontMetrics metrics = getFontMetrics(getFont());
        int fontAscent = metrics.getAscent();
        float tickSize = 5 / rulerScale;
        float mainTickSize = (fontAscent + 6) / rulerScale;
        NumberFormat format = NumberFormat.getIntegerInstance();

        g2D.setColor(getForeground());
        float lineWidth = 0.5f / rulerScale;
        g2D.setStroke(new BasicStroke(lineWidth));
        if (this.orientation == SwingConstants.HORIZONTAL) {
            // Draw horizontal ruler base
            g2D.draw(new Line2D.Float(xMin, yRulerBase - lineWidth / 2, xMax, yRulerBase - lineWidth  / 2));

            // Draw small ticks
            for (double x = (int)(xMin / gridSize) * gridSize; x < xMax; x += gridSize) {
                g2D.draw(new Line2D.Double(x, yMax - tickSize, x, yMax));
            }
        } else {
            // Draw vertical ruler base
            if (leftToRightOriented) {
                g2D.draw(new Line2D.Float(xRulerBase - lineWidth / 2, yMin, xRulerBase - lineWidth / 2, yMax));
            } else {
                g2D.draw(new Line2D.Float(xRulerBase + lineWidth / 2, yMin, xRulerBase + lineWidth / 2, yMax));
            }

            // Draw small ticks
            for (double y = (int)(yMin / gridSize) * gridSize; y < yMax; y += gridSize) {
                if (leftToRightOriented) {
                    g2D.draw(new Line2D.Double(xMax - tickSize, y, xMax, y));
                } else {
                    g2D.draw(new Line2D.Double(xMin, y, xMin + tickSize, y));
                }
            }
        }

        if (mainGridSize != gridSize) {
            g2D.setStroke(new BasicStroke(1.5f / rulerScale, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL));
            AffineTransform previousTransform = g2D.getTransform();

            // Draw big ticks
            if (this.orientation == SwingConstants.HORIZONTAL) {
                for (double x = ((int)(xMin / mainGridSize) - 1) * mainGridSize; x < xMax; x += mainGridSize) {
                    g2D.draw(new Line2D.Double(x, yMax - mainTickSize, x, yMax));

                    // Draw unit text
                    g2D.translate(x, yMax - mainTickSize);
                    g2D.scale(1 / rulerScale, 1 / rulerScale);
                    g2D.drawString(getFormattedTickText(format, x), 3, fontAscent - 1);
                    g2D.setTransform(previousTransform);
                }
            } else {
                for (double y = ((int)(yMin / mainGridSize) - 1) * mainGridSize; y < yMax; y += mainGridSize) {
                    String yText = getFormattedTickText(format, y);
                    g2D.draw(new Line2D.Double(xMax - mainTickSize, y, xMax, y));

                    // Draw unit text with a vertical orientation
                    g2D.translate(xMax - mainTickSize, y);
                    g2D.scale(1 / rulerScale, 1 / rulerScale);
                    g2D.rotate(-Math.PI / 2);
                    g2D.drawString(yText, -metrics.stringWidth(yText) - 3, fontAscent - 1);
                    g2D.setTransform(previousTransform);
                }
            }
        }

        if (mouseLocation != null) {
            if (orientation == SwingConstants.HORIZONTAL) {
                float x = planPanel.convertXPixelToModel(mouseLocation.x);
                ApplicationController.getInstance().setXStatusLabel(getMouseLocationText(x));
            } else {
                float y = planPanel.convertYPixelToModel(mouseLocation.y);
                ApplicationController.getInstance().setYStatusLabel(getMouseLocationText(y));
            }
/*            Font font = getFont().deriveFont(12f);
            g2D.setFont(font);
            g2D.setColor(planPanel.getSelectionColor());
            g2D.setStroke(new BasicStroke(1 / rulerScale));
            AffineTransform previousTransform = g2D.getTransform();

            if (orientation == SwingConstants.HORIZONTAL) {
                // Draw mouse feedback vertical line
                float x = planPanel.convertXPixelToModel(mouseLocation.x);
                g2D.draw(new Line2D.Float(x, yMax - mainTickSize, x, yMax));

                g2D.translate(x, yMax - mainTickSize);
                g2D.scale(1 / rulerScale, 1 / rulerScale);
                g2D.drawString(getMouseLocationText(x), 3, 15);
                g2D.setTransform(previousTransform);
            } else {
                // Draw mouse feedback horizontal line
                float y = planPanel.convertYPixelToModel(mouseLocation.y);
                g2D.draw(new Line2D.Float(xMax - mainTickSize, y, xMax, y));

                g2D.translate(xMax - mainTickSize, y);
                g2D.scale(1 / rulerScale, 1 / rulerScale);
                g2D.rotate(-Math.PI / 2);
                g2D.drawString(getMouseLocationText(y), 3, fontAscent - 1);
                g2D.setTransform(previousTransform);
            } */
        }
    }

    private String getFormattedTickText(NumberFormat format, double value) {
        String text;
        if (Math.abs(value) < 1E-5) {
            value = 0; // Avoid "-0" text
        }

        // 1m = 1000 mm (items' coordinates stored in mm)
        text = format.format(value / 1000);
        return text;
    }

    private String getMouseLocationText(float value) {
        int m, cm;
        if (value < 0) {
            m = (int) Math.round(Math.ceil(value / 1000));
            cm = Math.round((m * 1000 - value) / 10);
            if (m == 0) cm = -cm;
        } else {
            m = (int) Math.round(Math.floor(value / 1000));
            cm = Math.round((value - m * 1000) / 10);
        }

        // don't show 0 in meters
        if (m == 0) return L10.get(L10.MOUSE_LOCATION_HINT1, cm);
        return L10.get(L10.MOUSE_LOCATION_HINT2, m, cm);
    }

    /**
     * Returns the space between main lines grid.
     */
    public static float getMainGridSize(float gridScale, int pixelsPerUnit) {
        float [] mainGridSizes;

        // Use a grid in mm and meters with a minimum grid increment of 1 m
        mainGridSizes = new float [] { 1000, 2000, 5000, 10000, 20000, 50000, 100000 };

        // Compute grid size to get a grid where the space between each line is less than 50 units
        float mainGridSize = mainGridSizes[0];
        for (int i = 1; i < mainGridSizes.length && mainGridSize * gridScale < 50f / pixelsPerUnit; i++) {
            mainGridSize = mainGridSizes [i];
        }
        return mainGridSize;
    }

    /** Returns the space between lines grid */
    public static float getGridSize(float paintScale, int pixelsPerUnit) {
        float[] gridSizes;

        // Use a grid in mm and meters with a minimum grid increment of 1 cm
        //gridSizes = new float [] { 10, 20, 50, 100, 200, 500, 1000, 2000, 5000, 10000, 20000, 50000, 100000 };
        gridSizes = new float [] { 10, 100, 1000, 10000, 100000 };

        // Compute grid size to get a grid where the space between each line is less than 10 units
        float gridSize = gridSizes[0];
        for (int i = 1; i < gridSizes.length && gridSize * paintScale < 10f / pixelsPerUnit; i++) {
            gridSize = gridSizes [i];
        }
        return gridSize;
    }
}
