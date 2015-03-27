package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;

import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Default selection state. This state manages transition to other modes,
 * the deletion of selected items, and the move of selected items with arrow keys.
 */
public class SelectionState extends AbstractModeChangeState {
    private boolean isStateActive;

    public SelectionState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        isStateActive = true;
        moveMouse(getXLastMouseMove(), getYLastMouseMove());
        selectionChanged(null);
    }

    public void selectionChanged(ItemList selectedItems) {
        if (!isStateActive) return;

        boolean oneItemSelection = (selectedItems != null && selectedItems.size() == 1);
        planPanel.setResizeIndicatorVisible(oneItemSelection);
    }

    @Override
    public void moveMouse(float x, float y) {
        if (getDimensionLineController().getResizedDimensionLineStartAt(x, y) != null
                || getDimensionLineController().getResizedDimensionLineEndAt(x, y) != null
                || getWallController().getResizedWallStartAt(x, y) != null
                || getWallController().getResizedWallEndAt(x, y) != null
                || getBeamController().getResizedBeamStartAt(x, y) != null
                || getBeamController().getResizedBeamEndAt(x, y) != null) {
            planPanel.setCursor(Cursors.RESIZE);
            return;
        }

        if (getDimensionLineController().getOffsetDimensionLineAt(x, y) != null) {
            planPanel.setCursor(Cursors.HEIGHT);
            return;
        }

        // If a selected item is under cursor position
        if (planController.isItemSelectedAt(x, y)) {
            planPanel.setCursor(Cursors.MOVE);
        } else {
            planPanel.setCursor(Cursors.SELECTION);
        }
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        if (clickCount != 1) return;

        if (getDimensionLineController().getResizedDimensionLineStartAt(x, y) != null || getDimensionLineController().getResizedDimensionLineEndAt(x, y) != null) {
            setState(getDimensionLineController().dimensionLineResizeState);
            return;
        }

        if (getWallController().getResizedWallStartAt(x, y) != null || getWallController().getResizedWallEndAt(x, y) != null) {
            setState(getWallController().wallResizeState);
            return;
        }

        if (getBeamController().getResizedBeamStartAt(x, y) != null || getBeamController().getResizedBeamEndAt(x, y) != null) {
            setState(getBeamController().resizeState);
            return;
        }

        if (getDimensionLineController().getOffsetDimensionLineAt(x, y) != null) {
            setState(getDimensionLineController().dimensionLineOffsetState);
            return;
        }

        if (getLabelController().getResizedLabelStartAt(x, y) != null || getLabelController().getResizedLabelEndAt(x, y) != null) {
            setState(getLabelController().labelResizeState);
            return;
        }

        ItemList<Item> items = getSelectableItemsAt(x, y);

        // If shift isn't pressed, and an item is under cursor position
        if (!shiftDown && !items.isEmpty()) {
            // Change state to SelectionMoveState
            setState(planController.getSelectionMoveState());
        } else {
            // Otherwise change state to RectangleSelectionState
            setState(planController.getRectangleSelectionState());
        }
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        isStateActive = false;
    }
}