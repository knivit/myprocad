package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.UUID;

public abstract class Item implements Cloneable, JsonSerializable {
    public static transient int MIN_COORDINATE = -50000; // -50m .. +50m
    public static transient int MAX_COORDINATE = 50000;

    private String typeName;
    protected int xStart;
    protected int xEnd;
    protected int yStart;
    protected int yEnd;
    protected int zStart;
    protected int zEnd;

    protected abstract Shape getItemShape();

    private transient String id;
    public transient Plan plan;
    private transient Plan plan_save; // keeps the plan references when notifications were switched off
    private transient Shape shapeCache;

    public void setXStart(int value) {
        if (xStart == value) return;

        xStart = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public void setXEnd(int value) {
        if (xEnd == value) return;

        xEnd = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public void setYStart(int value) {
        if (yStart == value) return;

        yStart = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public void setYEnd(int value) {
        if (yEnd == value) return;

        yEnd = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public void setZStart(int value) {
        if (zStart == value) return;

        zStart = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public void setZEnd(int value) {
        if (zEnd == value) return;

        zEnd = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public abstract String getPopupItemName();

    protected Item() {
        generateId();
    }

    public String getId() { return id; }

    private void generateId() {
        id = UUID.randomUUID().toString();
    }

    public String getTypeName() { return typeName; }

    public void setPlan(Plan value) {
        plan = value;
    }

    public void stopNotifications() {
        plan_save = plan;
        plan = null;
    }

    public void startNotifications() {
        plan = plan_save;
        plan_save = null;
        resetCaches();
    }

    public void populateFrom(Item item) {
        xStart = item.xStart;
        xEnd = item.xEnd;
        yStart = item.yStart;
        yEnd = item.yEnd;
        zStart = item.zStart;
        zEnd = item.zEnd;

        resetCaches();
    }

    public static String validateCoordinate(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < MIN_COORDINATE || value > MAX_COORDINATE)
            return L10.get(L10.ITEM_INVALID_COORDINATE, MIN_COORDINATE, MAX_COORDINATE);
        return null;
    }

    public void setTypeName(String typeName) { this.typeName = typeName; }

    public static Item newInstance(String typeName) {
        ItemType type = ItemType.findById(typeName);
        if (type == null) throw new IllegalArgumentException("Unknown typeName " + typeName);

        return type.newInstance();   }

    public void normalizeStartAndEnd() {
        if (xStart > xEnd) { int t = xStart; xStart = xEnd; xEnd = t; }
        if (yStart > yEnd) { int t = yStart; yStart = yEnd; yEnd = t; }
    }

    /**
     * Returns <code>true</code> if this rectangle intersects
     * with the horizontal rectangle which opposite corners are at points
     * (<code>x0</code>, <code>y0</code>) and (<code>x1</code>, <code>y1</code>).
     */
    public boolean intersectsRectangle(Plan plan, float x0, float y0, float x1, float y1) {
        if (!isAtZLevel(plan)) return false;

        Rectangle2D rectangle = new Rectangle2D.Float(x0, y0, 0, 0);
        rectangle.add(x1, y1);
        return getShape().intersects(rectangle);
    }

    /**
     * Returns <code>true</code> if this wall contains
     * the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code>.
     */
    public boolean containsPoint(Plan plan, float x, float y, float margin) {
        if (!isAtZLevel(plan)) return false;
        return containsShapeAtWithMargin(getShape(), x, y, margin);
    }

    /**
     * Returns <code>true</code> if <code>shape</code> contains
     * the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code>.
     */
    protected boolean containsShapeAtWithMargin(Shape shape, float x, float y, float margin) {
        if (margin == 0) return shape.contains(x, y);
        return shape.intersects(x - margin, y - margin, 2 * margin, 2 * margin);
    }

    /**
     * Returns <code>true</code> if this item start line contains
     * the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code> around the item start line.
     */
    public boolean containsItemStartAt(float x, float y, float margin) {
        Rectangle rect = getShape().getBounds();
        Line2D startLine = new Line2D.Float(rect.x, rect.y, rect.x, rect.y + rect.height);
        return containsShapeAtWithMargin(startLine, x, y, margin);
    }

    /**
     * Returns <code>true</code> if this item end line contains
     * the point at (<code>x</code>, <code>y</code>)
     * with a given <code>margin</code> around the wall end line.
     */
    public boolean containsItemEndAt(float x, float y, float margin) {
        Rectangle rect = getShape().getBounds();
        Line2D endLine = new Line2D.Float(rect.x + rect.width, rect.y, rect.x + rect.width, rect.y + rect.height);
        return containsShapeAtWithMargin(endLine, x, y, margin);
    }

    public Shape getShape() {
        if (shapeCache == null) shapeCache = getItemShape();
        return shapeCache;
    }

    protected void resetCaches() {
        shapeCache = null;
    }

    public int getXStart() { return xStart; }

    public void setXStart(float value) { setXStart(Math.round(value)); }

    public int getXEnd() { return xEnd; }

    public void setXEnd(float value) { setXEnd(Math.round(value)); }

    public int getYStart() { return yStart; }

    public void setYStart(float value) { setYStart(Math.round(value)); }

    public int getYEnd() { return yEnd; }

    public void setYEnd(float value) { setYEnd(Math.round(value)); }

    public int getZStart() { return zStart; }

    public int getZEnd() { return zEnd; }

    public int getXDistance() {
        return Math.abs(xEnd - xStart);
    }

    public int getYDistance() {
        return Math.abs(yEnd - yStart);
    }

    public int getZDistance() { return Math.abs(zEnd - zStart); }

    public Item move(float dx, float dy, float dz) {
        int idx = Math.round(dx);
        int idy = Math.round(dy);
        int idz = Math.round(dz);

        setXStart(xStart + idx);
        setXEnd(xEnd + idx);
        setYStart(yStart + idy);
        setYEnd(yEnd + idy);
        setZStart(zStart + idz);
        setZEnd(zEnd + idz);
        return this;
    }

    public void rotate(int ox, int oy, int degree) {
        ItemPoint start = new ItemPoint(xStart, yStart);
        start.rotate(ox, oy, degree);
        ItemPoint end = new ItemPoint(xEnd, yEnd);
        end.rotate(ox, oy, degree);

        if (start.x < end.x) {
            setXStart(start.x);
            setXEnd(end.x);
        } else {
            setXStart(end.x);
            setXEnd(start.x);
        }

        if (start.y < end.y) {
            setYStart(start.y);
            setYEnd(end.y);
        } else {
            setYStart(end.y);
            setYEnd(start.y);
        }
    }

    public void movePoint(int x, int y, boolean startPoint) {
        if (startPoint) {
            setXStart(x);
            setYStart(y);
        } else {
            setXEnd(x);
            setYEnd(y);
        }
    }

    public boolean isAtZLevel(Plan plan) {
        return isAtZLevel(plan.getLevel());
    }

    public boolean isAtZLevel(Level level) {
        int levelStart = level.getStart();
        int levelEnd = level.getEnd();

        boolean S_GET_S = (zStart >= levelStart);
        boolean E_LET_E = (zEnd <= levelEnd);
        boolean S_LT_S = (zStart <= levelStart);
        boolean E_GT_E = (zEnd >= levelEnd);

        // Start and End inside the range or
        // Start below and End above the range
        return (S_GET_S && E_LET_E) || (S_LT_S && E_GT_E);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (!id.equals(item.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    /** Keep item's id */
    public Item historyClone() {
        Item clone = clone();
        clone.id = id;
        return clone;
    }

    @Override
    public Item clone() {
        try {
            Item clone = (Item)super.clone();
            clone.generateId();
            clone.plan = null;
            resetCaches();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("xStart", xStart)
                .write("xEnd", xEnd)
                .write("yStart", yStart)
                .write("yEnd", yEnd)
                .write("zStart", zStart)
                .write("zEnd", zEnd);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defInteger("xStart", ((value) -> xStart = value))
                .defInteger("xEnd", ((value) -> xEnd = value))
                .defInteger("yStart", ((value) -> yStart = value))
                .defInteger("yEnd", ((value) -> yEnd = value))
                .defInteger("zStart", ((value) -> zStart = value))
                .defInteger("zEnd", ((value) -> zEnd = value));
    }
}