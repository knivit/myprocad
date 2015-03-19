package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.History;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.swing.PlanPanel;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;
import com.tsoft.myprocad.viewcontroller.component.*;

public abstract class ControllerState  {
    protected Plan plan;
    protected PlanController planController;
    protected History history;

    protected PlanPanel planPanel;

    public abstract Mode getMode();

    public ControllerState(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();
        this.planPanel = planController.planPanel;
        this.history = planController.history;
    }

    public void enter() { }

    public void exit() { }

    public void setMode(Mode mode) { }

    public void deleteSelection() { }

    public void escape() { }

    public void moveSelection(int dx, int dy) { }

    public void rotateSelection(int dx, int dy) { }

    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) { }

    public void releaseMouse(float x, float y) { }

    public void moveMouse(float x, float y) { }

    public void zoom(float factor) { }

    public WallController getWallController() { return planController.wallController; }

    public BeamController getBeamController() { return planController.beamController; }

    public DimensionLineController getDimensionLineController() {
        return planController.dimensionLineController;
    }

    public LabelController getLabelController() { return planController.labelController; }

    public LevelMarkController getLevelMarkController() {
        return planController.levelMarkController;
    }

    protected ControllerState getSelectionState() {
        return planController.getSelectionState();
    }

    protected void setState(ControllerState state) {
        planController.setState(state);
    }

    protected float getXLastMousePress() {
        return planController.getXLastMousePress();
    }

    protected float getYLastMousePress() {
        return planController.getYLastMousePress();
    }

    protected float getXLastMouseMove() {
        return planController.getXLastMouseMove();
    }

    protected float getYLastMouseMove() {
        return planController.getYLastMouseMove();
    }

    protected ItemList<Item> getSelectableItemsAt(float x, float y) {
        return planController.getSelectableItemsAt(x, y);
    }

    protected void selectItem(Item item) {
        planController.selectItem(item);
    }

    protected void selectItems(ItemList items) {
        planController.selectItems(items);
    }

    protected void deleteItems(ItemList<Item> items) {
        plan.deleteItems(items);
        history.push(CollectionEvent.Type.DELETE, items);
    }

    protected void deselectAll() {
        planController.deselectAll();
    }

    protected boolean wasShiftDownLastMousePress() {
        return planController.wasShiftDownLastMousePress();
    }
}