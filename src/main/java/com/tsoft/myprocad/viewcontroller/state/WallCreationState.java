package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Wall creation state. This state manages transition to other modes,
 * and initial wall creation.
 */
public class WallCreationState extends AbstractModeChangeState {
    public WallCreationState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.WALL_CREATION;
    }

    @Override
    public void enter() {
        planPanel.setCursor(Cursors.DRAW);
    }

    @Override
    public void moveMouse(float x, float y) { }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        setState(getWallController().wallDrawingState);
    }

    @Override
    public void exit() {
        planPanel.deleteFeedback();
    }
}
