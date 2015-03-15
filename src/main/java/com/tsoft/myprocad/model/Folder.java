package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.io.IOException;

public class Folder implements JsonSerializable {
    private long id;
    public String name;
    private int tabIndex;
    private ProjectItemList items = new ProjectItemList();

    private transient Project project;

    public Folder() { }

    public void setProject(Project project) {
        this.project = project;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getTabIndex() {
        return tabIndex;
    }

    public void setTabIndex(int value) {
        if (tabIndex == value) return;
        this.tabIndex = value;
        project.setModified(true);
    }

    public ProjectItemList getItems() { return items; }

    public boolean addItem(ProjectItem item) {
        return items.add(item);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Folder folder = (Folder) o;

        if (id != folder.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("id", id)
                .write("name", name)
                .write("tabIndex", tabIndex)
                .writeMixCollection("items", items);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException{
        items = new ProjectItemList();
        reader
                .defLong("id", ((value) -> id = value))
                .defString("name", ((value) -> name = value))
                .defInteger("tabIndex", ((value) -> tabIndex = value))
                .defMixCollection("items", ProjectItem::newInstance, ((value) -> items.add((ProjectItem) value)))
                .read();
    }
}
