package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.calculation.WoodBeam;

public enum ProjectItemType {
    PLAN("Plan", L10.get(L10.PROJECT_ITEM_PLAN)) {
        @Override
        public ProjectItem newInstance() {
            ProjectItem item = new Plan();
            item.setTypeName(PLAN.getId());
            return item;
        }
    },

    NOTES("Notes", L10.get(L10.PROJECT_ITEM_NOTES)) {
        @Override
        public ProjectItem newInstance() {
            ProjectItem item = new Notes();
            item.setTypeName(NOTES.getId());
            return item;
        }
    },

    WOOD_BEAM("WoodBeam", L10.get(L10.PROJECT_ITEM_WOOD_BEAM)) {
        @Override
        public ProjectItem newInstance() {
            ProjectItem item = new WoodBeam();
            item.setTypeName(WOOD_BEAM.getId());
            return item;
        }
    };

    private String id;
    private String text;

    public abstract ProjectItem newInstance();

    ProjectItemType(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public String getId() { return id; }

    public static ProjectItemType findById(String id) {
        for (ProjectItemType type : values()) {
            if (type.getId().equals(id)) return type;
        }
        return null;
    }

    @Override
    public String toString() { return text; }
}