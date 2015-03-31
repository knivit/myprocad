package com.tsoft.myprocad.model;

import com.tsoft.myprocad.j3d.Triangle3D;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.Vec3;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMaterialItem extends Item {
    public static transient final int MAX_BORDER_WIDTH = 5;

    // For 3d modelling, the item's triangles
    protected static transient final int[][] FACES = new int[][] {
        {1, 7, 5}, {1, 3, 7}, {1, 4, 3},
        {1, 2, 4}, {3, 8, 7}, {3, 4, 8},
        {5, 7, 8}, {5, 8, 6}, {1, 5, 6},
        {1, 6, 2}, {2, 6, 8}, {2, 8, 4}
    };

    private long materialId;
    private int backgroundColor = Color.WHITE.getRGB();
    private int foregroundColor = Color.BLACK.getRGB();
    private int borderColor = Color.BLACK.getRGB();
    private int borderWidth = 1;
    private int patternId = Pattern.HATCH_UP.getId();

    private transient Material material;
    private transient Pattern pattern;

    /* Inner props */
    protected transient Vec3[] vertexes = new Vec3[8];

    public abstract double getVolume();

    public AbstractMaterialItem() {
        super();
    }

    public long getMaterialId() { return materialId; }

    public Material getMaterial() {
        if (material == null && plan != null) material = plan.getProject().getMaterials().findById(materialId);
        return material;
    }

    public AbstractMaterialItem setMaterial(Material value) {
        if (!ObjectUtil.equals(getMaterial(), value)) {
            materialId = value.getId();
            material = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public float getDensity() {
        Material material = getMaterial();
        if (material != null) return material.getDensity();
        return 1;
    }

    public Pattern getPattern() {
        if (pattern == null) pattern = Pattern.findById(patternId);
        return pattern;
    }

    public AbstractMaterialItem setPattern(Pattern value) {
        if (!ObjectUtil.equals(getPattern(), value)) {
            patternId = value.getId();
            pattern = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setPattern(String patternName) {
        Pattern pattern = Pattern.findByName(patternName);
        if (pattern == null) throw new IllegalArgumentException("Unknown pattern '" + patternName + "'");
        return setPattern(pattern);
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public AbstractMaterialItem setBackgroundColor(int value) {
        if (backgroundColor != value) {
            backgroundColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public int getForegroundColor() {
        return foregroundColor;
    }

    public AbstractMaterialItem setForegroundColor(int value) {
        if (foregroundColor != value) {
            foregroundColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public AbstractMaterialItem setBorderColor(int value) {
        if (borderColor != value) {
            borderColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public String validateBorderWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > MAX_BORDER_WIDTH) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, 0, MAX_BORDER_WIDTH);
        return null;
    }

    public AbstractMaterialItem setBorderWidth(int value) {
        if (borderWidth != value) {
            borderWidth = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public double getWeight() {
        return getVolume() * getDensity();
    }

    public double getPrice() {
        Material material = getMaterial();
        if (material != null) return getVolume() * material.getPrice();
        return 0;
    }

    public List<Triangle3D> get3dTriangles() {
        List<Triangle3D> trigs = new ArrayList<>(12);
        for (int i = 0; i < 12; i ++) {
            trigs.add(new Triangle3D(vertexes[FACES[i][0]-1], vertexes[FACES[i][1]-1], vertexes[FACES[i][2]-1]));
        }
        return trigs;
    }

    public String toObjString(int vno) {
        StringBuilder buf = new StringBuilder();

        // vertexes
        for (int i = 0; i < 8; i ++) {
            buf.append("v " + vertexes[i].x() + " " + vertexes[i].y() + " " + vertexes[i].z()).append('\n');
        }

        // faces
        for (int i = 0; i < 12; i ++) {
            buf.append("f " + (vno + FACES[i][0]) + " " + (vno + FACES[i][1]) + " " + (vno + FACES[i][2])).append('\n');
        }
        return buf.toString();
    }

    @Override
    public String getPopupItemName() {
        return getMaterial().getName();
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("materialId", materialId)
                .write("patternId", patternId)
                .write("backgroundColor", backgroundColor)
                .write("foregroundColor", foregroundColor)
                .write("borderColor", borderColor)
                .write("borderWidth", borderWidth);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        super.fromJson(reader);
        reader
                .defLong("materialId", ((value) -> materialId = value))
                .defInteger("patternId", ((value) -> patternId = value))
                .defInteger("backgroundColor", ((value) -> backgroundColor = value))
                .defInteger("foregroundColor", ((value) -> foregroundColor = value))
                .defInteger("borderColor", ((value) -> borderColor = value))
                .defInteger("borderWidth", ((value) -> borderWidth = value));
    }
}
