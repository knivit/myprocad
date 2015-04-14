package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TableDialogPanel;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import java.util.Optional;

public class LevelListController {
    private Plan plan;

    public LevelListController(Plan plan) {
        this.plan = plan;
    }

    public void edit() {
        LevelList levels = (LevelList)plan.getLevels().getDeepClone();
        TableDialogPanel tableDialogPanel = new TableDialogPanel(plan, levels,
                (entity, value) -> { return ((Plan)entity).validateLevels((TableDialogPanelSupport)value); });

        DialogButton result = tableDialogPanel.displayView(L10.get(L10.EDIT_LEVELS_TITLE), DialogButton.SAVE, DialogButton.CANCEL);
        if (DialogButton.SAVE.equals(result)) {
            // if the current level was removed, use the first
            Optional<Level> currentLevel = levels.findByName(plan.getLevel().getName());
            plan.setLevels(levels);
            if (!currentLevel.isPresent()) plan.setLevel(levels.get(0));
        }
    }
}
