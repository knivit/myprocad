package com.tsoft.myprocad.model.property;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.tsoft.myprocad.swing.dialog.AbstractDialogPanel;
import com.tsoft.myprocad.viewcontroller.property.AbstractPropertiesController;

import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ObjectProperty {
    private AbstractPropertiesController propertiesController;

    private final String uuid = UUID.randomUUID().toString();
    private String categoryName;
    private String labelName;
    private Class rendererType = String.class;
    private Class editorType = String.class;

    private Object[] availableValues;
    private AbstractDialogPanel dialogPanel;
    private boolean calculable; // true if the property should be re-calculated (it's getter invoked) on every change of other properties
    private boolean tooComplexForBatchOpertion; // usually true for lists as they consume too much reources in their getter

    private List<ActionListener> editorButtons = new ArrayList<>();

    public interface Getter { public Object getValue(Object object); }

    public interface Setter { public void setValue(Object object, Object value); }

    /** Return error message or null */
    public interface Validator { public String validateValue(Object object, Object value); }

    private Getter getter;
    private Setter setter;
    private Validator validator;

    public ObjectProperty(AbstractPropertiesController propertiesController) {
        this.propertiesController = propertiesController;

        propertiesController.addObjectProperty(this);
    }

    public String getCategoryName() {
        return categoryName;
    }

    public ObjectProperty setCategoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public String getLabelName() {
        return labelName;
    }

    public ObjectProperty setLabelName(String labelName) {
        this.labelName = labelName;
        return this;
    }

    public boolean isEditable() {
        return setter != null;
    }

    public Getter getValueGetter() {
        return getter;
    }

    public ObjectProperty setValueGetter(Getter getter) {
        this.getter = getter;
        return this;
    }

    public Setter getValueSetter() {
        return setter;
    }

    public ObjectProperty setValueSetter(Setter setter) {
        this.setter = setter;
        return this;
    }

    public Validator getValueValidator() {
        return validator;
    }

    public ObjectProperty setValueValidator(Validator validator) {
        this.validator = validator;
        return this;
    }

    public Class getRendererType() {
        return rendererType;
    }

    public Class getEditorType() {
        return editorType;
    }

    /** Defines both Renderer and Editor types */
    public ObjectProperty setType(Class type) {
        this.editorType = type;

        // Renders are only for standard data types
        if (!type.isInstance(AbstractPropertyEditor.class)) this.rendererType = type;
        return this;
    }

    public Object[] getAvailableValues() {
        return availableValues;
    }

    public ObjectProperty setAvailableValues(Object ... availableValues) {
        this.availableValues = availableValues;
        return this;
    }

    public ObjectProperty addEditorButton(ActionListener listener) {
        editorButtons.add(listener);
        return this;
    }

    public List<ActionListener> getEditorButtons() { return editorButtons ;}

    public boolean isCalculable() {
        return calculable;
    }

    public ObjectProperty setCalculable(boolean calculable) {
        this.calculable = calculable;
        return this;
    }

    public boolean isTooComplexForBatchOpertion() {
        return tooComplexForBatchOpertion;
    }

    public ObjectProperty setTooComplexForSelectionOperation(boolean tooComplexForBatchOpertion) {
        this.tooComplexForBatchOpertion = tooComplexForBatchOpertion;
        return this;
    }

    public AbstractDialogPanel getDialogPanel() { return dialogPanel; }

    public ObjectProperty setDialogPanel(AbstractDialogPanel dialogPanel) {
        this.dialogPanel = dialogPanel;
        return this;
    }

    public AbstractPropertiesController getPropertiesController() {
        return propertiesController;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ObjectProperty that = (ObjectProperty) o;

        if (!uuid.equals(that.uuid)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public String toString() {
        return "ObjectProperty{" +
                "categoryName='" + categoryName + '\'' +
                ", labelName='" + labelName + '\'' +
                '}';
    }
}
