package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Abstract state able to manage the transition to other modes.
 */
public abstract class AbstractModeChangeState extends ControllerState {
    public AbstractModeChangeState(PlanController planController) {
        super(planController);
    }

    @Override
    public void setMode(Mode mode) {
        if (mode == Mode.SELECTION) {
            setState(getSelectionState());
        } else if (mode == Mode.PANNING) {
            setState(planController.getPanningState());
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
    public void deleteSelection() {
        deleteItems(planController.getSelectedItems());

        // Compute again feedback
        moveMouse(getXLastMouseMove(), getYLastMouseMove());
    }

    @Override
    public void moveSelection(int dx, int dy) {
        planController.moveAndShowSelectedItems(dx, dy, 0);

        // Compute again feedback
        moveMouse(getXLastMouseMove(), getYLastMouseMove());
    }

    @Override
    public void rotateSelection(int ox, int oy) {
        planController.rotateSelectedItems(ox, oy);

        // Compute again feedback
        moveMouse(getXLastMouseMove(), getYLastMouseMove());
    }

    @Override
    public void zoom(float factor) {
        plan.setScale(plan.getScale() * factor);
    }
}