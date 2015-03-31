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

    public Seg3 getBeamCoreSegment() {
        Vec3 startPoint = new Vec3(getXStart(), getYStart(), getZStart());
        Vec3 endPoint = new Vec3(getXEnd(), getYEnd(), getZEnd());
        return new Seg3(startPoint, endPoint);
    }

    public double getArea() {
        return width / 1000.0 * height / 1000.0;
    }

    public float getLength() {
        return getBeamCoreSegment().getLength();
    }

    @Override
    public double getVolume() {
        return getArea() * getLength() / 1000.0;
    }

    public int getXozAngle() {
        float angle = getBeamCoreSegment().getAngle(Plane.XOZ);
        xozAngle = (int)Math.round(Math.toDegrees(angle));
        return xozAngle;
    }

    public int getXoyAngle() {
        float angle = getBeamCoreSegment().getAngle(Plane.XOY);
        xoyAngle = (int)Math.round(Math.toDegrees(angle));
        return xoyAngle;
    }

    public int getYozAngle() {
        float angle = getBeamCoreSegment().getAngle(Plane.YOZ);
        yozAngle = (int)Math.round(Math.toDegrees(angle));
        return yozAngle;
    }

    /** Online graph http://www.livephysics.com/tools/mathematical-tools/online-3-d-function-grapher/ */
    @Override
    protected Shape getItemShape() {
        // rotate along the OX
        Seg3 core = getBeamCoreSegment();
        Rot rot = new Rot(core.direction(), Seg3.OX.direction());
        Seg3 rcore = rot.rotateSegment(core);

        // calculate vertexes, counter-clockwise
        // top side
        vertexes[0] = rcore.p0().plus(new Vec3(0, width/2, height/2));
        vertexes[1] = rcore.p0().plus(new Vec3(0, -width/2, height/2));
        vertexes[2] = rcore.p1().plus(new Vec3(0, -width/2, height/2));
        vertexes[3] = rcore.p1().plus(new Vec3(0, width/2, height/2));

        // bottom side
        vertexes[4] = rcore.p0().plus(new Vec3(0, width/2, -height/2));
        vertexes[5] = rcore.p0().plus(new Vec3(0, -width/2, -height/2));
        vertexes[6] = rcore.p1().plus(new Vec3(0, -width/2, -height/2));
        vertexes[7] = rcore.p1().plus(new Vec3(0, width/2, -height/2));

        // rotate the vertexes back
        rot.invert();
        for (int i = 0; i < 8; i ++) vertexes[i] = rot.rotateVector(vertexes[i]);

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
