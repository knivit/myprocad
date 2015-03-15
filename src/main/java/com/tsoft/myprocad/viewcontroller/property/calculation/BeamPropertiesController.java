package com.tsoft.myprocad.viewcontroller.property.calculation;

import com.l2fprod.common.beans.editor.ObjectListPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.calculation.*;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

public class BeamPropertiesController extends AbstractCalculationPropertiesController<Beam> {
    public BeamPropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(propertiesManagerPanel);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_LENGTH_PROPERTY))
            .setType(Double.class)
            .setValueGetter(entity -> ((Beam) entity).getLength())
            .setValueValidator((entity, value) -> ((Beam)entity).validateLength(((Double)value)))
            .setValueSetter((entity, value) -> ((Beam) entity).setLength((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_LEFT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setValueGetter(entity -> ((Beam) entity).getLeftSupport())
            .setValueValidator((entity, value) -> ((Beam)entity).validateLeftSupport(((Double)value)))
            .setValueSetter((entity, value) -> ((Beam) entity).setLeftSupport((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_RIGHT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setValueGetter(entity -> ((Beam) entity).getRightSupport())
            .setValueValidator((entity, value) -> ((Beam)entity).validateRightSupport(((Double)value)))
            .setValueSetter((entity, value) -> ((Beam) entity).setRightSupport((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ELASTIC_STRENGTH_PROPERTY))
            .setType(Double.class)
            .setValueGetter(entity -> ((Beam) entity).getElasticStrength())
            .setValueSetter((entity, value) -> ((Beam) entity).setElasticStrength((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ALLOWABLE_STRESS_PROPERTY))
            .setType(Double.class)
            .setValueGetter(entity -> ((Beam) entity).getAllowableStress())
            .setValueSetter((entity, value) -> ((Beam) entity).setAllowableStress((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_BENDING_MOMENTS_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setValueGetter(entity -> ((Beam) entity).getMoments())
            .setValueValidator ((entity, value) -> ((Beam) entity).validateMoments((MomentList) value))
            .setValueSetter((entity, value) -> ((Beam) entity).setMoments((MomentList) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setValueGetter(entity -> ((Beam) entity).getForces())
            .setValueValidator((entity, value) -> ((Beam) entity).validateForces((ForceList) value))
            .setValueSetter((entity, value) -> ((Beam) entity).setForces((ForceList) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setValueGetter(entity -> ((Beam) entity).getDistributedForces())
            .setValueValidator((entity, value) -> ((Beam) entity).validateDistributedForces((DistributedForceList) value))
            .setValueSetter((entity, value) -> ((Beam) entity).setDistributedForces((DistributedForceList) value));
    }
}
