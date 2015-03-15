package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

public enum WallShape {
    RECTANGLE(0, L10.get(L10.WALL_SHAPE_RECTANGLE)),
    DIAGONAL1(1, L10.get(L10.WALL_SHAPE_DIAGONAL1)),
    DIAGONAL2(2, L10.get(L10.WALL_SHAPE_DIAGONAL2)),
    DIAGONAL1U(3, L10.get(L10.WALL_SHAPE_DIAGONAL1U)),
    DIAGONAL1D(4, L10.get(L10.WALL_SHAPE_DIAGONAL1D)),
    DIAGONAL2U(5, L10.get(L10.WALL_SHAPE_DIAGONAL2U)),
    DIAGONAL2D(6, L10.get(L10.WALL_SHAPE_DIAGONAL2D)),
    TRIANGLE1(7, L10.get(L10.WALL_SHAPE_TRIANGLE1)),
    TRIANGLE2(8, L10.get(L10.WALL_SHAPE_TRIANGLE2)),
    TRIANGLE3(9, L10.get(L10.WALL_SHAPE_TRIANGLE3)),
    TRIANGLE4(10, L10.get(L10.WALL_SHAPE_TRIANGLE4)),
    CIRCLE(11, L10.get(L10.WALL_SHAPE_CIRCLE));

    private int id;
    private String text;

    WallShape(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public static WallShape findById(int id) {
        for (WallShape wallShape : values()) {
            if (wallShape.id == id) return wallShape;
        }
        return null;
    }

    @Override
    public String toString() { return text; }
}
