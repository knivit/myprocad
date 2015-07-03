package com.tsoft.myprocad.viewcontroller.component;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TableDialogPanel;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

public class MaterialListController {
    private Project project;

    public MaterialListController(Project project) {
        this.project = project;
    }

    public void edit() {
        MaterialList materials = (MaterialList)project.getMaterials().getDeepClone();
        materials.setProject(project);

        TableDialogPanel tableDialogPanel = new TableDialogPanel(project, materials,
                (entity, value) -> ((Project)entity).validateMaterials((TableDialogPanelSupport<Material>)value));

        DialogButton result = tableDialogPanel.displayView(L10.get(L10.EDIT_MATERIALS_TITLE), DialogButton.SAVE, DialogButton.CANCEL);
        if (DialogButton.SAVE.equals(result)) {
            project.setMaterials(materials);
        }
    }
}
