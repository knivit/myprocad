package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.function.Function;

public class CalculationsDialogPanel<T> extends AbstractDialogPanel {
    public CalculationsDialogPanel(Function<PropertiesManagerPanel, AbstractPropertiesController<T>> constructor, T entity, String pictureFileName) {
        super(new BorderLayout());

        JLabel picLabel = new JLabel(new ImageIcon(MyProCAD.class.getResource(pictureFileName)));
        add(BorderLayout.NORTH, picLabel);

        PropertiesManagerPanel propertiesManagerPanel = new PropertiesManagerPanel();
        add(BorderLayout.CENTER, propertiesManagerPanel);

        AbstractPropertiesController propertiesController = constructor.apply(propertiesManagerPanel);
        propertiesController.selectObjects(Arrays.asList(entity));
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return new Dimension(600, 600);
    }
}