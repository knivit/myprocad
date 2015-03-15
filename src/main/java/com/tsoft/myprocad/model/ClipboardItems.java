package com.tsoft.myprocad.model;

import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class ClipboardItems implements JsonSerializable {
    private ItemList<Item> items = new ItemList<>();
    private MaterialList materials = new MaterialList();

    public ClipboardItems() { }

    public void copyToClipboard(ItemList items) {
        this.items = items;
        enlistMaterials();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            JsonWriter writer = new JsonWriter(bos);
            writer.toJson(this);

            String data = bos.toString("UTF-8");
            StringSelection stringSelection = new StringSelection(data);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ItemList<Item> getItems() {
        return items;
    }

    public MaterialList getMaterials() {
        return materials;
    }

    // Create list of materials for the selected items
    private void enlistMaterials() {
        WallList walls = new WallList(items.getWallsSubList());
        materials = walls.getMaterials();
    }

    public static ClipboardItems readFromClipboard() {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

        try {
            String data = (String)clipboard.getData(DataFlavor.stringFlavor);
            ByteArrayInputStream bis = new ByteArrayInputStream(data.getBytes("UTF-8"));

            JsonReader reader = new JsonReader(bis);
            ClipboardItems clipboardItems = new ClipboardItems();
            clipboardItems.fromJson(reader);
            return clipboardItems;
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .writeMixCollection("items", items)
                .write("materials", materials);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        items = new ItemList<>();
        materials = new MaterialList();
        reader
                .defMixCollection("items", Item::newInstance, ((value) -> items.add((Item)value)))
                .defCollection("materials", Material::new, ((value) -> materials.add((Material)value)))
                .read();
    }
}
