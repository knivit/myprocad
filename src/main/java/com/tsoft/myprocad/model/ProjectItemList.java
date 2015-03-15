package com.tsoft.myprocad.model;

import java.util.ArrayList;

public class ProjectItemList extends ArrayList<ProjectItem>{
    public ProjectItemList() { super(); }

    public ProjectItem findByName(String name) {
        for (ProjectItem item : this) {
            if (item.getName().equals(name)) return item;
        }
        return null;
    }
}
