package com.tsoft.myprocad.swing.properties;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.l2fprod.common.propertysheet.PropertyEditorRegistry;
import com.l2fprod.common.propertysheet.PropertyRendererRegistry;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;

import javax.swing.table.TableCellRenderer;
import java.awt.EventQueue;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class SheetProperty implements PropertyChangeListener {
    private List<SheetProperty> subProperties = new ArrayList<>();

    private Object value;

    private transient PropertyChangeSupport listeners = new PropertyChangeSupport(this);

    private AbstractPropertiesController propertiesController;
    private ObjectProperty objectProperty;

    public SheetProperty(AbstractPropertiesController propertiesController, ObjectProperty objectProperty) {
        super();

        this.propertiesController = propertiesController;
        this.objectProperty = objectProperty;

        refreshValue();

        addPropertyChangeListener(this);
    }

    public Object getValue() {
        return value;
    }

    public TableCellRenderer getRenderer() {
        return PropertyRendererRegistry.getRenderer(objectProperty.getRendererType());
    }

    public AbstractPropertyEditor getEditor() {
        Class dataType = objectProperty.getEditorType();

        // see is this is a standard data-type related editor
        Class<? extends AbstractPropertyEditor> editorClass = PropertyEditorRegistry.getEditorClass(dataType);

        // if not, then use Editor's class directly
        if (editorClass == null) editorClass = dataType;

        AbstractPropertyEditor editor = PropertyEditorRegistry.createEditorInstance(editorClass);
        if (editor == null) throw new IllegalStateException("Unknown editor class='" + editorClass.getName() + "' for " + objectProperty.toString());

        editor.setObjectProperty(objectProperty);
        objectProperty.getEditorButtons().forEach(editor::addButton);
        return editor;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);

        List<SheetProperty> subProperties = getSubProperties();
        for (SheetProperty prop : subProperties)
            prop.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);

        List<SheetProperty> subProperties = getSubProperties();
        for (SheetProperty prop : subProperties) {
            prop.removePropertyChangeListener(listener);
        }
    }

    protected void firePropertyChange(Object oldValue, Object newValue) {
        listeners.firePropertyChange("value", oldValue, newValue);
    }

    public void refreshValue() {
        Object value = propertiesController.getPropertyValue(objectProperty);
        setValue(value);

     //   if (objectProperty.getEditor() != null) {
     //       objectProperty.getEditor().setValue(value);
     //   }
    }

    // Change actual object property's value when user changes it through PropertyEditor
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        final Object newValue = evt.getNewValue();

        // invoke separately to prevent hanging under debugger
        EventQueue.invokeLater(() -> {
            // set the value and this triggers propertyChange event
            // and it will refresh all PropertyEditor's properties
            // (this is needed for calculable properties)
            Object value1 = newValue;
            propertiesController.setPropertyValue(objectProperty, value1);

            // apply the changes
            propertiesController.applyChanges();
        });
    }

    public String getName() {
        return objectProperty.getLabelName();
    }

    public String getDisplayName() {
        return objectProperty.getLabelName();
    }

    public boolean isEditable() {
        return objectProperty.isEditable();
    }

    public String getCategory() {
        return objectProperty.getCategoryName();
    }

    public void setValue( Object value ) {
        Object oldValue = this.value;
        this.value = value;
        if (!Objects.equals(value, oldValue)) {
            firePropertyChange(oldValue, getValue());
        }
    }

    public List<SheetProperty> getSubProperties() {
        return subProperties;
    }

    @Override
    public String toString() {
        return objectProperty.toString();
    }

    @Override
    public int hashCode() {
        return objectProperty.hashCode();
    }

    /**
     * Compares two DefaultProperty objects. Two DefaultProperty objects are equal
     * if they are the same object or if their name, display name, short
     * description, category, type and editable property are the same. Note the
     * property value is not considered in the implementation.
     */
    @Override
    public boolean equals(Object other) {
        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        if (other == this) {
            return true;
        }

        SheetProperty dp = (SheetProperty) other;
        return objectProperty.equals(dp.objectProperty);
    }

    @Override
    public Object clone() {
        SheetProperty clone;
        try {
            clone = (SheetProperty)super.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Reads the value of this Property from the given object. It uses reflection
     * and looks for a method starting with "is" or "get" followed by the
     * capitalized Property name.
     *
    public void readFromObject(Object object) {
        try {
            Method method = BeanUtils.getReadMethod(object.getClass(), getName());
            if (method != null) {
                Object value = method.invoke(object, null);
                initializeValue(value); // avoid updating parent or firing property change

                if (value != null) {
                    for (SheetProperty subProperty : subProperties) {
                        subProperty.readFromObject(value);
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();
        listeners = new PropertyChangeSupport(this);
    }

    /**
     * Writes the value of the Property to the given object. It uses reflection
     * and looks for a method starting with "set" followed by the capitalized
     * Property name and with one parameter with the same type as the Property.
     *
    public void writeToObject(Object object) {
        try {
            Method method = BeanUtils.getWriteMethod(object.getClass(), getName(), getType());
            if (method != null) {
                method.invoke(object, new Object[] { getValue()});
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    } */
}
