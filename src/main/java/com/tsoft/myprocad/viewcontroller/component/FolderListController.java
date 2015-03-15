package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Folder;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.model.ProjectItem;
import com.tsoft.myprocad.model.ProjectItemList;
import com.tsoft.myprocad.model.property.ProjectProperties;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.InputDialogPanel;
import com.tsoft.myprocad.swing.dialog.InputTextElement;
import com.tsoft.myprocad.swing.dialog.ListDialogPanel;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.ProjectController;

import java.util.Arrays;
import java.util.Optional;

public class FolderListController extends AbstractListController {
    private Project project;
    private ProjectController projectController;

    public FolderListController(ProjectController projectController) {
        this.projectController = projectController;
        this.project = projectController.project;
    }

    /** Return true if folder list was modified */
    public boolean edit() {
        ListDialogPanel<Folder> listDialogPanel = new ListDialogPanel<>(project.getFolders(), this::moveFolderUp,
                this::moveFolderDown, this::addFolder, this::deleteFolder, this::renameFolder);
        DialogButton result = listDialogPanel.displayView(L10.get(L10.EDIT_FOLDERS_TITLE), DialogButton.SAVE, DialogButton.CANCEL);
        if (DialogButton.SAVE.equals(result)) {
            project.setModified(true);
        }

        return true;
    }

    public Folder addFolder(int index) {
        String folderName = SwingTools.showInputDialog(L10.get(L10.INPUT_FOLDER_NAME), null);
        if (StringUtil.isEmpty(folderName)) return null;

        Optional<Folder> check = project.getFolders().stream().filter(e -> e.name.equals(folderName)).findFirst();
        if (check.isPresent()) {
            SwingTools.showMessage(L10.get(L10.FOLDER_ALREADY_EXISTS, folderName));
            return null;
        }

        Folder folder = project.createFolder(folderName, index);
        projectController.projectChanged(ProjectProperties.FOLDERS);
        return folder;
    }

    public boolean deleteFolder(Folder folder) {
        ProjectItemList folderItems = folder.getItems();
        if (folderItems != null && !folderItems.isEmpty()) {
            SwingTools.showMessage(L10.get(L10.CANT_REMOVE_NON_EMPTY_FOLDER));
            return false;
        }

        if (project.getFolders().size() == 1) {
            SwingTools.showMessage(L10.get(L10.CANT_REMOVE_THE_ONLY_FOLDER));
            return false;
        }

        project.deleteFolder(folder);
        return true;
    }

    public boolean moveFolderUp(Folder folder) {
        boolean isModified = moveElementUp(project.getFolders(), folder);
        if (isModified) project.setModified(true);
        return isModified;
    }

    public boolean moveFolderDown(Folder folder) {
        boolean isModified = moveElementDown(project.getFolders(), folder);
        if (isModified) project.setModified(true);
        return isModified;
    }

    public boolean renameFolder(Folder folder) {
        InputTextElement nameElement = new InputTextElement(folder.name);
        InputDialogPanel inputDialogPanel = new InputDialogPanel(Arrays.asList(nameElement));
        DialogButton result = inputDialogPanel.displayView(L10.get(L10.RENAME_FOLDER), DialogButton.OK, DialogButton.CANCEL);
        if (!DialogButton.OK.equals(result)) return false;

        Optional<Folder> check = project.getFolders().stream().filter(e -> e.name.equals(nameElement.getValue())).findFirst();
        if (check.isPresent()) {
            SwingTools.showMessage(L10.get(L10.FOLDER_ALREADY_EXISTS, folder.name));
            return false;
        }

        folder.name = nameElement.getValue();
        return true;
    }
}
