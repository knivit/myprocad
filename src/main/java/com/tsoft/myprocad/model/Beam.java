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

    /* Inner props */
    private Vec3f[] vertexes = new Vec3f[8];

    Beam() {
        super();
        setTypeName(ItemType.BEAM.getTypeName());

        properties.put(WIDTH_PROPERTY, 50);
        properties.put(HEIGHT_PROPERTY, 150);
        properties.put(FOREGROUND_COLOR_PROPERTY, Color.BLACK.getRGB());
        properties.put(BACKGROUND_COLOR_PROPERTY, Color.WHITE.getRGB());
        properties.put(BORDER_COLOR_PROPERTY, Color.BLACK.getRGB());
        properties.put(BORDER_WIDTH_PROPERTY, 1);
        properties.put(PATTERN_ID_PROPERTY, Pattern.BACKGROUND.getId());
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

    /** Online graph http://www.livephysics.com/tools/mathematical-tools/online-3-d-function-grapher/ */
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

        vertexes[0] = rcore.p0().plus(new Vec3f(0, width/2, height/2));
        vertexes[1] = rcore.p0().plus(new Vec3f(0, -width/2, height/2));
        vertexes[2] = rcore.p1().plus(new Vec3f(0, -width/2, height/2));
        vertexes[3] = rcore.p1().plus(new Vec3f(0, width/2, height/2));

        // bottom side
        vertexes[4] = rcore.p0().plus(new Vec3f(0, width/2, -height/2));
        vertexes[5] = rcore.p0().plus(new Vec3f(0, -width/2, -height/2));
        vertexes[6] = rcore.p1().plus(new Vec3f(0, -width/2, -height/2));
        vertexes[7] = rcore.p1().plus(new Vec3f(0, width/2, -height/2));

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

    public String toObjString(int vno) {
        StringBuilder buf = new StringBuilder();

        // vertexes
        for (int i = 0; i < 8; i ++) {
            buf.append("v " + vertexes[i].x() + " " + vertexes[i].y() + " " + vertexes[i].z()).append('\n');
        }

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
