package com.tsoft.myprocad.viewcontroller.property.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.calculation.Triangle;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

public class TrianglePropertiesController extends AbstractCalculationPropertiesController<Triangle> {
    public TrianglePropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(propertiesManagerPanel);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_A_LEG_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((Triangle) entity).getALeg())
            .setValueSetter((entity, value) -> {
                ((Triangle) entity).setALeg((Integer) value);
                loadAndRefreshValues();
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_B_LEG_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((Triangle) entity).getBLeg())
            .setValueSetter((entity, value) -> {
                ((Triangle) entity).setBLeg((Integer) value);
                loadAndRefreshValues();
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_C_LEG_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((Triangle) entity).getCLeg())
            .setValueSetter((entity, value) -> {
                ((Triangle) entity).setCLeg((Integer) value);
                loadAndRefreshValues();
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_A_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getAAngle());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_B_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getBAngle());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_C_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getCAngle());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_AREA_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getArea());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_AREA_M2_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getAreaM2());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_PERIMETER_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((Triangle) entity).getPerimeter());
    }
}