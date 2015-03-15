package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;

import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;

public class PropertiesDialogPanel extends AbstractDialogPanel {
    public PropertiesDialogPanel(Function<PropertiesManagerPanel, AbstractPropertiesController> constructor, Object entity) {
        super(new BorderLayout());

        PropertiesManagerPanel propertiesManagerPanel = new PropertiesManagerPanel();

        add(BorderLayout.CENTER, propertiesManagerPanel);

        AbstractPropertiesController propertiesController = constructor.apply(propertiesManagerPanel);
        propertiesController.selectObjects(Arrays.asList(entity));
    }

    @Override
    public Dimension getDialogPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)Math.round(screenSize.width * 0.2), (int)Math.round(screenSize.height * 0.5));
    }
}
