package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.model.DimensionLine;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.model.LevelMark;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Selection;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

import java.util.Arrays;

public class PlanPropertiesManager {
    public Plan plan;
    public PropertiesManagerPanel propertiesManagerPanel;

    private SelectionPropertiesController selectionPropertiesController;
    private WallPropertiesController wallPropertiesController;
    private LabelPropertiesController labelPropertiesController;
    private DimensionLinePropertiesController dimensionLinePropertiesController;
    private LevelMarkPropertiesController levelMarkPropertiesController;
    private PlanPropertiesController planPropertiesController;

    private AbstractComponentPropertiesController currentPropertiesController;

    private boolean reloadDisabled;

    public static PlanPropertiesManager createPropertiesController(Plan plan) {
        PlanPropertiesManager planPropertiesManager = new PlanPropertiesManager(plan);
        planPropertiesManager.propertiesManagerPanel = new PropertiesManagerPanel();
        planPropertiesManager.createSubControllers();
        return planPropertiesManager;
    }

    public PlanPropertiesManager(Plan plan) {
        this.plan = plan;
    }

    private void createSubControllers() {
        selectionPropertiesController = new SelectionPropertiesController(this);
        wallPropertiesController = new WallPropertiesController(this);
        labelPropertiesController = new LabelPropertiesController(this);
        levelMarkPropertiesController = new LevelMarkPropertiesController(this);
        dimensionLinePropertiesController = new DimensionLinePropertiesController(this);
        planPropertiesController = new PlanPropertiesController(this);
    }

    public void disableReload() { reloadDisabled = true; }

    public void enableReload() { reloadDisabled = false; }

    public void planChanged() {
        if (reloadDisabled) return;

        planPropertiesController.loadValues();
        planPropertiesController.refreshValues();
    }

    public void wallChanged() {
        if (! reloadDisabled) wallPropertiesController.loadAndRefreshValues();
    }

    public void dimensionLineChanged() {
        if (!reloadDisabled) dimensionLinePropertiesController.loadAndRefreshValues();
    }

    public void labelChanged() {
        if (!reloadDisabled) labelPropertiesController.loadAndRefreshValues();
    }

    public void levelMarkChanged() {
        if (!reloadDisabled) levelMarkPropertiesController.loadAndRefreshValues();
    }

    public void materialListChanged() {
        wallPropertiesController.refreshMaterialList();
    }

    public void selectionStateChanged() {
        if (currentPropertiesController != null) currentPropertiesController.loadAndRefreshValues();
    }

    private void selectPlanPropertiesComponent() {
        currentPropertiesController = planPropertiesController;
        planPropertiesController.selectObjects(Arrays.asList(plan));
        planPropertiesController.loadValues();
    }

    // User's selection is changed, can be any elements - plan, walls, labels etc
    public void selectionChanged(ItemList<Item> selectedItems) {
        if (reloadDisabled) return;

        ItemList<Wall> walls = selectedItems.getWallsSubList();
        ItemList<Label> labels = selectedItems.getLabelsSubList();
        ItemList<DimensionLine> dimensionLines = selectedItems.getDimensionLinesSubList();
        ItemList<LevelMark> levelMarks = selectedItems.getLevelMarksSubList();

        // plan selected
        if (walls.isEmpty() && labels.isEmpty() && dimensionLines.isEmpty() && levelMarks.isEmpty()) {
            selectPlanPropertiesComponent();
            return;
        }

        // wall(s) selected
        if (!walls.isEmpty() && labels.isEmpty() && dimensionLines.isEmpty() && levelMarks.isEmpty()) {
            currentPropertiesController = wallPropertiesController;
            wallPropertiesController.selectObjects(walls);
            return;
        }

        // label(s)
        if (walls.isEmpty() && !labels.isEmpty() && dimensionLines.isEmpty() && levelMarks.isEmpty()) {
            currentPropertiesController = labelPropertiesController;
            labelPropertiesController.selectObjects(labels);
            return;
        }

        // dimension line(s)
        if (walls.isEmpty() && labels.isEmpty() && !dimensionLines.isEmpty() && levelMarks.isEmpty()) {
            currentPropertiesController = dimensionLinePropertiesController;
            dimensionLinePropertiesController.selectObjects(dimensionLines);
            return;
        }

        // level marks
        if (walls.isEmpty() && labels.isEmpty() && dimensionLines.isEmpty() && !levelMarks.isEmpty()) {
            currentPropertiesController = levelMarkPropertiesController;
            levelMarkPropertiesController.selectObjects(levelMarks);
            return;
        }

        // mix of them
        if (!walls.isEmpty() || !labels.isEmpty() || !dimensionLines.isEmpty() || !levelMarks.isEmpty()) {
            currentPropertiesController= selectionPropertiesController;
            Selection selection = new Selection(selectedItems);
            selectionPropertiesController.selectObjects(Arrays.asList(selection));
            return;
        }

        currentPropertiesController = null;
    }
}
