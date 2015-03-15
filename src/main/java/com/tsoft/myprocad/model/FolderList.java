package com.tsoft.myprocad.model;

import java.util.ArrayList;

public class FolderList extends ArrayList<Folder> {
    public FolderList() { super(); }

    public Folder findById(long id) {
        for (Folder folder : this) {
            if (folder.getId() == id) return folder;
        }
        return null;
    }
}
