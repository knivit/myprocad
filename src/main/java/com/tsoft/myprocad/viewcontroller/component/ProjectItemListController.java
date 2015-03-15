package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.dialog.*;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.ProjectController;

import java.util.Arrays;
import java.util.List;

public class ProjectItemListController extends AbstractListController {
    private Project project;
    private ProjectController projectController;

    private boolean isModified;

    public ProjectItemListController(ProjectController projectController) {
        this.project = projectController.project;
        this.projectController = projectController;
    }

    public boolean edit() {
        isModified = false;

        ListDialogPanel<ProjectItem> listDialogPanel = new ListDialogPanel<>(getItems(), this::moveUp,
                this::moveDown, this::add, this::delete);
        listDialogPanel.displayView(L10.get(L10.EDIT_PROJECT_TITLE), DialogButton.CLOSE);

        if (isModified) project.setModified(true);
        return isModified;
    }

    private ProjectItemList getItems() {
        return project.getActiveFolder().getItems();
    }

    private ProjectItem findItemByName(String name) {
        ProjectItemList items = project.getActiveFolder().getItems();
        return items.findByName(name);
    }

    /** Returns added item or null */
    public ProjectItem add(int index) {
        InputTextElement itemName = new InputTextElement(null);
        InputListElement<ProjectItemType> itemType = new InputListElement<>(Arrays.asList(ProjectItemType.values()));

        InputDialogPanel inputDialogPanel = new InputDialogPanel(Arrays.asList(itemType, itemName));
        DialogButton result = inputDialogPanel.displayView(L10.get(L10.ADD_ITEM), DialogButton.ADD, DialogButton.CANCEL);
        if (!DialogButton.ADD.equals(result)) return null;

        String name = itemName.getValue();
        if (StringUtil.isEmpty(name)) return null;

        if (findItemByName(name) != null) {
            SwingTools.showMessage(L10.get(L10.ITEM_ALREADY_EXISTS, name));
            return null;
        }

        ProjectItem item = projectController.addItem(itemType.getValue(), name, index);

        isModified = true;
        updateTabPosition(item);
        return item;
    }

    public boolean delete(ProjectItem item) {
        ProjectItemList items = project.getActiveFolder().getItems();
        if (items.size() == 1) {
            SwingTools.showMessage(L10.get(L10.CANT_REMOVE_THE_ONLY_PROJECT_ITEM));
            return false;
        }

        if (!SwingTools.showConfirmDialog(L10.get(L10.CONFIRM_ITEM_REMOVAL, item.getName()))) return false;

        items.remove(item);
        projectController.removeItemController(item);

        isModified = true;
        updateTabPosition(item);
        return true;
    }

    private boolean moveUp(ProjectItem item) {
        boolean isMoved = moveElementUp(project.getActiveFolder().getItems(), item);
        if (isMoved) {
            isModified = true;
            updateTabPosition(item);
        }
        return isMoved;
    }

    private boolean moveDown(ProjectItem item) {
        boolean isMoved = moveElementDown(project.getActiveFolder().getItems(), item);
        if (isMoved) {
            isModified = true;
            updateTabPosition(item);
        }
        return isMoved;
    }

    private void updateTabPosition(ProjectItem item) {
        List<ProjectItem> items = project.getActiveFolder().getItems();
        int index = items.indexOf(item);
        projectController.moveTabTo(item, index);
    }
}
