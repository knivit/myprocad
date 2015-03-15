package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.l10n.L10;

import com.tsoft.myprocad.model.calculation.RightTriangle;
import com.tsoft.myprocad.model.calculation.Triangle;
import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.model.property.ProjectProperties;
import com.tsoft.myprocad.swing.*;

import com.tsoft.myprocad.swing.dialog.CalculationsDialogPanel;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.ProjectPanelMenu;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.util.ContentManager;
import com.tsoft.myprocad.util.FileRecorder;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.component.FolderListController;
import com.tsoft.myprocad.viewcontroller.component.MaterialListController;
import com.tsoft.myprocad.viewcontroller.component.ProjectItemListController;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;
import com.tsoft.myprocad.viewcontroller.property.calculation.RightTrianglePropertiesController;
import com.tsoft.myprocad.viewcontroller.property.calculation.TrianglePropertiesController;

import java.util.List;
import java.util.function.Function;

public class ProjectController {
    public Project project;

    public ProjectPanel projectPanel;

    private FolderListController folderListController;
    private MaterialListController materialListController;
    private ProjectItemListController projectItemListController;

    private ProjectItemController activeController;

    public static ProjectController createProjectController(Project project) {
        ProjectController projectController = new ProjectController(project);
        projectController.projectPanel = new ProjectPanel(projectController);
        return projectController;
    }

    private ProjectController(Project project) {
        this.project = project;
        project.projectController = this;

        /* sub-controllers */
        folderListController = new FolderListController(this);
        materialListController = new MaterialListController(project);
        projectItemListController = new ProjectItemListController(this);
    }

    private ProjectItemList getProjectItems() {
        return project.getActiveFolder().getItems();
    }

    public void afterOpen() {
        project.afterOpen();
        projectPanel.fillFolderComboBox(project);

        createTabs();
        restoreActiveTab();
        ProjectPanelMenu.setVisible(true);
    }

    private void createTabs() {
        // remove old sub-tabs
        projectPanel.removeAllTabs();

        List<ProjectItem> items = project.getActiveFolder().getItems();
        items.forEach(this::addItemController);
    }

    public void afterActivation() {
        afterTabChanged();
    }

    public void afterTabChanged() {
        // notify current controller
        if (activeController != null) activeController.beforeDeactivation();

        // switch to the new one
        int index = projectPanel.getSelectedTabIndex();
        if (index < 0 || index >= getProjectItems().size()) activeController = null;
        else activeController = getProjectItems().get(index).getController();

        // and notify it
        if (activeController != null) activeController.afterActivation();
    }

    public ProjectItem addItem(ProjectItemType itemType, String name, int index) {
        ProjectItem item = itemType.newInstance();
        item.setName(name);
        item.setProject(project);

        ProjectItemList items = project.getActiveFolder().getItems();
        items.add(index < 0 || index > items.size() ? items.size() : index, item);

        addItemController(item);
        return item;
    }

    private void addItemController(ProjectItem item) {
        ProjectItemController itemController = item.getController();
        itemController.setProjectController(this);
        itemController.afterOpen();

        projectPanel.addTab(item.getName(), itemController.getParentComponent());
    }

    public void moveTabTo(ProjectItem item, int index) {
        projectPanel.moveTabTo(item.getName(), index);
    }

    public void removeItemController(ProjectItem item) {
        projectPanel.removeTab(item.getName());
        project.getActiveFolder().setTabIndex(projectPanel.getSelectedTabIndex());
        project.setModified(true);
    }

    public ProjectItemController getActiveController() {
        return activeController;
    }

    public void beforeClose() {
        for (ProjectItem projectItem : getProjectItems()) {
            projectItem.getController().beforeClose();
        }
    }

    public void projectChanged(ListenedField property) {
        if (ProjectProperties.FOLDERS.equals(property)) projectPanel.fillFolderComboBox(project);
        ApplicationController.getInstance().projectChanged(project, property);
    }

    public void save() {
        if (project.getFileName() == null) saveAs();
        else doSave();
    }

    public void saveAs() {
        String fileName = SwingTools.showSaveDialog(project.getFileName(), ContentManager.ContentType.MY_PRO_CAD);
        if (fileName != null) {
            project.setFileName(fileName);
            doSave();
        }
    }

    private void doSave() {
        ThreadedTaskController task = new ThreadedTaskController(L10.get(L10.PROJECT_SAVE_MESSAGE));

        task.execute(() -> {
            FileRecorder fileRecorder = new FileRecorder();
            if (fileRecorder.writeProject(project, project.getFileName())) project.setModified(false);
            else SwingTools.showError(L10.get(L10.CANT_SAVE_PROJECT));
            return null;
        });
    }

    public void beforeFolderChanged() {
        // save active folder's tab index
        project.getActiveFolder().setTabIndex(projectPanel.getSelectedTabIndex());

        ProjectItemController activeController = getActiveController();
        if (activeController != null) activeController.beforeDeactivation();
    }

    public void afterFolderChanged() {
        createTabs();
        restoreActiveTab();
    }

    // Restore active tab
    private void restoreActiveTab() {
        int tabIndex = project.getActiveFolder().getTabIndex();
        projectPanel.selectTab(tabIndex);
    }

    public void planNameChanged(Plan plan) {
        projectPanel.setCurrentTabName(plan.getName());
    }

    private void editProject() {
        projectItemListController.edit();
    }

    public void addProjectItem() {
        ProjectItem item = projectItemListController.add(-1);
        if (item != null) projectPanel.selectTab(item.getName());
    }

    public void deleteProjectItem(ProjectItem item) {
        projectItemListController.delete(item);
    }

    private void editFolders() {
        folderListController.edit();
    }

    public MaterialList findMaterialByName(String materialName) {
        return project.getMaterials().filterByNameOrPartOfIt(materialName);
    }

    public void modeChanged(String text) {
        ApplicationController.getInstance().modeChanged(text);
    }

    public void setStatusPanelVisible(boolean isVisible) {
        ApplicationController.getInstance().setStatusPanelVisible(isVisible);
    }

    private <T> void calculations(Function<PropertiesManagerPanel, AbstractPropertiesController<T>> constructor, T entity, String picFileName) {
        CalculationsDialogPanel dialog = new CalculationsDialogPanel(constructor, entity, picFileName);
        dialog.displayView(L10.get(L10.CALCULATION_DIALOG_TITLE), DialogButton.CLOSE);
    }

    public boolean doMenuAction(Menu menu) {
        /* Project */
        if (Menu.EDIT_PROJECT.equals(menu)) { editProject(); return true; }
        if (Menu.EDIT_FOLDERS.equals(menu)) { editFolders(); return true; }
        if (Menu.MATERIALS.equals(menu)) { materialListController.edit(); return true; }

        /* Calculations */
        if (Menu.CALCULATION_TRIANGLE.equals(menu)) {
            calculations(TrianglePropertiesController::new, Triangle.entity, "resources/calculations/Triangle.png");
            return true;
        }
        if (Menu.CALCULATION_RIGHT_TRIANGLE.equals(menu)) {
            calculations(RightTrianglePropertiesController::new, RightTriangle.entity, "resources/calculations/RightTriangle.gif");
            return true;
        }

        /* Delegate it to item controller */
        ProjectItemController itemController = getActiveController();
        if (itemController != null && itemController.doMenuAction(menu)) return true;

        return false;
    }
}
