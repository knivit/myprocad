package com.tsoft.myprocad.model;

public enum ItemType {
    WALL("Wall") {
        @Override
        public Item newInstance() {
            return new Wall();
        }
    },

    BEAM("Beam") {
        @Override
        public Item newInstance() {
            return new Beam();
        }
    },

    LABEL("Label") {
        @Override
        public Item newInstance() {
            return new Label();
        }
    },

    DIMENSION_LINE("DimensionLine") {
        @Override
        public Item newInstance() {
            return new DimensionLine();
        }
    },

    LEVEL_MARK("LevelMark") {
        @Override
        public Item newInstance() {
            return new LevelMark();
        }
    };

    private String typeName;

    public abstract Item newInstance();

    ItemType(String typeName) { this.typeName = typeName; }

    public String getTypeName() { return typeName; }

    public static ItemType findById(String id) {
        for (ItemType itemType : values()) {
            if (itemType.typeName.equals(id)) return itemType;
        }
        return null;
    }
}
