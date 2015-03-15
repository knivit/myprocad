package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;

public enum Rotation {
    ANGLE_0(1, 0, L10.get(L10.ROTATION_ANGLE_0)),
    ANGLE_90(2, 90, L10.get(L10.ROTATION_ANGLE_90)),
    ANGLE_180(3, 180, L10.get(L10.ROTATION_ANGLE_180)),
    ANGLE_270(4, 270, L10.get(L10.ROTATION_ANGLE_270));

    private int id;
    private int angle;
    private String text;

    Rotation(int id, int angle, String text) {
        this.id = id;
        this.angle = angle;
        this.text = text;
    }

    public int getId() { return id; }

    public int getAngle() { return angle; }

    public boolean isVertical() { return angle == 90 || angle == 270; }

    @Override
    public String toString() { return text; }

    public static Rotation findById(int rotationId) {
        for (Rotation rotation : values()) {
            if (rotation.getId() == rotationId) return rotation;
        }
        return null;
    }
}
