package com.tsoft.myprocad.model.property;

import java.util.UUID;

public class ListenedField {
    private String name;

    public ListenedField() {
        this.name = UUID.randomUUID().toString();
    }

    public boolean inList(ListenedField ... fields) {
        if (fields == null) return false;
        for (ListenedField field : fields) {
            if (field.equals(this)) {
                return true;
            }
        }
        return false;
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
