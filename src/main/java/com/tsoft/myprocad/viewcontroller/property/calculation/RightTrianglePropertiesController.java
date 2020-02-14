package com.tsoft.myprocad.viewcontroller.property.calculation;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.calculation.RightTriangle;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;

public class RightTrianglePropertiesController extends AbstractCalculationPropertiesController<RightTriangle> {
    public RightTrianglePropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(propertiesManagerPanel);
    }

    @Override
    protected void initObjectProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_A_LEG_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((RightTriangle) entity).getALeg())
            .setValueSetter((entity, value) -> {
                ((RightTriangle) entity).setALeg((Integer) value);
                loadAndRefreshValues();
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_B_LEG_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((RightTriangle) entity).getBLeg())
            .setValueSetter((entity, value) -> {
                ((RightTriangle) entity).setBLeg((Integer) value);
                loadAndRefreshValues();
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_C_HYPOTENUSE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(entity -> ((RightTriangle) entity).getCHypotenuse())
            .setValueSetter((entity, value) -> {
                ((RightTriangle) entity).setCHypotenuse((Integer) value);
                loadAndRefreshValues();
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_A_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((RightTriangle) entity).getAAngle())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_B_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((RightTriangle) entity).getBAngle())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_AREA_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((RightTriangle) entity).getArea())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_AREA_M2_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((RightTriangle) entity).getAreaM2())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_PERIMETER_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(entity -> ((RightTriangle) entity).getPerimeter())
        );
    }
}
