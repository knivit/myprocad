package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

import com.tsoft.myprocad.util.linealg.Geometry2DLib;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;

public class DimensionLine extends Item implements JsonSerializable {
    public static transient final String TYPE_NAME = "DL"; // do not localize
    public static transient final int MAX_LINE_WIDTH = 10;
    public static transient final int MAX_FONT_SIZE = 1000;

    private String text;
    private int offset;
    private String fontFamily = Font.SANS_SERIF;
    private int fontSize = 100;
    private int startPointShapeTypeId = PointShapeType.DIMENSION.getId();
    private int endPointShapeTypeId = PointShapeType.DIMENSION.getId();
    private Color color = Color.BLACK;
    private int lineWidth = 1;

    private transient Font font;
    private transient PointShapeType startPointShapeType;
    private transient PointShapeType endPointShapeType;

    DimensionLine() {
        super();
        setTypeName(ItemType.DIMENSION_LINE.getTypeName());
    }

    public int getOffset() {
        return offset;
    }

    public String validateOffset(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < MIN_COORDINATE|| value > MAX_COORDINATE) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, MIN_COORDINATE, MAX_COORDINATE);
        return null;
    }

    public void setOffset(int value) {
        if (value == offset) return;

        offset = value;
        resetCaches();

        if (plan != null) plan.itemChanged(this);
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color value) {
        if (color.equals(value)) return;

        color = value;
        if (plan != null) plan.itemChanged(this);
    }

    public int getLineWidth() {
        return lineWidth;
    }

    public String validateLineWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 1 || value > MAX_LINE_WIDTH) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, 1, MAX_LINE_WIDTH);
        return null;
    }

    public void setLineWidth(int value) {
        if (lineWidth == value) return;

        lineWidth = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public float getLength() {
        return (float)Point2D.distance(xStart, yStart, xEnd, yEnd);
    }

    public String getFontFamily() { return fontFamily; }

    public void setFontFamily(String fontFamily) {
        if (this.fontFamily.equals(fontFamily)) return;

        this.fontFamily = fontFamily;
        font = null;

        if (plan != null) plan.itemChanged(this);
    }

    public int getFontSize() { return fontSize; }

    public String validateFontSize(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > MAX_FONT_SIZE) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, 0, MAX_FONT_SIZE);
        return null;
    }

    public void setFontSize(int value) {
        if (value == fontSize) return;

        fontSize = value;
        font = null;
        if (plan != null) plan.itemChanged(this);
    }

    public Font getFont() {
        if (font == null) {
            font = new Font(fontFamily == null ? Font.SERIF : fontFamily, 0, fontSize);
        }
        return font;
    }

    public PointShapeType getStartPointShapeType() {
        if (startPointShapeType == null) startPointShapeType = PointShapeType.findById(startPointShapeTypeId);
        return startPointShapeType;
    }

    public void setStartPointShapeType(PointShapeType value) {
        if (getStartPointShapeType().equals(value)) return;

        startPointShapeTypeId = value.getId();
        startPointShapeType = value;
        if (plan != null) plan.itemChanged(this);
    }

    public PointShapeType getEndPointShapeType() {
        if (endPointShapeType == null) endPointShapeType = PointShapeType.findById(endPointShapeTypeId);
        return endPointShapeType;
    }

    public void setEndPointShapeType(PointShapeType value) {
        if (getEndPointShapeType().equals(value)) return;

        endPointShapeTypeId = value.getId();
        endPointShapeType = value;
        if (plan != null) plan.itemChanged(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (ObjectUtil.equals(this.text, text)) return;

        this.text = text;
        if (plan != null) plan.itemChanged(this);
    }

    public String getLengthText() {
        if (StringUtil.isEmpty(text)) return String.format("%d", Math.round(getLength()));
        return text;
    }

    public Integer getAngle(Selection selection) {
        ItemList<DimensionLine> dimensionLines = selection.getItems().getDimensionLinesSubList();

        // For one item, return an angle between it and X-axis
        if (dimensionLines.size() == 1) {
            DimensionLine dl = dimensionLines.get(0);
            double angle = Geometry2DLib.getAngleBetweenTwoSections(dl.getXStart(), dl.getYStart(),
                    dl.getXEnd(), dl.getYEnd(), 0, 0, 10, 0);
            return Math.abs((int)Math.round(Math.toDegrees(angle)));
        }

        // for two items, return an angle between them
        if (dimensionLines.size() == 2) {
            DimensionLine dl1 = dimensionLines.get(0);
            DimensionLine dl2 = dimensionLines.get(1);
            double angle = Geometry2DLib.getAngleBetweenTwoSections(dl1.getXStart(), dl1.getYStart(),
                    dl1.getXEnd(), dl1.getYEnd(), dl2.getXStart(), dl2.getYStart(),
                    dl2.getXEnd(), dl2.getYEnd());
            return Math.abs((int)Math.round(Math.toDegrees(angle)));
        }

        return null;
    }

    /**
     * Returns <code>true</code> if the middle point of this dimension line
     * is the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code>.
     */
    public boolean isMiddlePointAt(float x, float y, float margin) {
        double angle = Math.atan2(yEnd - yStart, xEnd - xStart);
        float dx = (float)-Math.sin(angle) * offset;
        float dy = (float)Math.cos(angle) * offset;
        float xMiddle = (xStart + xEnd) / 2 + dx;
        float yMiddle = (yStart + yEnd) / 2 + dy;

        return Math.abs(x - xMiddle) <= margin && Math.abs(y - yMiddle) <= margin;
    }

    /**
     * Returns <code>true</code> if the extension line at the start of this dimension line
     * contains the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code> around the extension line.
     */
    public boolean containsStartExtensionLinetAt(float x, float y, float margin) {
        double angle = Math.atan2(yEnd - yStart, xEnd - xStart);
        Line2D startExtensionLine = new Line2D.Float(xStart, yStart,
                xStart + (float)-Math.sin(angle) * offset,
                yStart + (float)Math.cos(angle) * offset);

        return containsShapeAtWithMargin(startExtensionLine, x, y, margin);
    }

    /**
     * Returns <code>true</code> if the extension line at the end of this dimension line
     * contains the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code> around the extension line.
     */
    public boolean containsEndExtensionLineAt(float x, float y, float margin) {
        double angle = Math.atan2(yEnd - yStart, xEnd - xStart);
        Line2D endExtensionLine = new Line2D.Float(xEnd, yEnd,
                xEnd + (float)-Math.sin(angle) * offset,
                yEnd + (float)Math.cos(angle) * offset);

        return containsShapeAtWithMargin(endExtensionLine, x, y, margin);
    }

    @Override
    protected GeneralPath getItemShape() {
        GeneralPath shapeCache = new GeneralPath();

        // Create the rectangle that matches piece bounds
        double angle = Math.atan2(yEnd - yStart, xEnd - xStart);
        float dx = (float)-Math.sin(angle) * offset;
        float dy = (float)Math.cos(angle) * offset;

        // Append dimension line
        shapeCache.append(new Line2D.Float(xStart + dx, yStart + dy, xEnd + dx, yEnd + dy), false);

        // Append extension lines
        shapeCache.append(new Line2D.Float(xStart, yStart, xStart + dx, yStart + dy), false);
        shapeCache.append(new Line2D.Float(xEnd, yEnd, xEnd + dx, yEnd + dy), false);

        return shapeCache;
    }

    @Override
    public String toString() {
        return TYPE_NAME + ": " + getId();
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.DIMENSION_LINE_TYPE_NAME);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("text", text)
                .write("offset", offset)
                .write("fontFamily", fontFamily)
                .write("fontSize", fontSize)
                .write("startPointShapeTypeId", startPointShapeTypeId)
                .write("endPointShapeTypeId", endPointShapeTypeId)
                .write("color", color.getRGB())
                .write("lineWidth", lineWidth);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defString("text", ((value) -> text = value))
                .defInteger("offset", ((value) -> offset = value))
                .defString("fontFamily", ((value) -> fontFamily = value))
                .defInteger("fontSize", ((value) -> fontSize = value))
                .defInteger("startPointShapeTypeId", ((value) -> startPointShapeTypeId = value))
                .defInteger("endPointShapeTypeId", ((value) -> endPointShapeTypeId = value))
                .defInteger("color", ((value) -> color = new Color(value)))
                .defInteger("lineWidth", ((value) -> lineWidth = value))
                .read();
    }
}
