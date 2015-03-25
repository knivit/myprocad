package com.tsoft.myprocad.model.property;

public class ItemProperty {
    private String name;
    private Class typeClass;
    private ItemPropertyValidator validator;
    private boolean calculated;

    public ItemProperty(String name, Class typeClass) {
        this(name, typeClass, null, false);
    }

    public ItemProperty(String name, Class typeClass, ItemPropertyValidator validator) {
        this(name, typeClass, validator, false);
    }

    public ItemProperty(String name, Class typeClass, ItemPropertyValidator validator, boolean calculated) {
        this.name = name;
        this.typeClass = typeClass;
        this.validator = validator;
        this.calculated = calculated;
    }

    public String getName() { return name; }

    public Class getType() { return typeClass; }

    public String validateValue(Object value) {
        if (validator == null) return null;
        return validator.validate(name, value);
    }

    public boolean isCalculated() { return calculated; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ItemProperty that = (ItemProperty) o;

        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
