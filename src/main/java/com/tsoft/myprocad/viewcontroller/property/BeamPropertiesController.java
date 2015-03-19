package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PatternComboBoxPropertyEditor;

import java.awt.*;
import java.util.Collections;

public class BeamPropertiesController extends AbstractComponentPropertiesController<Beam> {
    private ObjectProperty material;

    public BeamPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        super.initObjectProperties();

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_WIDTH_PROPERTY))
                .setItemProperty(Beam.WIDTH_PROPERTY);

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_HEIGHT_PROPERTY))
                .setItemProperty(Beam.HEIGHT_PROPERTY);

        material = new ObjectProperty(this)
                .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_MATERIAL_PROPERTY))
                .setType(ComboBoxPropertyEditor.class)
                .setAvailableValues(getAvailableMaterials())
                .setValueGetter(beam -> ((Beam)beam).getMaterial())
                .setValueSetter((beam, value) -> ((Beam)beam).setMaterial((Material) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_PATTERN_PROPERTY))
                .setType(PatternComboBoxPropertyEditor.class)
                .setAvailableValues(Pattern.values())
                .setValueGetter(beam -> ((Beam) beam).getPattern())
                .setValueSetter((beam, value) -> ((Beam) beam).setPattern(((Pattern) value)));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BACKGROUND_COLOR_PROPERTY))
                .setType(ColorPropertyEditor.class)
                .setValueGetter(beam -> new Color((int)((Beam) beam).getPropertyValue(Beam.BACKGROUND_COLOR_PROPERTY)))
                .setValueSetter((beam, value) -> ((Beam) beam).setPropertyValue(Beam.BACKGROUND_COLOR_PROPERTY, ((Color) value).getRGB()));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.FOREGROUND_COLOR_PROPERTY))
                .setType(ColorPropertyEditor.class)
                .setValueGetter(beam -> new Color((int)((Beam) beam).getPropertyValue(Beam.FOREGROUND_COLOR_PROPERTY)))
                .setValueSetter((beam, value) -> ((Beam) beam).setPropertyValue(Beam.FOREGROUND_COLOR_PROPERTY, ((Color) value).getRGB()));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BORDER_COLOR_PROPERTY))
                .setType(ColorPropertyEditor.class)
                .setValueGetter(beam -> new Color((int)((Beam) beam).getPropertyValue(Beam.BORDER_COLOR_PROPERTY)))
                .setValueSetter((beam, value) -> ((Beam) beam).setPropertyValue(Beam.BORDER_COLOR_PROPERTY, ((Color) value).getRGB()));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BORDER_WIDTH_PROPERTY))
                .setItemProperty(Beam.BORDER_WIDTH_PROPERTY);
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
