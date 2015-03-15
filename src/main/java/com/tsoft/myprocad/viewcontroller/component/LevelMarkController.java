package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.LevelMark;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.LevelMarkCreationState;

public class LevelMarkController {
    private Plan plan;
    private PlanController planController;

    public final ControllerState levelMarkCreationState;

    public LevelMarkController(PlanController planController) {
        this.planController = planController;
        this.plan = planController.getPlan();

        levelMarkCreationState = new LevelMarkCreationState(planController);
    }

    public LevelMark createLevelMark(float fx, float fy) {
        int x = Math.round(fx / 10.0f) * 10;
        int y = Math.round(fy / 10.0f) * 10;
        LevelMark levelMark = plan.createLevelMark(x - 300, x + 150, y - 200, y);
        planController.selectAndShowItems(new ItemList<Item>(levelMark));
        return levelMark;
    }
}
