package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonSerializable;

import java.awt.*;

public class Beam extends Item implements JsonSerializable {
    Beam() { super(); }

    @Override
    protected Shape getItemShape() {
        return null;
    }

    @Override
    public String getPopupItemName() {
        return null;
    }
}
