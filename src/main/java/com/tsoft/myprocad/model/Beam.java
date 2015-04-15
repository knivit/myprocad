package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.*;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.io.IOException;

public class Beam extends AbstractMaterialItem implements JsonSerializable {
    private int width = 50;
    private int height = 150;

    /* Calculated */
    private transient int xozAngle;
    private transient int xoyAngle;
    private transient int yozAngle;

    Beam() {
        super();
        setTypeName(ItemType.BEAM.getTypeName());
    }

    public int getWidth() {
        return width;
    }

    public String validateWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > 1000) return L10.get(L10.ITEM_INVALID_INTEGER_PROPERTY, 0, 1000);
        return null;
    }

    public void setWidth(int value) {
        if (width == value) return;

        width = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public int getHeight() {
        return height;
    }

    public String validateHeight(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > 1000) return L10.get(L10.ITEM_INVALID_INTEGER_PROPERTY, 0, 1000);
        return null;
    }

    public void setHeight(int value) {
        if (height == value) return;

        height = value;
        resetCaches();
        if (plan != null) plan.itemChanged(this);
    }

    public Seg3 getSegment() {
        Vec3 startPoint = new Vec3(getXStart(), getYStart(), getZStart());
        Vec3 endPoint = new Vec3(getXEnd(), getYEnd(), getZEnd());
        return new Seg3(startPoint, endPoint);
    }

    public double getArea() {
        return width / 1000.0 * height / 1000.0;
    }

    @Override
    public double getLength() {
        return getSegment().getLength();
    }

    @Override
    public double getVolume() {
        return getArea() * getLength() / 1000.0;
    }

    public int getXozAngle() {
        float angle = getSegment().getAngle(Plane.XOZ);
        xozAngle = (int)Math.round(Math.toDegrees(angle));
        return xozAngle;
    }

    public int getXoyAngle() {
        float angle = getSegment().getAngle(Plane.XOY);
        xoyAngle = (int)Math.round(Math.toDegrees(angle));
        return xoyAngle;
    }

    public int getYozAngle() {
        float angle = getSegment().getAngle(Plane.YOZ);
        yozAngle = (int)Math.round(Math.toDegrees(angle));
        return yozAngle;
    }

    /** Online graph http://www.livephysics.com/tools/mathematical-tools/online-3-d-function-grapher/ */
    /**
     *      1-----------2
     *     / |         / |
     *  Z /  |        /  |
     *    0----------3   |
     *    |  5-------|--6    --> X
     *    |/         | /
     *    4----------7/
     *   /
     * Y
     */
    @Override
    protected Shape getItemShape() {
        // http://stackoverflow.com/questions/22769430/find-corners-of-rectangle-given-plane-equation-height-and-width
        Seg3 core = getSegment();
        if (core.isVertical()) {
            vertexes[0] = core.p1().plus(-width/2, height/2, 0);
            vertexes[1] = core.p1().plus(-width/2, -height/2, 0);
            vertexes[2] = core.p1().plus(width/2, -height/2, 0);
            vertexes[3] = core.p1().plus(width/2, height/2, 0);
            vertexes[4] = core.p0().plus(-width/2, height/2, 0);
            vertexes[5] = core.p0().plus(-width/2, -height/2, 0);
            vertexes[6] = core.p0().plus(width/2, -height/2, 0);
            vertexes[7] = core.p0().plus(width/2, height/2, 0);
        } else {
            Vec3 u = Vec3.Z_AXIS.cross(core.direction());
            u.normalize();
            Vec3 w = core.direction().cross(u);
            w.normalize();

            vertexes[0] = core.p0().plus(u.times(width / 2)).plus(w.times(height / 2));
            vertexes[4] = core.p0().plus(u.times(width / 2)).minus(w.times(height / 2));
            vertexes[1] = core.p0().minus(u.times(width / 2)).plus(w.times(height / 2));
            vertexes[5] = core.p0().minus(u.times(width / 2)).minus(w.times(height / 2));

            vertexes[3] = core.p1().plus(u.times(width / 2)).plus(w.times(height / 2));
            vertexes[7] = core.p1().plus(u.times(width / 2)).minus(w.times(height / 2));
            vertexes[2] = core.p1().minus(u.times(width / 2)).plus(w.times(height / 2));
            vertexes[6] = core.p1().minus(u.times(width / 2)).minus(w.times(height / 2));
        }

        // draw a top
        GeneralPath path = new GeneralPath();
        path.moveTo(vertexes[0].x(), vertexes[0].y());
        path.lineTo(vertexes[1].x(), vertexes[1].y());
        path.lineTo(vertexes[2].x(), vertexes[2].y());
        path.lineTo(vertexes[3].x(), vertexes[3].y());
        path.closePath();

        // draw a bottom
        path.moveTo(vertexes[4].x(), vertexes[4].y());
        path.lineTo(vertexes[5].x(), vertexes[5].y());
        path.lineTo(vertexes[6].x(), vertexes[6].y());
        path.lineTo(vertexes[7].x(), vertexes[7].y());
        path.closePath();

        // draw a left side
        path.moveTo(vertexes[0].x(), vertexes[0].y());
        path.lineTo(vertexes[1].x(), vertexes[1].y());
        path.lineTo(vertexes[5].x(), vertexes[5].y());
        path.lineTo(vertexes[4].x(), vertexes[4].y());
        path.closePath();

        // draw a right side
        path.moveTo(vertexes[2].x(), vertexes[2].y());
        path.lineTo(vertexes[3].x(), vertexes[3].y());
        path.lineTo(vertexes[7].x(), vertexes[7].y());
        path.lineTo(vertexes[6].x(), vertexes[6].y());
        path.closePath();
        return path;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("width", width)
                .write("height", height);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defInteger("width", ((value) -> width = value))
                .defInteger("height", ((value) -> height = value))
                .read();
    }
}
