package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class LabelResizeState extends ControllerState {
    private Label selectedLabel;
    private Label historyLabel;

    private boolean startPoint;
    private int oldX;
    private int oldY;
    private float deltaXToResizePoint;
    private float deltaYToResizePoint;

    public LabelResizeState(PlanController planController) {
        super(planController);
    }

    @Override
    public PlanController.Mode getMode() {
        return PlanController.Mode.SELECTION;
    }

    @Override
    public void enter() {
        super.enter();

        selectedLabel = (Label)planController.getSelectedItems().get(0);
        historyLabel = (Label)selectedLabel.historyClone();

        startPoint = (selectedLabel == getLabelController().getResizedLabelStartAt(getXLastMousePress(), getYLastMousePress()));
        if (startPoint) {
            oldX = selectedLabel.getXStart();
            oldY = selectedLabel.getYStart();
        } else {
            oldX = selectedLabel.getXEnd();
            oldY = selectedLabel.getYEnd();
        }
        deltaXToResizePoint = getXLastMousePress() - oldX;
        deltaYToResizePoint = getYLastMousePress() - oldY;

        planPanel.setResizeIndicatorVisible(true);
    }

    @Override
    public void moveMouse(float x, float y) {
        float newX = x - deltaXToResizePoint;
        float newY = y - deltaYToResizePoint;
        selectedLabel.movePoint(Math.round(newX), Math.round(newY), startPoint);

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        setState(getSelectionState());
        history.push(historyLabel);
    }

    @Override
    public void escape() {
        selectedLabel.movePoint(oldX, oldY, startPoint);
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        selectedLabel = null;
    }
}