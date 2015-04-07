package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Label extends Item implements JsonSerializable {
    public static transient final String TYPE_NAME = "LB"; // do not localize
    public static transient final int MAX_BORDER_WIDTH = 5;

    private String text;
    private String fontFamily = Font.SANS_SERIF;
    private int fontSize = 200;
    private int rotationId = Rotation.ANGLE_0.getId();
    private Color borderColor = Color.BLACK;
    private int borderWidth = 0;

    private transient Font font;
    private transient Rotation rotation;

    Label() {
        super();
        setTypeName(ItemType.LABEL.getTypeName());
    }

    public Rotation getRotation() {
        if (rotation == null) rotation = Rotation.findById(rotationId);
        return rotation;
    }

    public void setRotation(Rotation value) {
        if (ObjectUtil.equals(getRotation(), value)) return;

        rotationId = value.getId();
        rotation = value;
        if (plan != null) plan.itemChanged(this);
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color value) {
        if (borderColor.equals(value)) return;

        borderColor = value;
        if (plan != null) plan.itemChanged(this);
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public String validateBorderWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > MAX_BORDER_WIDTH) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, 0, MAX_BORDER_WIDTH);
        return null;
    }

    public void setBorderWidth(int value) {
        if (borderWidth == value) return;

        borderWidth = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        if (ObjectUtil.equals(this.text, text)) return;

        this.text = text;

        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public String getFontFamily() { return fontFamily; }

    public void setFontFamily(String fontFamily) {
        if (this.fontFamily.equals(fontFamily)) return;

        this.fontFamily = fontFamily;
        font = null;

        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        if (fontSize == this.fontSize) return;

        this.fontSize = fontSize;
        font = null;

        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public Font getFont() {
        if (font == null) {
            font = new Font(fontFamily == null ? Font.SERIF : fontFamily, 0, fontSize);
        }
        return font;
    }

    @Override
    protected Shape getItemShape() {
        int x1 = xStart; int x2 = xEnd;
        if (xEnd < xStart) { x1 = xEnd; x2 = xStart; }
        int y1 = yStart; int y2 = yEnd;
        if (yEnd < yStart) { y1 = yEnd; y2 = yStart; }
        int dx = x2 - x1;
        int dy = y2 - y1;

        return new Rectangle2D.Float(x1, y1, dx, dy);
    }

    public double getArea() {
        double wx = Math.abs(xEnd - xStart);
        double wy = Math.abs(yEnd - yStart);

        // millimeters to m2
        return wx / 1000.0 * wy / 1000.0;
    }

    @Override
    public String toString() {
        return TYPE_NAME + ": " + getId();
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.LABEL_TYPE_NAME);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("text", text)
                .write("fontFamily", fontFamily)
                .write("fontSize", fontSize)
                .write("rotationId", rotationId)
                .write("borderColor", borderColor.getRGB())
                .write("borderWidth", borderWidth);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defString("text", ((value) -> text = value))
                .defString("fontFamily", ((value) -> fontFamily = value))
                .defInteger("fontSize", ((value) -> fontSize = value))
                .defInteger("rotationId", ((value) -> rotationId = value))
                .defInteger("borderColor", ((value) -> borderColor = new Color(value)))
                .defInteger("borderWidth", ((value) -> borderWidth = value))
                .read();
    }
}
