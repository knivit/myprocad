package com.tsoft.myprocad.model.property;

import com.tsoft.myprocad.l10n.L10;

public class IntegerPropertyValidator implements ItemPropertyValidator {
    private int min;
    private int max;

    public IntegerPropertyValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String validate(String propertyName, Object value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);

        int newValue = (int)value;
        if (newValue < min || newValue > max) return L10.get(L10.ITEM_INVALID_INTEGER_PROPERTY, propertyName, min, max);
        return null;
    }
}
