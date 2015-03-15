package com.tsoft.myprocad.viewcontroller.state;

import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.CollectionEvent;
import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.util.Cursors;
import com.tsoft.myprocad.viewcontroller.PlanController;
import com.tsoft.myprocad.viewcontroller.PlanController.Mode;

/**
 * Label creation state. This state manages transition to
 * other modes, and initial label creation.
 */
public class LabelCreationState extends AbstractModeChangeState {
    public LabelCreationState(PlanController planController) {
        super(planController);
    }

    @Override
    public Mode getMode() {
        return Mode.LABEL_CREATION;
    }

    @Override
    public void enter() {
        planPanel.setCursor(Cursors.DRAW);
    }

    @Override
    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        Label label = getLabelController().createLabel(x, y);
        history.push(CollectionEvent.Type.ADD, label);


        if (Application.getInstance().isItemCreationToggled())
            setState(getLabelController().labelCreationState);
        else
            setState(getSelectionState());
    }
}
