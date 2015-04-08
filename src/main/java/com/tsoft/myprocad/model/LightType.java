package com.tsoft.myprocad.model;

public enum LightType {
    AMBIENT(1, "Ambient"),
    DIRECTIONAL(2, "Directional");

    private int id;
    private String name;

    LightType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }

    public String getName() {
        return name;
    }

    public static LightType findById(int id) {
        for (LightType lightType : values()) {
            if (lightType.id == id) return lightType;
        }
        return null;
    }

    public static LightType findByName(String name) {
        for (LightType lightType : values()) {
            if (lightType.name.equalsIgnoreCase(name) ||
                    lightType.name().equalsIgnoreCase(name)) return lightType;
        }
        return null;
    }

    @Override
    public String toString() { return name; }
}
