package com.tsoft.myprocad.model;

public enum ItemType {
    WALL("Wall") {
        @Override
        public Item newInstance() {
            Wall wall = new Wall();
            wall.setTypeName(WALL.getTypeName());
            return wall;
        }
    },

    LABEL("Label") {
        @Override
        public Item newInstance() {
            Label label = new Label();
            label.setTypeName(LABEL.getTypeName());
            return label;
        }
    },

    DIMENSION_LINE("DimensionLine") {
        @Override
        public Item newInstance() {
            DimensionLine dimensionLine = new DimensionLine();
            dimensionLine.setTypeName(DIMENSION_LINE.getTypeName());
            return dimensionLine;
        }
    },

    LEVEL_MARK("LevelMark") {
        @Override
        public Item newInstance() {
            LevelMark levelMark = new LevelMark();
            levelMark.setTypeName(LEVEL_MARK.getTypeName());
            return levelMark;
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
