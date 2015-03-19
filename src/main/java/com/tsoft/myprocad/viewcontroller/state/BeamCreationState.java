package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class BeamCreationState extends AbstractModeChangeState {
    public BeamCreationState(PlanController planController) {
        super(planController);
    }

    @Override
    public PlanController.Mode getMode() {
        return PlanController.Mode.BEAM_CREATION;
    }

    @Override
    public void enter() {
        planPanel.setCursor(Cursors.DRAW);
    }

    @Override
    public void moveMouse(float x, float y) { }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        setState(getBeamController().drawingState);
    }

    @Override
    public void exit() {
        planPanel.deleteFeedback();
    }
}