package com.tsoft.myprocad.model.property;

public class CalculatedItemProperty extends ItemProperty {
    public CalculatedItemProperty(String name, Class typeClass) {
        super(name, typeClass, null, true);
    }

    public CalculatedItemProperty(String name, Class typeClass, ItemPropertyValidator validator) {
        super(name, typeClass, validator, true);
    }
}
