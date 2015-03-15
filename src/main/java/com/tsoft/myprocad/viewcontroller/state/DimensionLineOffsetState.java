package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.DimensionLine;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

import java.awt.geom.Line2D;

public class DimensionLineOffsetState extends ControllerState {
    private DimensionLine selectedDimensionLine;
    private DimensionLine historyDimensionLine;

    private int oldOffset;
    private float deltaXToOffsetPoint;
    private float deltaYToOffsetPoint;

    public DimensionLineOffsetState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.SELECTION;
    }

    @Override
    public void enter() {
        selectedDimensionLine = (DimensionLine)planController.getSelectedItems().get(0);
        historyDimensionLine = (DimensionLine)selectedDimensionLine.historyClone();

        oldOffset = selectedDimensionLine.getOffset();

        double angle = Math.atan2(selectedDimensionLine.getYEnd() - selectedDimensionLine.getYStart(),
                selectedDimensionLine.getXEnd() - selectedDimensionLine.getXStart());
        float dx = (float)-Math.sin(angle) * this.oldOffset;
        float dy = (float)Math.cos(angle) * this.oldOffset;
        float xMiddle = (selectedDimensionLine.getXStart() + selectedDimensionLine.getXEnd()) / 2 + dx;
        float yMiddle = (selectedDimensionLine.getYStart() + selectedDimensionLine.getYEnd()) / 2 + dy;
        deltaXToOffsetPoint = getXLastMousePress() - xMiddle;
        deltaYToOffsetPoint = getYLastMousePress() - yMiddle;

        planPanel.setResizeIndicatorVisible(true);
    }

    @Override
    public void moveMouse(float x, float y) {
        float newX = x - deltaXToOffsetPoint;
        float newY = y - deltaYToOffsetPoint;

        float distanceToDimensionLine =
                (float) Line2D.ptLineDist(selectedDimensionLine.getXStart(), selectedDimensionLine.getYStart(),
                        selectedDimensionLine.getXEnd(), selectedDimensionLine.getYEnd(), newX, newY);
        int relativeCCW = Line2D.relativeCCW(selectedDimensionLine.getXStart(), selectedDimensionLine.getYStart(),
                selectedDimensionLine.getXEnd(), selectedDimensionLine.getYEnd(), newX, newY);
        int offset = Math.round(-Math.signum(relativeCCW) * distanceToDimensionLine);
        selectedDimensionLine.setOffset(offset);

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void releaseMouse(float x, float y) {
        setState(getSelectionState());
        history.push(historyDimensionLine);
    }

    @Override
    public void escape() {
        selectedDimensionLine.setOffset(oldOffset);
        setState(getSelectionState());
    }

    @Override
    public void exit() {
        planPanel.setResizeIndicatorVisible(false);
        selectedDimensionLine = null;
    }
}
