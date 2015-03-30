package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.io.IOException;

public abstract class AbstractMaterialItem extends Item {
    public static transient final int MAX_BORDER_WIDTH = 5;

    private long materialId;
    private int backgroundColor = Color.WHITE.getRGB();
    private int foregroundColor = Color.BLACK.getRGB();
    private int borderColor = Color.BLACK.getRGB();
    private int borderWidth = 1;
    private int patternId = Pattern.HATCH_UP.getId();

    private transient Material material;
    private transient Pattern pattern;

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

    public double getWeight() {
        return getVolume() * getDensity();
    }

    public double getPrice() {
        Material material = getMaterial();
        if (material != null) return getVolume() * material.getPrice();
        return 0;
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
