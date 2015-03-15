package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

public class PanningState extends ControllerState {
    private Integer xLastMouseMove;
    private Integer yLastMouseMove;

    public PanningState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.PANNING;
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == Mode.SELECTION) {
            setState(getSelectionState());
        } else if (mode == Mode.WALL_CREATION) {
            setState(getWallController().wallCreationState);
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
        planPanel.setCursor(Cursors.PANNING);
    }

    @Override
    public void moveSelection(int dx, int dy) {
        planPanel.moveView(dx * 10, dy * 10);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        if (clickCount == 1) {
            xLastMouseMove = planPanel.convertXModelToScreen(x);
            yLastMouseMove = planPanel.convertYModelToScreen(y);
        } else {
            xLastMouseMove = null;
            yLastMouseMove = null;
        }
    }

    @Override
    public void moveMouse(float x, float y) {
        if (xLastMouseMove != null) {
            int newX = planPanel.convertXModelToScreen(x);
            int newY = planPanel.convertYModelToScreen(y);
            planPanel.moveView((this.xLastMouseMove - newX) / planController.getScale(), (yLastMouseMove - newY) / planController.getScale());
            xLastMouseMove = newX;
            yLastMouseMove = newY;
        }
    }

    @Override
    public void releaseMouse(float x, float y) {
        xLastMouseMove = null;
    }

    @Override
    public void escape() {
        xLastMouseMove = null;
    }

    @Override
    public void zoom(float factor) {
        plan.setScale(plan.getScale() * factor);
    }
}
