package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Beam;

public class BeamBinding extends ItemBinding<Beam> {
    public BeamBinding(Beam beam) { super(beam); }

    public float getLength() { return item.getLength(); }

    public MaterialBinding getMaterial() { return new MaterialBinding(item.getMaterial()); }

    public void setMaterial(MaterialBinding materialBinding) {
        item.setMaterial(materialBinding.getMaterial());
    }

    @Override
    public String getBindingName() {
        return "beam";
    }
}
