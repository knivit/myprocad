package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.NotesController;
import com.tsoft.myprocad.viewcontroller.ProjectItemController;

import java.io.IOException;

public class Notes extends ProjectItem {
    private String text;
    private PageSetup pageSetup = new PageSetup();

    private transient NotesController notesController;

    // Don't remove this constructor, it is needed for JsonReader
    Notes() { }

    public PageSetup getPageSetup() {
        if (pageSetup == null) pageSetup = new PageSetup();
        return pageSetup;
    }

    @Override
    public ProjectItemController getController() {
        if (notesController == null) {
            notesController = NotesController.createNotesController(this);
        }
        return notesController;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("text", text)
                .write("pageSetup", pageSetup);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException{
        pageSetup = new PageSetup();

        super.fromJson(reader);
        reader
                .defString("text", ((value) -> text = value))
                .defObject("pageSetup", pageSetup::fromJson)
                .read();
    }
}
