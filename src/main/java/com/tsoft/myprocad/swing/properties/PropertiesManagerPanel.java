package com.tsoft.myprocad.swing.properties;

import com.l2fprod.common.propertysheet.PropertySheet;
import com.l2fprod.common.propertysheet.PropertySheetPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class PropertiesManagerPanel extends JPanel {
    private PropertySheetPanel propertySheetPanel;

    private JLabel infoLabel;

    public PropertiesManagerPanel() {
        super(new BorderLayout());

        createComponents();
    }

    private void createComponents() {
        propertySheetPanel = new PropertySheetPanel();
        propertySheetPanel.setMode(PropertySheet.VIEW_AS_CATEGORIES);
        propertySheetPanel.setToolBarVisible(false);

        // add components
        infoLabel = new JLabel();
        add(infoLabel, BorderLayout.PAGE_START);
        add(propertySheetPanel, BorderLayout.CENTER);
    }

    public JComponent linkTo(JComponent panel) {
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this, panel);
        splitPane.setFocusable(false);

        splitPane.setDividerLocation(350);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setResizeWeight(0.3);
        splitPane.setBorder(null);
        return splitPane;
    }

    public void setProperties(List<SheetProperty> properties) {
        setProperties(propertySheetPanel, properties);
    }

    private void setProperties(PropertySheetPanel sheetPanel, List<SheetProperty> properties) {
        sheetPanel.setProperties(properties);
    }

    public void refreshValues() {
        refreshValues(propertySheetPanel);
    }

    public void refreshValues(PropertySheetPanel sheetPanel) {
        for (SheetProperty sheetProperty : sheetPanel.getProperties()) {
            sheetProperty.removePropertyChangeListener(sheetProperty);
            sheetProperty.refreshValue();
            sheetProperty.addPropertyChangeListener(sheetProperty);
        }
    }

    public void setInfoMessage(String text) {
        infoLabel.setText(text);
    }
}
