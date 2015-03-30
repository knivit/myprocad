package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.property.ObjectProperty;

public class BeamPropertiesController extends AbstractComponentPropertiesController<Beam> {
    public BeamPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        addCommonProperties();

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_WIDTH_PROPERTY))
                .setType(Integer.class)
                .setValueGetter(item -> ((Beam) item).getWidth())
                .setValueValidator((item, value) -> { return ((Beam)item).validateWidth((Integer)value); })
                .setValueSetter((item, value) -> {
                    addToHistory((Beam) item);
                    ((Beam) item).setWidth((int) value);
                });

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_HEIGHT_PROPERTY))
                .setType(Integer.class)
                .setValueGetter(item -> ((Beam) item).getHeight())
                .setValueValidator((item, value) -> { return ((Beam)item).validateHeight((Integer)value); })
                .setValueSetter((item, value) -> {
                    addToHistory((Beam) item);
                    ((Beam) item).setHeight((int) value);
                });

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.INFO_CATEGORY))
                .setLabelName(L10.get(L10.LENGTH_PROPERTY))
                .setType(Float.class)
                .setValueGetter(item -> ((Beam) item).getLength());

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_XOZ_ANGLE_PROPERTY))
                .setType(Integer.class)
                .setValueGetter(item -> ((Beam) item).getXozAngle());

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_XOY_ANGLE_PROPERTY))
                .setType(Integer.class)
                .setValueGetter(item -> ((Beam) item).getXoyAngle());

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.VIEW_CATEGORY))
                .setLabelName(L10.get(L10.BEAM_YOZ_ANGLE_PROPERTY))
                .setType(Integer.class)
                .setValueGetter(item -> ((Beam) item).getYozAngle());

        addMaterialItemProperties();
    }

    @Override
    public void refreshValues() {
        refreshMaterialList();
        super.refreshValues();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.BEAM_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
