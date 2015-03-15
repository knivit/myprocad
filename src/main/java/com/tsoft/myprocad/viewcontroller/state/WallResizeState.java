package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

public class WallResizeState extends ControllerState {
    private Wall selectedWall;
    private Wall historyWall;

    private boolean startPoint;
    private int oldX;
    private int oldY;
    private float deltaXToResizePoint;
    private float deltaYToResizePoint;

    public WallResizeState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        super.enter();

        selectedWall = (Wall)planController.getSelectedItems().get(0);
        historyWall = (Wall)selectedWall.historyClone();
        startPoint = (selectedWall == getWallController().getResizedWallStartAt(getXLastMousePress(), getYLastMousePress()));
        if (startPoint) {
            oldX = selectedWall.getXStart();
            oldY = selectedWall.getYStart();
        } else {
            oldX = selectedWall.getXEnd();
            oldY = selectedWall.getYEnd();
        }
        deltaXToResizePoint = getXLastMousePress() - oldX;
        deltaYToResizePoint = getYLastMousePress() - oldY;

        planPanel.setResizeIndicatorVisible(true);
    }

    @Override
    public void moveMouse(float x, float y) {
        float newX = x - deltaXToResizePoint;
        float newY = y - deltaYToResizePoint;
        selectedWall.movePoint(Math.round(newX), Math.round(newY), startPoint);

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        setState(getSelectionState());
        history.push(historyWall);
    }

    @Override
    public void escape() {
        selectedWall.movePoint(oldX, oldY, startPoint);
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        selectedWall = null;
        planPanel.deleteFeedback();
    }
}
