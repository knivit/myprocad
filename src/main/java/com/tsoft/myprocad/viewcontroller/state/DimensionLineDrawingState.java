package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.DimensionLine;
import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

import java.awt.geom.Line2D;

/**
 * Dimension line drawing state. This state manages dimension line creation at mouse press.
 */
public class DimensionLineDrawingState extends ControllerState {
    private float xStart;
    private float yStart;
    private boolean editingStartPoint;
    private DimensionLine newDimensionLine;
    private boolean offsetChoice;

    public DimensionLineDrawingState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.DIMENSION_LINE_CREATION;
    }

    @Override
    public void setMode(Mode mode) {
        // Escape current creation and change state to matching mode
        escape();

        if (mode == Mode.SELECTION) {
            setState(getSelectionState());
        } else if (mode == Mode.PANNING) {
            setState(planController.getPanningState());
        } else if (mode == Mode.WALL_CREATION) {
            setState(getWallController().wallCreationState);
        } else if (mode == Mode.LABEL_CREATION) {
            setState(getLabelController().labelCreationState);
        } else if (mode == Mode.LEVEL_MARK_CREATION) {
            setState(getLevelMarkController().levelMarkCreationState);
        }
    }

    @Override
    public void enter() {
        xStart = getXLastMouseMove();
        yStart = getYLastMouseMove();
        editingStartPoint = false;
        offsetChoice = false;
        newDimensionLine = null;
    }

    @Override
    public void moveMouse(float x, float y) {
        planPanel.deleteFeedback();

        if (offsetChoice) {
            float distanceToDimensionLine = (float) Line2D.ptLineDist(
                    newDimensionLine.getXStart(), newDimensionLine.getYStart(),
                    newDimensionLine.getXEnd(), newDimensionLine.getYEnd(), x, y);
            if (newDimensionLine.getLength() > 0) {
                int relativeCCW = Line2D.relativeCCW(
                        newDimensionLine.getXStart(), newDimensionLine.getYStart(),
                        newDimensionLine.getXEnd(), newDimensionLine.getYEnd(), x, y);
                int offset = Math.round(-Math.signum(relativeCCW) * distanceToDimensionLine);
                newDimensionLine.setOffset(offset);
            }
        } else {
            // If current dimension line doesn't exist
            if (newDimensionLine == null) {
                // Create a new one
                newDimensionLine = getDimensionLineController().createDimensionLine(xStart, yStart, x, y, 0);
                selectItem(newDimensionLine);
            } else {
                // Otherwise update its end points
                if (editingStartPoint) {
                    newDimensionLine.setXStart(x);
                    newDimensionLine.setYStart(y);
                } else {
                    newDimensionLine.setXEnd(x);
                    newDimensionLine.setYEnd(y);
                }
            }
        }

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        if (newDimensionLine == null && clickCount == 2) {
            // Try to guess the item to measure
            DimensionLine dimensionLine = getDimensionLineController().getMeasuringDimensionLineAt(x, y);
            if (dimensionLine != null) {
                newDimensionLine = getDimensionLineController().createDimensionLine(
                    dimensionLine.getXStart(), dimensionLine.getYStart(),
                    dimensionLine.getXEnd(), dimensionLine.getYEnd(),
                    dimensionLine.getOffset());
            }
        }

        // Create a new dimension line only when it will have a length > 0
        // meaning after the first mouse move
        if (newDimensionLine != null) {
            selectItem(newDimensionLine);

            if (offsetChoice) {
                endDimensionLineCreation();
            } else {
                // Switch to offset choice
                offsetChoice = true;
                planPanel.setCursor(Cursors.HEIGHT);
            }
        }
    }

    private void endDimensionLineCreation() {
        history.push(CollectionEvent.Type.ADD, newDimensionLine);
        newDimensionLine = null;

        if (Application.getInstance().isItemCreationToggled())
            setState(getDimensionLineController().dimensionLineCreationState);
        else
            setState(getSelectionState());
    }

    @Override
    public void escape() {
        if (newDimensionLine != null) {
            // Delete current created dimension line
            plan.deleteDimensionLine(newDimensionLine);
        }

        // Change state to DimensionLineCreationState
        setState(getDimensionLineController().dimensionLineCreationState);
    }

    @Override
    public void exit() {
        newDimensionLine = null;
        planPanel.deleteFeedback();
    }
}
