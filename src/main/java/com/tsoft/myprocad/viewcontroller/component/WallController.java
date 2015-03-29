package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.WallCreationState;
import com.tsoft.myprocad.viewcontroller.state.WallDrawingState;
import com.tsoft.myprocad.viewcontroller.state.WallResizeState;

import java.util.List;

public class WallController {
    private Plan plan;
    private PlanController planController;

    public final ControllerState wallCreationState;
    public final ControllerState wallDrawingState;
    public final ControllerState wallResizeState;

    public WallController(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();

        wallCreationState = new WallCreationState(planController);
        wallDrawingState = new WallDrawingState(planController);
        wallResizeState = new WallResizeState(planController);
    }

    public Wall createWall(float xStart, float yStart, float xEnd, float yEnd) {
        return plan.createWall(xStart, yStart, plan.getLevel().getStart(), xEnd, yEnd, plan.getLevel().getEnd());
    }

    /**
     * Returns the selected wall with a start point
     * at (<code>x</code>, <code>y</code>).
     */
    public Wall getResizedWallStartAt(float x, float y) {
        ItemList<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Wall) {
            Wall wall = (Wall)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (wall.containsItemStartAt(x, y, margin)) {
                return wall;
            }
        }
        return null;
    }

    /**
     * Returns the selected wall with an end point at (<code>x</code>, <code>y</code>).
     */
    public Wall getResizedWallEndAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Wall) {
            Wall wall = (Wall)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (wall.containsItemEndAt(x, y, margin)) {
                return wall;
            }
        }
        return null;
    }
}
