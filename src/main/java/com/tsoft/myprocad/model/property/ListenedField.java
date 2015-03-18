package com.tsoft.myprocad.model.property;

import java.util.UUID;

public class ListenedField {
    private String name;

    public ListenedField() {
        this.name = UUID.randomUUID().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || !(o instanceof ListenedField)) return false;

        ListenedField that = (ListenedField) o;
        if (!name.equals(that.name)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
