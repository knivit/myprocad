package com.tsoft.myprocad.swing.component;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.swing.SelectionPaintInfo;

import java.awt.*;
import java.awt.geom.*;
import java.util.HashMap;
import java.util.Map;

public class DimensionLineComponent {
    private PlanPanel planPanel;

    private static Map<PointShapeType, GeneralPath> shapes = new HashMap<>();

    static {
        GeneralPath NONE_SHAPE = new GeneralPath();
        shapes.put(PointShapeType.NONE, NONE_SHAPE);

        GeneralPath DIMENSION_SHAPE = new GeneralPath();
        DIMENSION_SHAPE.moveTo(-50, 50);
        DIMENSION_SHAPE.lineTo(50, -50);
        DIMENSION_SHAPE.moveTo(0, 50);
        DIMENSION_SHAPE.lineTo(0, -50);
        shapes.put(PointShapeType.DIMENSION, DIMENSION_SHAPE);

        GeneralPath ARROW_SHAPE = new GeneralPath();
        ARROW_SHAPE.moveTo(-70, 30);
        ARROW_SHAPE.lineTo(0, 0);
        ARROW_SHAPE.lineTo(-70, -30);
        ARROW_SHAPE.lineTo(-70, 30);
        shapes.put(PointShapeType.ARROW, ARROW_SHAPE);

        GeneralPath SQUARE_SHAPE = new GeneralPath();
        SQUARE_SHAPE.moveTo(-25, 25);
        SQUARE_SHAPE.lineTo(0, 25);
        SQUARE_SHAPE.lineTo(0, -25);
        SQUARE_SHAPE.lineTo(-25, -25);
        shapes.put(PointShapeType.SQUARE, SQUARE_SHAPE);

        GeneralPath CIRCLE_SHAPE = new GeneralPath(new Ellipse2D.Float(-25, -25, 50, 50));
        shapes.put(PointShapeType.CIRCLE, CIRCLE_SHAPE);
    }

    public DimensionLineComponent(PlanPanel planPanel) {
        this.planPanel = planPanel;
    }

    // Paint selectionOutlinePaint, Stroke selectionOutlineStroke,
    // Paint indicatorPaint, Stroke extensionLineStroke, float planScale,
    // Color backgroundColor, Color foregroundColor, PlanPanel.PaintMode paintMode,
    public void paintDimensionLines(Graphics2D g2D, ItemList<DimensionLine> dimensionLines, ItemList selectedItems, SelectionPaintInfo selectionPaintInfo) {
        BasicStroke[] strokes = new BasicStroke[DimensionLine.MAX_LINE_WIDTH + 1];

        // Change font size
        Font previousFont = g2D.getFont();
        for (DimensionLine dimensionLine : dimensionLines) {
            AffineTransform previousTransform = g2D.getTransform();

            int wy = dimensionLine.getYEnd() - dimensionLine.getYStart();
            int wx = dimensionLine.getXEnd() - dimensionLine.getXStart();
            double angle = Math.atan2(wy, wx);
            float dimensionLineLength = dimensionLine.getLength();
            g2D.translate(dimensionLine.getXStart(), dimensionLine.getYStart());
            g2D.rotate(angle);
            g2D.translate(0, dimensionLine.getOffset());

            if (selectionPaintInfo.paintMode == PlanPanel.PaintMode.PAINT && selectedItems.contains(dimensionLine)) {
                // Draw selection border
                g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
                g2D.setStroke(selectionPaintInfo.selectionOutlineStroke);

                // Draw dimension line
                g2D.draw(new Line2D.Float(0, 0, dimensionLineLength, 0));

                // Draw dimension line ends
                g2D.draw(shapes.get(dimensionLine.getStartPointShapeType()));
                g2D.translate(dimensionLineLength, 0);
                g2D.draw(shapes.get(dimensionLine.getEndPointShapeType()));
                g2D.translate(-dimensionLineLength, 0);

                // Draw extension lines
                g2D.setPaint(selectionPaintInfo.selectionOutlinePaint);
                g2D.draw(new Line2D.Float(0, -dimensionLine.getOffset(), 0, -5));
                g2D.draw(new Line2D.Float(dimensionLineLength, -dimensionLine.getOffset(), dimensionLineLength, -5));
            }

            BasicStroke dimensionLineStroke = strokes[dimensionLine.getLineWidth()];
            if (dimensionLineStroke == null) {
                dimensionLineStroke = new BasicStroke(dimensionLine.getLineWidth() / selectionPaintInfo.scale);
                strokes[dimensionLine.getLineWidth()] = dimensionLineStroke;
            }
            g2D.setStroke(dimensionLineStroke);

            // Draw dimension line
            g2D.setPaint(new Color(dimensionLine.getColor()));
            g2D.draw(new Line2D.Float(0, 0, dimensionLineLength, 0));

            // Draw dimension line ends
            g2D.draw(shapes.get(dimensionLine.getStartPointShapeType()));
            g2D.translate(dimensionLineLength, 0);
            g2D.draw(shapes.get(dimensionLine.getEndPointShapeType()));
            g2D.translate(-dimensionLineLength, 0);

            // Draw extension lines
            g2D.setPaint(selectionPaintInfo.foregroundColor);
            g2D.setStroke(selectionPaintInfo.locationFeedbackStroke);
            g2D.draw(new Line2D.Float(0, -dimensionLine.getOffset(), 0, -5));
            g2D.draw(new Line2D.Float(dimensionLineLength, -dimensionLine.getOffset(), dimensionLineLength, -5));

            // Draw dimension length in middle
            if (dimensionLine.getFontSize() > 0) {
                FontMetrics lengthFontMetrics = planPanel.getFontMetrics(dimensionLine.getFont());
                String lengthText = dimensionLine.getLengthText();
                Rectangle2D tb = lengthFontMetrics.getStringBounds(lengthText, g2D);
                int fontAscent = lengthFontMetrics.getAscent();
                g2D.translate((dimensionLineLength - (float) tb.getWidth()) / 2,
                        dimensionLine.getOffset() <= 0 ? -lengthFontMetrics.getDescent() - 1 : fontAscent + 1);

                g2D.setPaint(selectionPaintInfo.backgroundColor);
                g2D.fill(new Rectangle(0, -(int) tb.getHeight(), (int) tb.getWidth(), (int) tb.getHeight()));

                g2D.setPaint(selectionPaintInfo.foregroundColor);
                g2D.setFont(dimensionLine.getFont());
                g2D.drawString(lengthText, 0, 0);
            }

            g2D.setTransform(previousTransform);
        }
        g2D.setFont(previousFont);
    }

    public Rectangle2D getItemBounds(Graphics g, Item item, Rectangle2D itemBounds) {
        // Add to bounds the text bounds of the dimension line length
        DimensionLine dimensionLine = (DimensionLine)item;
        float dimensionLineLength = dimensionLine.getLength();
        String lengthText = dimensionLine.getLengthText();

        FontMetrics lengthFontMetrics = planPanel.getFontMetrics(dimensionLine.getFont());
        Rectangle2D lengthTextBounds = lengthFontMetrics.getStringBounds(lengthText, g);

        // Transform length text bounding rectangle corners to their real location
        double angle = Math.atan2(dimensionLine.getYEnd() - dimensionLine.getYStart(), dimensionLine.getXEnd() - dimensionLine.getXStart());
        AffineTransform transform = AffineTransform.getTranslateInstance(dimensionLine.getXStart(), dimensionLine.getYStart());
        transform.rotate(angle);
        transform.translate(0, dimensionLine.getOffset());
        transform.translate((dimensionLineLength - lengthTextBounds.getWidth()) / 2,
                dimensionLine.getOffset() <= 0 ? -lengthFontMetrics.getDescent() - 1 : lengthFontMetrics.getAscent() + 1);
        GeneralPath lengthTextBoundsPath = new GeneralPath(lengthTextBounds);

        for (PathIterator it = lengthTextBoundsPath.getPathIterator(transform); !it.isDone(); ) {
            float [] pathPoint = new float[2];
            if (it.currentSegment(pathPoint) != PathIterator.SEG_CLOSE) {
                itemBounds.add(pathPoint [0], pathPoint [1]);
            }
            it.next();
        }

        // Add to bounds the end lines drawn at dimension line start and end
        transform.setToTranslation(dimensionLine.getXStart(), dimensionLine.getYStart());
        transform.rotate(angle);
        transform.translate(0, dimensionLine.getOffset());
        GeneralPath startPointPath = shapes.get(dimensionLine.getStartPointShapeType());
        for (PathIterator it = startPointPath.getPathIterator(transform); !it.isDone(); ) {
            float [] pathPoint = new float[6];
            if (it.currentSegment(pathPoint) != PathIterator.SEG_CLOSE) {
                itemBounds.add(pathPoint [0], pathPoint [1]);
            }
            it.next();
        }

        transform.translate(dimensionLineLength, 0);
        GeneralPath endPointPath = shapes.get(dimensionLine.getEndPointShapeType());
        for (PathIterator it = endPointPath.getPathIterator(transform); !it.isDone(); ) {
            float [] pathPoint = new float[6];
            if (it.currentSegment(pathPoint) != PathIterator.SEG_CLOSE) {
                itemBounds.add(pathPoint [0], pathPoint [1]);
            }
            it.next();
        }
        return itemBounds;
    }

    public void dimensionLineChanged() {
        planPanel.revalidate();
    }

    public void dimensionLineListChanged() {
        planPanel.revalidate();
    }
}
