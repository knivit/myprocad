package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.Beam;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class BeamDrawingState extends ControllerState {
    private float xStart;
    private float yStart;
    private Beam newBeam;

    public BeamDrawingState(PlanController planController) {
        super(planController);
    }

    @Override
    public PlanController.Mode getMode() {
        return PlanController.Mode.BEAM_CREATION;
    }

    @Override
    public void setMode(PlanController.Mode mode) {
        // Escape current creation and change state to matching mode
        escape();

        if (mode == PlanController.Mode.SELECTION) {
            setState(getSelectionState());
        } else if (mode == PlanController.Mode.PANNING) {
            setState(planController.getPanningState());
        } else if (mode == PlanController.Mode.WALL_CREATION) {
            setState(getWallController().wallCreationState);
        } else if (mode == PlanController.Mode.DIMENSION_LINE_CREATION) {
            setState(getDimensionLineController().dimensionLineCreationState);
        } else if (mode == PlanController.Mode.LABEL_CREATION) {
            setState(getLabelController().labelCreationState);
        } else if (mode == PlanController.Mode.LEVEL_MARK_CREATION) {
            setState(getLevelMarkController().levelMarkCreationState);
        }
    }

    @Override
    public void enter() {
        super.enter();

        xStart = getXLastMouseMove();
        yStart = getYLastMouseMove();

        newBeam = null;
        deselectAll();
    }

    @Override
    public void moveMouse(float x, float y) {
        // If current wall doesn't exist
        if (newBeam == null) {
            // Create a new one
            newBeam = getBeamController().createBeam(xStart, yStart, x, y);
            selectItem(newBeam);
        } else {
            // Otherwise update its end point
            newBeam.setXEnd(x);
            newBeam.setYEnd(y);
        }

        // Ensure point at (x,y) is visible
        planPanel.makePointVisible(x, y);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        if (clickCount == 2) {
            if (Application.getInstance().isItemCreationToggled()) {
                setBeamCreationState();
                endBeamCreation();
            } else {
                endBeamCreation();
                setState(getSelectionState());
            }
            return;
        }

        if (!Application.getInstance().isItemCreationToggled()) return;

        // Create a new beam only when it will have a distance between start and end points > 0
        if (newBeam != null && (newBeam.getXDistance() > 0 || newBeam.getYDistance() > 0)) {
            selectItem(newBeam);
            endBeamCreation();
        }
    }

    private void setBeamCreationState() {
        setState(getBeamController().creationState);
    }

    private void endBeamCreation() {
        if (newBeam != null) {
            xStart = newBeam.getXEnd();
            yStart = newBeam.getYEnd();

            history.push(CollectionEvent.Type.ADD, newBeam);
        }
        newBeam = null;
    }

    @Override
    public void escape() {
        if (newBeam != null) plan.deleteItem(newBeam);
        setBeamCreationState();
    }

    @Override
    public void exit() {
        newBeam = null;
        planPanel.deleteFeedback();
    }
}
