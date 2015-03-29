package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.model.property.ProjectProperties;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonSerializable;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.viewcontroller.ProjectController;
import com.tsoft.myprocad.viewcontroller.ProjectItemController;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Project implements JsonSerializable, Cloneable {
    private MaterialList materials = new MaterialList();

    private FolderList folders = new FolderList();
    private long activeFolderId;
    private long nextId = 1; // to make sure it differs from 0 (i.e. the default value for int, ling types)

    public transient ProjectController projectController;
    private transient Folder activeFolder;
    private transient String fileName;
    private transient boolean modified;

    public Project() { }

    // Create default folder
    private void createDefaultFolder() {
        if (folders.isEmpty()) createFolder(L10.get(L10.DEFAULT_FOLDER_NAME), 0);
        if (activeFolderId == 0) activeFolderId = folders.get(0).getId();
    }

    public void afterOpen() {
        createDefaultFolder();

        Material defaultMaterial = materials.getDefault();
        if (defaultMaterial == null) {
            defaultMaterial = new Material();
            defaultMaterial.setId(generateNextId());
            defaultMaterial.setDefault(true);
            materials.add(defaultMaterial);
        }

        // localize default material name
        defaultMaterial.setName(L10.get(L10.MATERIAL_UNKNOWN));
    }

    public long generateNextId() { return nextId ++; }

    /** Return file name without a path and an extension */
    public String getName() {
        if (fileName == null) return L10.get(L10.WINDOW_UNTITLED);

        File file = new File(fileName);
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index != -1) fileName = fileName.substring(0, index);
        return fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String value) { fileName = value; }

    public String getTitle() {
        if (fileName == null) return L10.get(L10.WINDOW_UNTITLED);
        return fileName;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        if (modified == this.modified) return;
        this.modified = modified;

        projectController.projectChanged(ProjectProperties.FILE_NAME);
    }

    public void planChanged() {
        setModified(true);
    }

    public Folder getActiveFolder() {
        if (activeFolder == null) activeFolder = folders.findById(activeFolderId);
        return activeFolder;
    }

    public void setActiveFolder(Folder value) {
        if (value.equals(getActiveFolder())) return;

        projectController.beforeFolderChanged();

        activeFolderId = value.getId();
        activeFolder = value;
        setModified(true);

        projectController.afterFolderChanged();
    }

    public FolderList getFolders() { return folders; }

    /** Create a folder and default plan in it */
    public Folder createFolder(String folderName, int index) {
        Folder folder = new Folder();
        folder.setId(generateNextId());
        folder.name = folderName;
        folder.setProject(this);

        folders.add(index < 0 || index >= folders.size() ? folders.size() : index, folder);

        Plan plan = (Plan)ProjectItemType.PLAN.newInstance();
        plan.setName(L10.get(L10.DEFAULT_PLAN_NAME));
        plan.setProject(this);
        folder.addItem(plan);

        return folder;
    }

    public boolean deleteFolder(Folder folder) {
        int index = folders.indexOf(folder);
        if (index == -1) return false;

        folders.remove(folder);
        if (activeFolder.equals(folder)) setActiveFolder(folders.get(index == 0 ? 0 : index - 1));
        projectController.projectChanged(ProjectProperties.FOLDERS);

        return true;
    }

    public List<Plan> getAllPlans() {
        List<Plan> allPlans = new ArrayList<>();
        for (Folder folder : folders) {
            folder.getItems().forEach(e -> {
                if (e.isPlan()) allPlans.add((Plan) e);
            });
        }
        return allPlans;
    }

    public ProjectItem addItem(ProjectItemType itemType, String name, int index) {
        ProjectItem item = itemType.newInstance();
        item.setName(name);
        item.setProject(this);

        ProjectItemList items = getActiveFolder().getItems();
        items.add(index < 0 || index > items.size() ? items.size() : index, item);
        return item;
    }

    public MaterialList getMaterials() {
        return materials;
    }

    public void setMaterials(MaterialList materials) {
        this.materials = materials;
        setModified(true);

        ProjectItemController controller = projectController.getActiveController();
        if (controller != null) controller.materialListChanged();
    }

    public void addMaterial(Material material) {
        material.setId(generateNextId());
        materials.add(material);
    }

    public String validateMaterials(TableDialogPanelSupport<Material> materials) {
        int row = 1;
        for (int i = 0; i < materials.size(); i ++) {
            Material material = materials.get(i);
            if  (StringUtil.isEmpty(material.getName())) return L10.get(L10.MATERIAL_NAME_CANT_BE_EMPTY, row);

            // check for duplicates
            boolean found = false;
            for (int k = i + 1; k < materials.size(); k ++) {
                if (material.equals(materials.get(k))) {
                    found = true;
                }
            }
            if (found) return L10.get(L10.MATERIAL_ALREADY_EXISTS, row);

            row ++;
        }

        return null;
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        writer
                .write("materials", materials)
                .write("folders", folders)
                .write("activeFolderId", activeFolderId)
                .write("nextId", nextId);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        materials = new MaterialList();
        folders = new FolderList();
        try {
            reader
                .defCollection("materials", Material::new, ((value) -> materials.add((Material) value)))
                .defCollection("folders", Folder::new, ((value) -> folders.add((Folder)value)))
                .defLong("activeFolderId", ((value) -> activeFolderId = value))
                .defLong("nextId", ((value) -> nextId = value))
                .read();
        } finally {
            // update references
            for (Folder folder : folders) {
                folder.setProject(this);
                folder.getItems().stream().forEach(e -> e.setProject(this));
            }
        }
    }
}