package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.Vec3;

import java.awt.Color;
import java.io.IOException;

public class Light implements JsonSerializable, Cloneable {
    private int lightTypeId = LightType.AMBIENT.getId();
    private int cx, cy, cz;
    private int dx, dy, dz;
    private Color color = Color.WHITE.darker();

    // inner props
    private transient LightType lightType;

    public Light() {
        super();
    }

    public LightType getLightType() {
        if (lightType == null) lightType = LightType.findById(lightTypeId);
        return lightType;
    }

    public Light setLightType(LightType value) {
        if (!ObjectUtil.equals(getLightType(), value)) {
            lightTypeId = value.getId();
            lightType = value;
        }
        return this;
    }

    public Light setLightType(String lightTypeName) {
        LightType lightType = LightType.findByName(lightTypeName);
        if (lightType == null) throw new IllegalArgumentException("Unknown light type '" + lightType + "'");
        return setLightType(lightType);
    }

    public int getCx() { return cx; }

    public int getCy() { return cy; }

    public int getCz() { return cz; }

    public Light setCenter(int cx, int cy, int cz) {
        this.cx = cx;
        this.cy = cy;
        this.cz = cz;
        return this;
    }

    public Light setCenter(Vec3 center) {
        cx = Math.round(center.x());
        cy = Math.round(center.y());
        cz = Math.round(center.z());
        return this;
    }

    public int getDx() { return dx; }

    public int getDy() { return dy; }

    public int getDz() { return dz; }

    public Light setDirection(int dx, int dy, int dz) {
        this.dx = dx;
        this.dy = dy;
        this.dz = dz;
        return this;
    }

    public Light setDirection(Vec3 dir) {
        dx = Math.round(dir.x());
        dy = Math.round(dir.y());
        dz = Math.round(dir.z());
        return this;
    }

    public Color getColor() { return color; }

    public Light setColor(Color color) {
        this.color = color;
        return this;
    }

    public Light getDeepClone() {
        return clone();
    }

    /** Used in PlanPropertiesController to display table of lights */
    @Override
    public String toString() {
        return color.toString();
    }

    @Override
    public Light clone() {
        try {
            Light clone = (Light)super.clone();
            return clone;
        } catch (CloneNotSupportedException ex) {
            throw new IllegalStateException("Super class isn't cloneable");
        }
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("lightTypeId", lightTypeId)
                .write("cx", cx)
                .write("cy", cy)
                .write("cz", cz)
                .write("dx", dx)
                .write("dy", dy)
                .write("dz", dz)
                .write("color", color.getRGB());
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defInteger("lightTypeId", ((value) -> lightTypeId = value))
                .defInteger("cx", ((value) -> cx = value))
                .defInteger("cy", ((value) -> cy = value))
                .defInteger("cz", ((value) -> cz = value))
                .defInteger("dx", ((value) -> dx = value))
                .defInteger("dy", ((value) -> dy = value))
                .defInteger("dz", ((value) -> dz = value))
                .defInteger("color", ((value) -> color = new Color(value)))
                .read();

    }
}
