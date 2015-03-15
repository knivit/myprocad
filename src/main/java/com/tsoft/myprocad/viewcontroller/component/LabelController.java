package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.LabelCreationState;
import com.tsoft.myprocad.viewcontroller.state.LabelResizeState;
import java.util.List;

public class LabelController {
    private Plan plan;
    private PlanController planController;

    public final ControllerState labelCreationState;
    public final ControllerState labelResizeState;

    public LabelController(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();

        labelCreationState = new LabelCreationState(planController);
        labelResizeState = new LabelResizeState(planController);
    }

    public Label createLabel(float x, float y) {
        Label label = plan.createLabel(x, y, L10.get(L10.NEW_LABEL_TEXT));
        planController.selectAndShowItems(new ItemList<Item>(label));
        return label;
    }

    /**
     * Returns the selected wall with a start point
     * at (<code>x</code>, <code>y</code>).
     */
    public Label getResizedLabelStartAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Label) {
            Label label = (Label)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (label.containsItemStartAt(x, y, margin)) {
                return label;
            }
        }
        return null;
    }

    /**
     * Returns the selected wall with an end point at (<code>x</code>, <code>y</code>).
     */
    public Label getResizedLabelEndAt(float x, float y) {
        List<Item> selectedItems = planController.getSelectedItems();
        if (selectedItems.size() == 1 && selectedItems.get(0) instanceof Label) {
            Label label = (Label)selectedItems.get(0);
            float margin = PlanController.INDICATOR_PIXEL_MARGIN / planController.getScale();
            if (label.containsItemEndAt(x, y, margin)) {
                return label;
            }
        }
        return null;
    }
}
