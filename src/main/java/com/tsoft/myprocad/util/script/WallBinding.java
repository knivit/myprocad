package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Wall;

public class WallBinding extends ItemBinding<Wall> {
    public WallBinding(Wall wall) {
        super(wall);
    }

    public MaterialBinding getMaterial() { return new MaterialBinding(item.getMaterial()); }

    @Override
    public String getBindingName() {
        return "wall";
    }
}
