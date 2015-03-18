package com.tsoft.myprocad.model.property;

public class ItemProperty {
    private String name;
    private Class typeClass;
    private ItemPropertyValidator validator;

    public ItemProperty(String name, Class typeClass) {
        this(name, typeClass, null);
    }

    public ItemProperty(String name, Class typeClass, ItemPropertyValidator validator) {
        this.name = name;
        this.typeClass = typeClass;
        this.validator = validator;
    }

    public String getName() { return name; }

    public Class getType() { return typeClass; }

    public String validateValue(Object value) {
        if (validator == null) return null;
        return validator.validate(name, value);
    }

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
