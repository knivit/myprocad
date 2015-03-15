package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.l10n.Language;
import com.tsoft.myprocad.model.Application;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class ApplicationPropertiesController extends AbstractPropertiesController<Application> {
    public ApplicationPropertiesController(PropertiesManagerPanel propertiesManagerPanel) {
        super(null, propertiesManagerPanel);

        init();
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.APPLICATION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.APPLICATION_LANGUAGE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(Language.values())
            .setValueGetter(application -> ((Application) application).getLanguage())
            .setValueSetter((application, value) -> { if (value != null) ((Application) application).setLanguage((Language) value); });
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
