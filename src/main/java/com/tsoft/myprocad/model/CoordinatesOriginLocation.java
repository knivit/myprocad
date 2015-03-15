package com.tsoft.myprocad.model;

public enum CoordinatesOriginLocation {
    TOP_LEFT(1), BOTTOM_LEFT(2);

    private int id;

    CoordinatesOriginLocation(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public static CoordinatesOriginLocation findById(int id) {
        for (CoordinatesOriginLocation location : values()) {
            if (location.getId() == id) return location;
        }
        return null;
    }
}
