package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.BeamCreationState;
import com.tsoft.myprocad.viewcontroller.state.BeamDrawingState;
import com.tsoft.myprocad.viewcontroller.state.BeamResizeState;

import java.util.List;

public class BeamController {
    private Plan plan;
    private PlanController planController;

    public final ControllerState creationState;
    public final ControllerState drawingState;
    public final ControllerState resizeState;

    public BeamController(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();

        creationState = new BeamCreationState(planController);
        drawingState = new BeamDrawingState(planController);
        resizeState = new BeamResizeState(planController);
    }

    public Beam createBeam(float xStart, float yStart, float xEnd, float yEnd) {
        return plan.addBeam(xStart, yStart, plan.getLevel().getStart(), xEnd, yEnd, plan.getLevel().getEnd(), 50, 150);
    }

    /**
     * Returns the selected wall with a start point
     * at (<code>x</code>, <code>y</code>).
     */
    public Beam getResizedBeamStartAt(float x, float y) {
        ItemList<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Beam) {
            Beam beam = (Beam)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (beam.containsItemStartAt(x, y, margin)) {
                return beam;
            }
        }
        return null;
    }

    /**
     * Returns the selected wall with an end point at (<code>x</code>, <code>y</code>).
     */
    public Beam getResizedBeamEndAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Beam) {
            Beam beam = (Beam)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (beam.containsItemEndAt(x, y, margin)) {
                return beam;
            }
        }
        return null;
    }
}

