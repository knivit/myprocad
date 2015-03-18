package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.IntegerPropertyValidator;
import com.tsoft.myprocad.model.property.ItemProperty;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.io.IOException;

public class Beam extends Item implements JsonSerializable {
    public static final transient ItemProperty WIDTH_PROPERTY = new ItemProperty("width", Integer.class, new IntegerPropertyValidator(1, 1000));
    public static final transient ItemProperty HEIGHT_PROPERTY = new ItemProperty("height", Integer.class, new IntegerPropertyValidator(1, 1000));
    public static final transient ItemProperty MATERIAL_PROPERTY = new ItemProperty("materialId", Integer.class);

    private transient Material material;

    Beam() {
        super();

        properties.put(WIDTH_PROPERTY, 50);
        properties.put(HEIGHT_PROPERTY, 150);
        properties.put(MATERIAL_PROPERTY, null);
    }

    @Override
    protected Shape getItemShape() {
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
