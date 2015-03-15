package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Wall;

public class WallBinding implements JavaScriptBinding {
    private Wall wall;

    public WallBinding(Wall wall) {
        this.wall = wall;
    }

    public int getXStart() { return wall.getXStart(); }

    public void setXStart(int value) { wall.setXStart(value); }

    public int getXEnd() { return wall.getXEnd(); }

    public void setXEnd(int value) { wall.setXEnd(value); }

    public int getYStart() { return wall.getYStart(); }

    public void setYStart(int value) { wall.setYStart(value); }

    public int getYEnd() { return wall.getYEnd(); }

    public void setYEnd(int value) { wall.setYEnd(value); }

    public int getZStart() { return wall.getZStart(); }

    public void setZStart(int value) { wall.setZStart(value); }

    public int getZEnd() { return wall.getZEnd(); }

    public void setZEnd(int value) { wall.setZEnd(value); }

    public MaterialBinding getMaterial() { return new MaterialBinding(wall.getMaterial()); }

    @Override
    public String getBindingName() {
        return "wall";
    }
}
