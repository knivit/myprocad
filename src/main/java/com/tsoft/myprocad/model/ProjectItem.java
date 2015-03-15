package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.ProjectItemController;

import java.io.IOException;

public abstract class ProjectItem implements JsonSerializable {
    private String name;

    private transient String typeName;
    private transient Project project;

    public abstract ProjectItemController getController();

    public boolean isPlan() {
        return ProjectItemType.PLAN.getId().equals(typeName);
    }

    public static ProjectItem newInstance(String typeName) {
        ProjectItemType type = ProjectItemType.findById(typeName);
        if (type == null) throw new IllegalArgumentException("Unknown typeName " + typeName);

        return type.newInstance();
    }

    public String getName() { return name; }

    public void setName(String name) {
        this.name = name;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    @Override
    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ProjectItem that = (ProjectItem) o;

        if (!name.equals(that.name)) return false;
        if (!typeName.equals(that.typeName)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name.hashCode();
        result = 31 * result + typeName.hashCode();
        return result;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("name", name);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        reader
                .defString("name", ((value) -> name = value));
    }
}
