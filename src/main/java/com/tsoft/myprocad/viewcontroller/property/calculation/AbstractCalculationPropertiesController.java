package com.tsoft.myprocad.viewcontroller.property.calculation;

import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;

public abstract class AbstractCalculationPropertiesController<T> extends AbstractPropertiesController<T> {
    public AbstractCalculationPropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(null, propertiesManagerPanel);

        init();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
