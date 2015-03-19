package com.tsoft.myprocad.util.script;

import com.tsoft.myprocad.model.Item;

public abstract class ItemBinding<T extends Item> implements JavaScriptBinding {
    protected T item;

    public ItemBinding(T item) {
        this.item = item;
    }

    public int getXStart() { return item.getXStart(); }

    public void setXStart(int value) { item.setXStart(value); }

    public int getXEnd() { return item.getXEnd(); }

    public void setXEnd(int value) { item.setXEnd(value); }

    public int getYStart() { return item.getYStart(); }

    public void setYStart(int value) { item.setYStart(value); }

    public int getYEnd() { return item.getYEnd(); }

    public void setYEnd(int value) { item.setYEnd(value); }

    public int getZStart() { return item.getZStart(); }

    public void setZStart(int value) { item.setZStart(value); }

    public int getZEnd() { return item.getZEnd(); }

    public void setZEnd(int value) { item.setZEnd(value); }
}
