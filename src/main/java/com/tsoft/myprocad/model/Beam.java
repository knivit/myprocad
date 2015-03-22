package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.IntegerPropertyValidator;
import com.tsoft.myprocad.model.property.ItemProperty;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
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

    @Override
    protected Shape getItemShape() {
        int
        return null;
    }

    @Override
    public String getPopupItemName() {
        return L10.get(L10.BEAM_TYPE_NAME) + (material == null ? "" : " " + material);
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);

        for (ItemProperty property : properties.keySet()) {
            writer.write(property.getName(), properties.get(property));
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
