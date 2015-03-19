package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class LevelMark extends Item implements JsonSerializable {
    public static transient final String TYPE_NAME = "LM"; // do not localize

    private String text;
    private String fontFamily = Font.SANS_SERIF;
    private int fontSize = 100;
    private int rotationId = Rotation.ANGLE_0.getId();

    private transient Font font;
    private transient Rotation rotation;

    LevelMark() {
        super();
        setTypeName(ItemType.LEVEL_MARK.getTypeName());
    }

    public int getX() { return xEnd - 150; }

    public void setX(int value) { setXStart(value - 300); setXEnd(value + 150); }

    public int getY() { return  yEnd; }

    public void setY(int value) { setYStart(value - 200); setYEnd(value); }

    public Rotation getRotation() {
        if (rotation == null) rotation = Rotation.findById(rotationId);
        return rotation;
    }

    public void setRotation(Rotation value) {
        if (ObjectUtil.equals(getRotation(), value)) return;

        rotationId = value.getId();
        rotation = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public String getText() {
        if (text == null || (text.length() > 1 && text.charAt(0) == ' ')) {
            int val = Math.round((getRotation().isVertical() ? getX() : getY()) / 10.0f) * 10;
            text = (val >=0 ? " +" : " ") + val;
        }

        return text;
    }

    public void setText(String value) {
        if (ObjectUtil.equals(getText(), value)) return;

        this.text = value;
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

        switch (getRotation()) {
            case ANGLE_0: {
                return new Rectangle2D.Float(x1, y1, dx, dy);
            }
            case ANGLE_90: {
                int xs = x1 + 300;
                int ys = y1 - 100;
                return new Rectangle2D.Float(xs, ys, dx, dy);
            }
            case ANGLE_180: {
                return new Rectangle2D.Float(x1 + 150, y1 + 200, dx, dy);
            }
            case ANGLE_270: {
                int xs = x1 + 100;
                int ys = y1 + 50;
                return new Rectangle2D.Float(xs, ys, dx, dy);
            }
        }
        throw new IllegalArgumentException("Unknown rotation = " + getRotation());
    }

    @Override
    public String toString() {
        return TYPE_NAME + ": " + getId();
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.LEVEL_MARK_TYPE_NAME);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("text", text)
                .write("fontFamily", fontFamily)
                .write("fontSize", fontSize)
                .write("rotationId", rotationId);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defString("text", ((value) -> text = value))
                .defString("fontFamily", ((value) -> fontFamily = value))
                .defInteger("fontSize", ((value) -> fontSize = value))
                .defInteger("rotationId", ((value) -> rotationId = value))
                .read();
    }
}
