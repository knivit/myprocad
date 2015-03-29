package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Material;

public class MaterialBinding implements JavaScriptBinding {
    private Material material;

    public MaterialBinding(Material material) { this.material = material; }

    public String getName() { return material.getName(); }

    public float getDensity() { return material.getDensity(); }

    public float getPrice() { return material.getPrice(); }

    public Material getMaterial() { return material; }

    @Override
    public String getBindingName() {
        return "material";
    }
}
