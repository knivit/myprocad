package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.property.ObjectProperty;

import java.util.Collections;

public class BeamPropertiesController extends AbstractComponentPropertiesController<Beam> {
    private ObjectProperty material;

    public BeamPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.WALL_X_CATEGORY))
                .setLabelName(L10.get(L10.WALL_START_PROPERTY))
                .setItemProperty(Beam.WIDTH_PROPERTY);

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.WALL_X_CATEGORY))
                .setLabelName(L10.get(L10.WALL_END_PROPERTY))
                .setItemProperty(Beam.HEIGHT_PROPERTY);

        material = new ObjectProperty(this)
                .setCategoryName(L10.get(L10.WALL_X_CATEGORY))
                .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
                .setItemProperty(Beam.MATERIAL_PROPERTY)
                .setType(ComboBoxPropertyEditor.class)
                .setAvailableValues(getAvailableMaterials());
    }

    @Override
    public void refreshValues() {
        refreshMaterialList();
        super.refreshValues();
    }

    public void refreshMaterialList() {
        material.setAvailableValues(getAvailableMaterials());
    }

    public Object[] getAvailableMaterials() {
        MaterialList list = getProject().getMaterials();
        Collections.sort(list);
        return list.toArray();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.BEAM_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
