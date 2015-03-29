package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.linealg.Geometry2DLib;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.Color;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

public class Wall extends Item implements JsonSerializable {
    public static transient final int MAX_BORDER_WIDTH = 5;
    public static transient final String TYPE_NAME = "WL"; // do not localize

    private int wallShapeId = WallShape.RECTANGLE.getId();
    private int diagonalWidth = 100; // used in DIAGONAL shape

    private long materialId;
    private int patternId = Pattern.HATCH_UP.getId();
    private int backgroundColor = Color.WHITE.getRGB();
    private int foregroundColor = Color.BLACK.getRGB();
    private int borderColor = Color.BLACK.getRGB();
    private int borderWidth = 1;
    private boolean alwaysShowBorders;
    private boolean skipInReports;

    private transient WallShape wallShape;
    private transient Pattern pattern;
    private transient Material material;
    private transient int diagonalShapeOff1;
    private transient int diagonalShapeOff2;

    Wall() {
        super();
        setTypeName(ItemType.WALL.getTypeName());
    }

    public int getDiagonalWidth() {
        return diagonalWidth;
    }

    public String validateDiagonalWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > Math.max(getXDistance(), getYDistance()))
            return L10.get(L10.ITEM_INVALID_DIAGONAL_WIDTH);
        return null;
    }

    public void setDiagonalWidth(int value) {
        if (diagonalWidth == value) return;

        diagonalWidth = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public long getMaterialId() { return materialId; }

    public Material getMaterial() {
        if (material == null && plan != null) material = plan.getProject().getMaterials().findById(materialId);
        return material;
    }

    public void setMaterial(Material value) {
        if (ObjectUtil.equals(getMaterial(), value)) return;
        materialId = value.getId();
        material = value;

        if (plan != null) plan.itemChanged(this);
    }

    public float getDensity() {
        Material material = getMaterial();
        if (material != null) return material.getDensity();
        return 1;
    }

    public WallShape getWallShape() {
        if (wallShape == null) {
            wallShape = WallShape.findById(wallShapeId);
        }
        return wallShape;
    }

    public void setWallShape(WallShape value) {
        if (ObjectUtil.equals(getWallShape(), value)) return;

        wallShapeId = value.getId();
        wallShape = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public Pattern getPattern() {
        if (pattern == null) {
            pattern = Pattern.findById(patternId);
        }
        return pattern;
    }

    public void setPattern(Pattern value) {
        if (ObjectUtil.equals(getPattern(), value)) return;

        patternId = value.getId();
        pattern = value;
        if (plan != null) plan.itemChanged(this);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int value) {
        if (backgroundColor == value) return;

        backgroundColor = value;
        if (plan != null) plan.itemChanged(this);
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public void setForegroundColor(int value) {
        if (foregroundColor == value) return;

        foregroundColor = value;
        if (plan != null) plan.itemChanged(this);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int value) {
        if (borderColor == value) return;

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

    public boolean isAlwaysShowBorders() { return alwaysShowBorders; }

    public void setAlwaysShowBorders(boolean value) {
        if (alwaysShowBorders == value) return;
        alwaysShowBorders = value;

        if (plan != null) plan.itemChanged(this);
    }

    public boolean isSkipInReports() { return skipInReports; }

    public void setSkipInReports(boolean value) {
        if (skipInReports == value) return;
        skipInReports = value;

        if (plan != null) plan.itemChanged(this);
    }

    public double getVolume() {
       return getArea() * Math.abs(zEnd - zStart) / 1000.0;
    }

    public double getWeight() {
        return getVolume() * getDensity();
    }

    public String getSize() {
        return L10.get(L10.WALL_SIZE_PROPERTY, Math.abs(xEnd - xStart), Math.abs(yEnd - yStart), Math.abs(zEnd - zStart));
    }

    public double getPrice() {
        Material material = getMaterial();
        if (material != null) return getVolume() * material.getPrice();
        return 0;
    }

    public double getArea() {
        int x1 = xStart; int x2 = xEnd;
        if (xEnd < xStart) { x1 = xEnd; x2 = xStart; }
        int y1 = yStart; int y2 = yEnd;
        if (yEnd < yStart) { y1 = yEnd; y2 = yStart; }

        switch (getWallShape()) {
            case RECTANGLE: {
                double dx = Math.abs(xEnd - xStart);
                double dy = Math.abs(yEnd - yStart);
                return dx / 1000.0 * dy / 1000.0;
            }

            case DIAGONAL1: case DIAGONAL2: {
                int offX = (int)Math.round(Math.sqrt(diagonalWidth*diagonalWidth/2.0));
                double dx = (float)Point2D.distance(xStart, yEnd-offX, xEnd-offX, yStart);
                double dy = diagonalWidth;
                return dx / 1000.0 * dy / 1000.0;
            }

            case DIAGONAL1U: case DIAGONAL1D: case DIAGONAL2U: case DIAGONAL2D: {
                int b1 = (int)Math.sqrt(diagonalShapeOff1*diagonalShapeOff1 - diagonalWidth*diagonalWidth);
                int b2 = (int)Math.sqrt(diagonalShapeOff2*diagonalShapeOff2 - diagonalWidth*diagonalWidth);
                int rd = (int)Math.sqrt((x2-x1)*(x2-x1) + (y2-y1)*(y2-y1));
                double area = rd/1000.0*diagonalWidth/1000.0 - (diagonalWidth/1000.0*b1/1000.0)/2.0 -
                        (diagonalWidth/1000.0*b2/1000.0)/2.0;
                return area;
            }

            case TRIANGLE1: case TRIANGLE2: case TRIANGLE3: case TRIANGLE4: {
                double dx = Math.abs(xEnd - xStart);
                double dy = Math.abs(yEnd - yStart);
                return (dx / 1000.0 * dy / 1000.0) / 2;
            }

            case CIRCLE: {
                return Math.PI * getXDistance()/(2 * 1000.0) * getYDistance()/(2 * 1000.0);
            }
        }
        throw new IllegalStateException("Unknown Wall Shape = " + getWallShape());
    }

    /**
     * @param corner square's corner, i.e. "11", "22" etc
     *   +----------+
     *   | \11  21/ |
     *   |12 \  / 22|
     *   |    /\    |
     *   |32/    \42|
     *   |/ 31  41 \|
     *   +----------+
     */
    public int getDiagonalOffset(String corner) {
        double a;
        switch (corner) {
            case "21":case "31": {
                a = Geometry2DLib.getAngleBetweenTwoSections(xStart, yStart, xEnd, yStart, xStart, yEnd, xEnd, yStart);
                a = Math.PI - a;
                return -(int)Math.round(diagonalWidth / Math.sin(a));
            }
            case "32":case "22": {
                a = Geometry2DLib.getAngleBetweenTwoSections(xStart, yStart, xStart, yEnd, xStart, yEnd, xEnd, yStart);
                return (int) Math.round(diagonalWidth / Math.sin(a));
            }
            case "11":case "41": {
                a = Geometry2DLib.getAngleBetweenTwoSections(xEnd, yEnd, xStart, yStart, xStart, yStart, xEnd, yStart);
                return (int) Math.round(diagonalWidth / Math.sin(a));
            }
            case "42":case "12": {
                a = Geometry2DLib.getAngleBetweenTwoSections(xStart, yStart, xEnd, yEnd, xEnd, yEnd, xEnd, yStart);
                a = a - Math.PI;
                return (int) Math.round(diagonalWidth / Math.sin(a));
            }
        }
        throw new IllegalArgumentException("Unknown corner = " + corner);
    }

    @Override
    protected Shape getItemShape() {
        int x1 = xStart; int x2 = xEnd;
        if (xEnd < xStart) { x1 = xEnd; x2 = xStart; }
        int y1 = yStart; int y2 = yEnd;
        if (yEnd < yStart) { y1 = yEnd; y2 = yStart; }
        int dx = x2 - x1;
        int dy = y2 - y1;

        switch (getWallShape()) {
            case RECTANGLE: {
                return new Rectangle2D.Float(x1, y1, dx, dy);
            }
            case DIAGONAL1: {
                int off = (int)Math.round(Math.sqrt(diagonalWidth*diagonalWidth/2.0));
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y2 - off).add(x2 - off, y1);
                ips.add(x2, y1 + off).add(x1 + off, y2);
                return ips.getShape();
            }
            case DIAGONAL1U: {
                diagonalShapeOff1 = getDiagonalOffset("32");
                diagonalShapeOff2 = getDiagonalOffset("21");
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y2 - diagonalShapeOff1).add(x2 - diagonalShapeOff2, y1);
                ips.add(x2, y1).add(x1, y2);
                return ips.getShape();
            }
            case DIAGONAL1D: {
                diagonalShapeOff1 = getDiagonalOffset("22");
                diagonalShapeOff2 = getDiagonalOffset("31");
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y2).add(x2, y1);
                ips.add(x2, y1 + diagonalShapeOff1).add(x1 + diagonalShapeOff2, y2);
                return ips.getShape();
            }
            case DIAGONAL2: {
                int off = (int)Math.round(Math.sqrt(diagonalWidth*diagonalWidth/2.0));
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1 + off).add(x1 + off, y1);
                ips.add(x2, y2 - off).add(x2 - off, y2);
                return ips.getShape();
            }
            case DIAGONAL2U: {
                diagonalShapeOff1 = getDiagonalOffset("11");
                diagonalShapeOff2 = getDiagonalOffset("42");
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1).add(x1 + diagonalShapeOff1, y1);
                ips.add(x2, y2 - diagonalShapeOff2).add(x2, y2);
                return ips.getShape();
            }
            case DIAGONAL2D: {
                diagonalShapeOff1 = getDiagonalOffset("41");
                diagonalShapeOff2 = getDiagonalOffset("12");
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1).add(x2, y2);
                ips.add(x2 - diagonalShapeOff1, y2);
                ips.add(x1, y1 + diagonalShapeOff2);
                return ips.getShape();
            }
            case TRIANGLE1: {
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1).add(x2, y1).add(x1, y2);
                return ips.getShape();
            }
            case TRIANGLE2: {
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1).add(x2, y1).add(x2, y2);
                return ips.getShape();
            }
            case TRIANGLE3: {
                ItemPoints ips = new ItemPoints();
                ips.add(x2, y1).add(x2, y2).add(x1, y2);
                return ips.getShape();
            }
            case TRIANGLE4: {
                ItemPoints ips = new ItemPoints();
                ips.add(x1, y1).add(x1, y2).add(x2, y2);
                return ips.getShape();
            }
            case CIRCLE: {
                return new Ellipse2D.Float(x1, y1, dx, dy);
            }
        }
        throw new IllegalStateException("Unknown Wall Shape = " + getWallShape());
    }

    /** Generate OBJ format */
    public String toObjString(int vno) {
        StringBuilder buf = new StringBuilder();
        String xs = Integer.toString(getXStart());
        String xe = Integer.toString(getXEnd());
        String ys = Integer.toString(getYStart());
        String ye = Integer.toString(getYEnd());
        String zs = Integer.toString(getZStart());
        String ze = Integer.toString(getZEnd());

        // vertexes
        buf.append("v " + xe + " " + ys + " " + zs).append('\n');
        buf.append("v " + xe + " " + ys + " " + ze).append('\n');
        buf.append("v " + xe + " " + ye + " " + zs).append('\n');
        buf.append("v " + xe + " " + ye + " " + ze).append('\n');
        buf.append("v " + xs + " " + ys + " " + zs).append('\n');
        buf.append("v " + xs + " " + ys + " " + ze).append('\n');
        buf.append("v " + xs + " " + ye + " " + zs).append('\n');
        buf.append("v " + xs + " " + ye + " " + ze).append('\n');

        // faces
        buf.append("f " + (vno + 1) + " " + (vno + 7) + " " + (vno + 5)).append('\n');
        buf.append("f " + (vno + 1) + " " + (vno + 3) + " " + (vno + 7)).append('\n');
        buf.append("f " + (vno + 1) + " " + (vno + 4) + " " + (vno + 3)).append('\n');
        buf.append("f " + (vno + 1) + " " + (vno + 2) + " " + (vno + 4)).append('\n');
        buf.append("f " + (vno + 3) + " " + (vno + 8) + " " + (vno + 7)).append('\n');
        buf.append("f " + (vno + 3) + " " + (vno + 4) + " " + (vno + 8)).append('\n');
        buf.append("f " + (vno + 5) + " " + (vno + 7) + " " + (vno + 8)).append('\n');
        buf.append("f " + (vno + 5) + " " + (vno + 8) + " " + (vno + 6)).append('\n');
        buf.append("f " + (vno + 1) + " " + (vno + 5) + " " + (vno + 6)).append('\n');
        buf.append("f " + (vno + 1) + " " + (vno + 6) + " " + (vno + 2)).append('\n');
        buf.append("f " + (vno + 2) + " " + (vno + 6) + " " + (vno + 8)).append('\n');
        buf.append("f " + (vno + 2) + " " + (vno + 8) + " " + (vno + 4)).append('\n');
        return buf.toString();
    }

    @Override
    public String toString() {
        return TYPE_NAME + ": " + getId();
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.WALL_TYPE_NAME) + (material == null ? "" : " " + material);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("wallShapeId", wallShapeId)
                .write("diagonalWidth", diagonalWidth)
                .write("materialId", materialId)
                .write("patternId", patternId)
                .write("backgroundColor", backgroundColor)
                .write("foregroundColor", foregroundColor)
                .write("borderColor", borderColor)
                .write("borderWidth", borderWidth)
                .write("alwaysShowBorders", alwaysShowBorders)
                .write("skipInReports", skipInReports);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defInteger("wallShapeId", ((value) -> wallShapeId = value))
                .defInteger("diagonalWidth", ((value) -> diagonalWidth = value))
                .defLong("materialId", ((value) -> materialId = value))
                .defInteger("patternId", ((value) -> patternId = value))
                .defInteger("backgroundColor", ((value) -> backgroundColor = value))
                .defInteger("foregroundColor", ((value) -> foregroundColor = value))
                .defInteger("borderColor", ((value) -> borderColor = value))
                .defInteger("borderWidth", ((value) -> borderWidth = value))
                .defBoolean("alwaysShowBorders", ((value) -> alwaysShowBorders = value))
                .defBoolean("skipInReports", ((value) -> skipInReports = value))
                .read();
    }
}
