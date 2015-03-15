package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.LevelMark;
import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;

public class LevelMarkCreationState extends AbstractModeChangeState {
    public LevelMarkCreationState(PlanController planController) {
        super(planController);
    }

    @Override
    public PlanController.Mode getMode() {
        return PlanController.Mode.LEVEL_MARK_CREATION;
    }

    @Override
    public void enter() {
        planPanel.setCursor(Cursors.DRAW);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        LevelMark levelMark = getLevelMarkController().createLevelMark(x, y);
        history.push(CollectionEvent.Type.ADD, levelMark);

        if (Application.getInstance().isItemCreationToggled())
            setState(getLevelMarkController().levelMarkCreationState);
        else
            setState(getSelectionState());
    }
}
