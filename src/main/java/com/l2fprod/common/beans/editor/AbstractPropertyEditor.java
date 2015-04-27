package com.l2fprod.common.beans.editor;

import com.l2fprod.common.propertysheet.CellEditorAdapter;
import com.tsoft.myprocad.model.property.ObjectProperty;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.beans.PropertyEditor;

public abstract class AbstractPropertyEditor implements PropertyEditor {
    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private ObjectProperty objectProperty;

    // Adapter made by JTable during an editing
    protected CellEditorAdapter cellEditorAdapter;

    public void addButton(ObjectProperty.Button dialogButton) { }

    @Override
    public boolean isPaintable() {
        return false;
    }

    @Override
    public boolean supportsCustomEditor() {
        return false;
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    protected void firePropertyChange(Object oldValue, Object newValue) {
        listeners.firePropertyChange("value", oldValue, newValue);
    }

    @Override
    public Object getValue() {
        return null;
    }

    @Override
    public void setValue(Object value) {
    }

    @Override
    public String getAsText() {
        return null;
    }

    @Override
    public String getJavaInitializationString() {
        return null;
    }

    @Override
    public String[] getTags() {
        return null;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
    }

    @Override
    public void paintValue(Graphics gfx, Rectangle box) {
    }

    public ObjectProperty getObjectProperty() {
        return objectProperty;
    }

    public void setObjectProperty(ObjectProperty objectProperty) { this.objectProperty = objectProperty ;}

    public void setCellEditorAdapter(CellEditorAdapter cellEditorAdapter) {
        this.cellEditorAdapter = cellEditorAdapter;
    }
}
