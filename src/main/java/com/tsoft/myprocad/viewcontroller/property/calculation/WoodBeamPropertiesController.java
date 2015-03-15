package com.tsoft.myprocad.viewcontroller.property.calculation;

import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.beans.editor.ObjectListPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.calculation.BeamSag;
import com.tsoft.myprocad.model.calculation.Load1List;
import com.tsoft.myprocad.model.calculation.Load2List;
import com.tsoft.myprocad.model.calculation.WoodBeam;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

public class WoodBeamPropertiesController extends AbstractCalculationPropertiesController<WoodBeam> {
    public WoodBeamPropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(propertiesManagerPanel);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_L_PROPERTY))
                .setType(Double.class)
                .setValueGetter(entity -> ((WoodBeam) entity).getL())
                .setValueValidator((entity, value) -> ((WoodBeam)entity).validateL(((Double) value)))
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setL((Double) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_B_PROPERTY))
                .setType(Double.class)
                .setValueGetter(entity -> ((WoodBeam) entity).getB())
                .setValueValidator((entity, value) -> ((WoodBeam)entity).validateB(((Double) value)))
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setB((Double) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_PERMANENT_LOAD_PROPERTY))
                .setType(ObjectListPropertyEditor.class)
                .setValueGetter(entity -> ((WoodBeam) entity).getPermanentLoad())
                .setValueValidator((entity, value) -> ((WoodBeam)entity).validatePermanentLoad((Load1List) value))
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setPermanentLoad((Load1List) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_TEMPORARY_LOAD_PROPERTY))
                .setType(ObjectListPropertyEditor.class)
                .setValueGetter(entity -> ((WoodBeam) entity).getTemporaryLoad())
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setTemporaryLoad((Load2List) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_SAG_PROPERTY))
                .setType(ComboBoxPropertyEditor.class)
                .setAvailableValues(BeamSag.values())
                .setValueGetter(entity -> ((WoodBeam) entity).getSag())
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setSag((BeamSag) value));

        new ObjectProperty(this)
                .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
                .setLabelName(L10.get(L10.CALCULATION_WOOD_CALC_ALL_PROPERTY))
                .setType(Boolean.class)
                .setValueGetter(entity -> ((WoodBeam) entity).isCalcAll())
                .setValueSetter((entity, value) -> ((WoodBeam) entity).setCalcAll((Boolean) value));
    }
}
