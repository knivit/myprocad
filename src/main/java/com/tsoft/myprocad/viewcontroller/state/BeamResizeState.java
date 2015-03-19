package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Beam;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class BeamResizeState extends ControllerState {
    private Beam selectedBeam;
    private Beam historyBeam;

    private boolean startPoint;
    private int oldX;
    private int oldY;
    private float deltaXToResizePoint;
    private float deltaYToResizePoint;

    public BeamResizeState(PlanController planController) {
        super(planController);
    }

    @Override
    public PlanController.Mode getMode() {
        return PlanController.Mode.SELECTION;
    }

    @Override
    public void enter() {
        super.enter();

        selectedBeam = (Beam)planController.getSelectedItems().get(0);
        historyBeam = (Beam)selectedBeam.historyClone();
        startPoint = (selectedBeam == getBeamController().getResizedBeamStartAt(getXLastMousePress(), getYLastMousePress()));
        if (startPoint) {
            oldX = selectedBeam.getXStart();
            oldY = selectedBeam.getYStart();
        } else {
            oldX = selectedBeam.getXEnd();
            oldY = selectedBeam.getYEnd();
        }
        deltaXToResizePoint = getXLastMousePress() - oldX;
        deltaYToResizePoint = getYLastMousePress() - oldY;

        planPanel.setResizeIndicatorVisible(true);
    }

    @Override
    public void moveMouse(float x, float y) {
        float newX = x - deltaXToResizePoint;
        float newY = y - deltaYToResizePoint;
        selectedBeam.movePoint(Math.round(newX), Math.round(newY), startPoint);

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        setState(getSelectionState());
        history.push(historyBeam);
    }

    @Override
    public void escape() {
        selectedBeam.movePoint(oldX, oldY, startPoint);
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        selectedBeam = null;
        planPanel.deleteFeedback();
    }
}