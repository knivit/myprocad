package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Dimension line creation state. This state manages transition to
 * other modes, and initial dimension line creation.
 */
public class DimensionLineCreationState extends AbstractModeChangeState {
    public DimensionLineCreationState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.DIMENSION_LINE_CREATION;
    }

    @Override
    public void enter() {
        planPanel.setCursor(Cursors.DRAW);
    }

    @Override
    public void moveMouse(float x, float y) {
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        // Ignore double clicks (may happen when state is activated returning from DimensionLineDrawingState)
        if (clickCount == 1) {
            // Change state to DimensionLineDrawingState
            setState(getDimensionLineController().dimensionLineDrawingState);
        }
    }

    @Override
    public void exit() {
        planPanel.deleteFeedback();
    }
}
