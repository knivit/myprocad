package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.common.swing.renderer.ObjectListCellRenderer;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TableDialogPanel;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ObjectListPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;
    private ObjectListCellRenderer label;
    private JButton button;

    private TableDialogPanelSupport values;

    public ObjectListPropertyEditor() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
        editorComponent.add("*", label = new ObjectListCellRenderer());
        label.setOpaque(false);

        editorComponent.add(button = ComponentFactory.Helper.getFactory().createMiniButton());
        button.addActionListener(e -> showDialog());

        editorComponent.setOpaque(false);
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public Object getValue() {
        return values;
    }

    @Override
    public void setValue(Object value) {
        values = (TableDialogPanelSupport)value;
        label.setValue(values);
    }

    protected void showDialog() {
        TableDialogPanelSupport clonedValues = values.getDeepClone();

        ObjectProperty.Validator validator = (getObjectProperty() == null) ? null : getObjectProperty().getValueValidator();
        List selectedItems = (getObjectProperty() == null) ? null : getObjectProperty().getPropertiesController().getSelectedItems();
        Object entity = (selectedItems == null || selectedItems.isEmpty()) ? null : selectedItems.get(0);

        String title = getObjectProperty().getLabelName();
        TableDialogPanel tableDialogPanel = new TableDialogPanel(entity, clonedValues, validator);
        DialogButton result = tableDialogPanel.displayView(title, DialogButton.SAVE, DialogButton.CANCEL);
        if (DialogButton.SAVE.equals(result)) {
            TableDialogPanelSupport oldValues = values;
            setValue(clonedValues);
            firePropertyChange(oldValues, clonedValues);
        }
    }
}
