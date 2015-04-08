package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.l10n.L10;

import com.tsoft.myprocad.model.calculation.RightTriangle;
import com.tsoft.myprocad.model.calculation.Triangle;
import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.model.property.ProjectProperties;
import com.tsoft.myprocad.swing.*;

import com.tsoft.myprocad.swing.dialog.*;
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

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.Arrays;
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
        ProjectItem item = project.addItem(itemType, name, index);
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

    private void show3D() {
        class PlanSelection {
            public boolean isSelected;
            public String name;
            public Plan plan;
        }

        class PlanSelectionTableModel extends AbstractTableModel {
            private List<PlanSelection> elements;

            public PlanSelectionTableModel(List<PlanSelection> elements) {
                this.elements = elements;
            }

            @Override
            public String getColumnName(int col) {
                if (col == 0) return "";
                return L10.get(L10.PLAN_NAME_PROPERTY);
            }

            @Override
            public int getRowCount() {
                return elements.size();
            }

            @Override
            public int getColumnCount() {
                return 2;
            }

            @Override
            public Class<?> getColumnClass(int col) {
                if (col == 0) return Boolean.class;
                return String.class;
            }

            @Override
            public Object getValueAt(int row, int col) {
                PlanSelection element = elements.get(row);
                if (col == 0) return element.isSelected;
                return element.name;
            }

            @Override
            public boolean isCellEditable(int row, int col) {
                if (col == 0) return true;
                return false;
            }

            @Override
            public void setValueAt(Object value, int row, int col) {
                PlanSelection element = elements.get(row);
                if (col == 0) element.isSelected = (boolean)value;
            }
        }

        class PlanSelectionSupport extends ArrayList<PlanSelection> implements TableDialogPanelSupport {
            private PlanSelectionTableModel tableModel;

            public PlanSelectionSupport(PlanSelectionTableModel tableModel) {
                this.tableModel = tableModel;
            }

            @Override
            public AbstractTableModel getTableModel() {
                return tableModel;
            }
        }

        List<PlanSelection> elements = new ArrayList<>();
        for (Folder folder : project.getFolders()) {
            for (ProjectItem projectItem : folder.getItems()) {
                if (projectItem.isPlan()) {
                    Plan plan = (Plan)projectItem;
                    PlanSelection element = new PlanSelection();
                    element.isSelected = (((PlanController)getActiveController()).getPlan() == plan);
                    element.name = folder.name + "/" + plan.getName();
                    element.plan = plan;

                    elements.add(element);
                }
            }
        }

        PlanSelectionTableModel model = new PlanSelectionTableModel(elements);
        PlanSelectionSupport values = new PlanSelectionSupport(model);
        InputTableElement<PlanSelection> element = new InputTableElement<>(L10.get(L10.SELECT_PLANS), values);

        InputDialogPanel inputDialogPanel = new InputDialogPanel(Arrays.asList(element));
        DialogButton result = inputDialogPanel.displayView(L10.get(L10.MENU_SHOW_PLAN_IN_3D_NAME), DialogButton.OK, DialogButton.CANCEL);
        if (!DialogButton.OK.equals(result)) return;

        ItemList<AbstractMaterialItem> items = new ItemList<>();
        List<Light> lights = new ArrayList<>();
        for (PlanSelection planSelection : elements) {
            if (planSelection.isSelected) {
                items.addAll(planSelection.plan.getMaterialItems());
                lights.addAll(planSelection.plan.getLights());
            }
        }
        if (items.isEmpty()) return;

        J3dDialog j3d = new J3dDialog();
        j3d.addModelToUniverse(items, lights);
        j3d.setVisible(true);
    }

    public boolean doMenuAction(Menu menu, Menu.Source source) {
        /* Project */
        if (Menu.EDIT_PROJECT.equals(menu)) { editProject(); return true; }
        if (Menu.EDIT_FOLDERS.equals(menu)) { editFolders(); return true; }
        if (Menu.SHOW_PROJECT_IN_3D.equals(menu)) { show3D(); return true; }
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
        if (itemController != null && itemController.doMenuAction(menu, source)) return true;

        return false;
    }
}
