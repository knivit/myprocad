package com.tsoft.myprocad.model;

public enum PointShapeType {
    NONE(1),
    DIMENSION(2),
    ARROW(3),
    SQUARE(4),
    CIRCLE(5);

    private int id;

    PointShapeType(int id) {
        this.id = id;
    }

    public int getId() { return id; }

    public static PointShapeType findById(int id) {
        for (PointShapeType pointShapeType : values()) {
            if (pointShapeType.getId() == id) return pointShapeType;
        }
        return null;
    }
}