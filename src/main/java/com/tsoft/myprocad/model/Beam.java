package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.IntegerPropertyValidator;
import com.tsoft.myprocad.model.property.ItemProperty;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.Plane;
import com.tsoft.myprocad.util.linealg.Rotf;
import com.tsoft.myprocad.util.linealg.Segment;
import com.tsoft.myprocad.util.linealg.Vec3f;
import com.tsoft.myprocad.model.property.CalculatedItemProperty;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.io.IOException;

public class Beam extends Item implements JsonSerializable {
    public static final transient ItemProperty WIDTH_PROPERTY = new ItemProperty("width", Integer.class, new IntegerPropertyValidator(1, 1000));
    public static final transient ItemProperty HEIGHT_PROPERTY = new ItemProperty("height", Integer.class, new IntegerPropertyValidator(1, 1000));
    public static final transient ItemProperty FOREGROUND_COLOR_PROPERTY = new ItemProperty("foregroundColor", Integer.class);
    public static final transient ItemProperty BACKGROUND_COLOR_PROPERTY = new ItemProperty("backgroundColor", Integer.class);
    public static final transient ItemProperty BORDER_COLOR_PROPERTY = new ItemProperty("borderColor", Integer.class);
    public static final transient ItemProperty BORDER_WIDTH_PROPERTY = new ItemProperty("borderWidth", Integer.class, new IntegerPropertyValidator(1, 8));
    public static final transient ItemProperty PATTERN_ID_PROPERTY = new ItemProperty("patternId", Integer.class);
    public static final transient ItemProperty MATERIAL_ID_PROPERTY = new ItemProperty("materialId", Integer.class);

    /* Calculated */
    public static final transient CalculatedItemProperty XOZ_ANGLE = new CalculatedItemProperty("xozAngle", Float.class);
    public static final transient CalculatedItemProperty XOY_ANGLE = new CalculatedItemProperty("xoyAngle", Float.class);
    public static final transient CalculatedItemProperty YOZ_ANGLE = new CalculatedItemProperty("yozAngle", Float.class);

    private transient Material material;
    private transient Pattern pattern;

    Beam() {
        super();
        setTypeName(ItemType.BEAM.getTypeName());

        properties.put(WIDTH_PROPERTY, 50);
        properties.put(HEIGHT_PROPERTY, 150);
        properties.put(FOREGROUND_COLOR_PROPERTY, Color.BLACK.getRGB());
        properties.put(BACKGROUND_COLOR_PROPERTY, Color.WHITE.getRGB());
        properties.put(BORDER_COLOR_PROPERTY, Color.BLACK.getRGB());
        properties.put(BORDER_WIDTH_PROPERTY, 1);
        properties.put(PATTERN_ID_PROPERTY, Pattern.HATCH_UP.getId());
        properties.put(MATERIAL_ID_PROPERTY, null);
    }

    public Pattern getPattern() {
        if (pattern == null) {
            int patternId = (int)getPropertyValue(PATTERN_ID_PROPERTY);
            pattern = Pattern.findById(patternId);
        }
        return pattern;
    }

    public void setPattern(Pattern value) {
        if (ObjectUtil.equals(getPattern(), value)) return;
        pattern = value;
        setPropertyValue(PATTERN_ID_PROPERTY, value.getId());
    }

    public Material getMaterial() {
        if (material == null && plan != null) {
            long materialId = (long)getPropertyValue(MATERIAL_ID_PROPERTY);
            material = plan.getProject().getMaterials().findById(materialId);
        }
        return material;
    }

    public void setMaterial(Material value) {
        if (ObjectUtil.equals(getMaterial(), value)) return;
        material = value;
        setPropertyValue(MATERIAL_ID_PROPERTY, value.getId());
    }

    private Segment getBeamCoreSegment() {
        Vec3f startPoint = new Vec3f(getXStart(), getYStart(), getZStart());
        Vec3f endPoint = new Vec3f(getXEnd(), getYEnd(), getZEnd());
        return new Segment(startPoint, endPoint);
    }

    /* Calculated properties */
    private Float getXozAngle() {
        if (properties.containsKey(XOZ_ANGLE)) return (Float)properties.get(XOZ_ANGLE);

        float angle = getBeamCoreSegment().getAngle(Plane.XOZ);
        float degrees = (float)Math.toDegrees(angle);
        setPropertyValue(XOZ_ANGLE, degrees);
        return degrees;
    }

    private Float getXoyAngle() {
        if (properties.containsKey(XOY_ANGLE)) return (Float)properties.get(XOY_ANGLE);

        float angle = getBeamCoreSegment().getAngle(Plane.XOY);
        float degrees = (float)Math.toDegrees(angle);
        setPropertyValue(XOY_ANGLE, degrees);
        return degrees;
    }

    private Float getYozAngle() {
        if (properties.containsKey(YOZ_ANGLE)) return (Float)properties.get(YOZ_ANGLE);

        float angle = getBeamCoreSegment().getAngle(Plane.YOZ);
        float degrees = (float)Math.toDegrees(angle);
        setPropertyValue(YOZ_ANGLE, degrees);
        return degrees;
    }

    @Override
    protected Shape getItemShape() {
        // rotate along the OX
        Segment core = getBeamCoreSegment();
        Rotf rot = new Rotf(core.direction(), Segment.OX.direction());
        Segment rcore = rot.rotateSegment(core);

        // calculate vertexes, counter-clockwise
        // top side
        int width = (int)getPropertyValue(WIDTH_PROPERTY);
        int height = (int)getPropertyValue(HEIGHT_PROPERTY);
        Vec3f[] v = new Vec3f[8];
        v[0] = rcore.p0().minus(new Vec3f(0, -width/2, height/2));
        v[1] = rcore.p0().minus(new Vec3f(0, width/2, height/2));
        v[2] = rcore.p1().minus(new Vec3f(0, width/2, height/2));
        v[3] = rcore.p1().minus(new Vec3f(0, -width/2, height/2));

        // bottom side
        v[4] = rcore.p0().minus(new Vec3f(0, -width/2, -height/2));
        v[5] = rcore.p0().minus(new Vec3f(0, width/2, -height/2));
        v[6] = rcore.p1().minus(new Vec3f(0, width/2, -height/2));
        v[7] = rcore.p1().minus(new Vec3f(0, -width/2, -height/2));

        // rotate the vertexes back
        rot.invert();
        for (int i = 0; i < 8; i ++) v[i] = rot.rotateVector(v[i]);

        // find out min/max of Z coordinates among the vertexes and Plan's level
        float zMin = Float.MAX_VALUE;
        float zMax = Float.MIN_VALUE;
        for (int i = 0; i < 8; i ++) {
            if (zMin > v[i].z()) zMin = v[i].z();
            if (zMax < v[i].z()) zMax = v[i].z();
        }
        zMin = Math.min(zMin, plan.getLevel().getStart());
        zMax = Math.max(zMax, plan.getLevel().getEnd());

        // create bottom and top XOY planes of the visible level
        Plane bottom = new Plane(new Vec3f(0, 1, 0), new Vec3f(0, 0, zMin));
        Plane top = new Plane(new Vec3f(0, 1, 0), new Vec3f(0, 0, zMax));

        // find out their projections on that planes
        Vec3f[] pt = new Vec3f[4];
        Vec3f[] pb = new Vec3f[4];
        for (int i = 0; i < 4; i ++) pt[i] = top.projectPoint(v[i]);
        for (int i = 0; i < 4; i ++) pb[i] = bottom.projectPoint(v[i + 4]);

        // draw the shapes of top and bottom projections
        GeneralPath path = new GeneralPath();
        path.moveTo(pt[0].x(), pt[0].y());
        for (int i = 1; i < 4; i++) path.lineTo(pt[i].x(), pt[i].y());
        path.closePath();

        path.moveTo(pb[0].x(), pb[0].y());
        for (int i = 1; i < 4; i++) path.lineTo(pb[i].x(), pb[i].y());
        path.closePath();

        return path;
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.BEAM_TYPE_NAME) + (material == null ? "" : " " + material);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);

        for (ItemProperty property : properties.keySet()) {
            if (!property.isCalculated()) writer.write(property.getName(), properties.get(property));
        }
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);

        for (ItemProperty property : properties.keySet()) {
            reader.defByType(property.getName(), ((value) -> properties.put(property, value)));
        }
        reader.read();
    }
}
