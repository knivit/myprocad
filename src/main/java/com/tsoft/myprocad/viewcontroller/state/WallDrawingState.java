package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Wall drawing state. This state manages wall creation at each mouse press.
 */
public class WallDrawingState extends ControllerState {
    private float xStart;
    private float yStart;
    private Wall newWall;

    public WallDrawingState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.WALL_CREATION;
    }

    @Override
    public void setMode(Mode mode) {
        // Escape current creation and change state to matching mode
        escape();

        if (mode == Mode.SELECTION) {
            setState(getSelectionState());
        } else if (mode == Mode.PANNING) {
            setState(planController.getPanningState());
        } else if (mode == Mode.BEAM_CREATION) {
            setState(getBeamController().creationState);
        } else if (mode == Mode.DIMENSION_LINE_CREATION) {
            setState(getDimensionLineController().dimensionLineCreationState);
        } else if (mode == Mode.LABEL_CREATION) {
            setState(getLabelController().labelCreationState);
        } else if (mode == Mode.LEVEL_MARK_CREATION) {
            setState(getLevelMarkController().levelMarkCreationState);
        }
    }

    @Override
    public void enter() {
        super.enter();

        xStart = getXLastMouseMove();
        yStart = getYLastMouseMove();

        newWall = null;
        deselectAll();
    }

    @Override
    public void moveMouse(float x, float y) {
        // If current wall doesn't exist
        if (newWall == null) {
            // Create a new one
            newWall = getWallController().createWall(xStart, yStart, x, y);
            selectItem(newWall);
        } else {
            // Otherwise update its end point
            newWall.setXEnd(x);
            newWall.setYEnd(y);
        }

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        if (clickCount == 2) {
            if (Application.getInstance().isItemCreationToggled()) {
                setWallCreationState();
                endWallCreation();
            } else {
                endWallCreation();
                setState(getSelectionState());
            }
            return;
        }

        if (!Application.getInstance().isItemCreationToggled()) return;

        // Create a new wall only when it will have a distance between start and end points > 0
        if (newWall != null && (newWall.getXDistance() > 0 || newWall.getYDistance() > 0)) {
            selectItem(newWall);
            endWallCreation();
        }
    }

    private void setWallCreationState() {
        setState(getWallController().wallCreationState);
    }

    private void endWallCreation() {
        if (newWall != null) {
            xStart = newWall.getXEnd();
            yStart = newWall.getYEnd();

            history.push(CollectionEvent.Type.ADD, newWall);
        }
        newWall = null;
    }

    @Override
    public void escape() {
        if (newWall != null) plan.deleteItem(newWall);
        setWallCreationState();
    }

    @Override
    public void exit() {
        newWall = null;
        planPanel.deleteFeedback();
    }
}
