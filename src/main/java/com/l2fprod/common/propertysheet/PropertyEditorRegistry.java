package com.l2fprod.common.propertysheet;

import com.l2fprod.common.beans.editor.*;
import com.tsoft.myprocad.swing.properties.SheetProperty;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Mapping between Properties, Property Types and Property Editors.
 */
public class PropertyEditorRegistry {
    private static Map<Class, Class<? extends AbstractPropertyEditor>> typeToEditor = new HashMap<>();

    /**
     * Adds default editors. This method is called by the constructor
     * but may be called later to reset any customizations made through
     * the <code>registerEditor</code> methods. <b>Note: if overriden,
     * <code>super.registerDefaults()</code> must be called before
     * plugging custom defaults. </b>
     */
    static {
        registerEditor(String.class, StringPropertyEditor.class);

        registerEditor(double.class, DoublePropertyEditor.class);
        registerEditor(Double.class, DoublePropertyEditor.class);

        registerEditor(float.class, FloatPropertyEditor.class);
        registerEditor(Float.class, FloatPropertyEditor.class);

        registerEditor(int.class, IntegerPropertyEditor.class);
        registerEditor(Integer.class, IntegerPropertyEditor.class);

        registerEditor(long.class, LongPropertyEditor.class);
        registerEditor(Long.class, LongPropertyEditor.class);

        registerEditor(short.class, ShortPropertyEditor.class);
        registerEditor(Short.class, ShortPropertyEditor.class);

        registerEditor(boolean.class, BooleanAsCheckBoxPropertyEditor.class);
        registerEditor(Boolean.class, BooleanAsCheckBoxPropertyEditor.class);

        registerEditor(File.class, FilePropertyEditor.class);

        registerEditor(List.class, ComboBoxPropertyEditor.class);
        registerEditor(ArrayList.class, ObjectListPropertyEditor.class);

        // awt object editors
        registerEditor(Color.class, ColorPropertyEditor.class);
        registerEditor(Dimension.class, DimensionPropertyEditor.class);
        registerEditor(Insets.class, InsetsPropertyEditor.class);
        registerEditor(Rectangle.class, RectanglePropertyEditor.class);
    }

    /** Returns Editor class or null */
    public static synchronized Class<? extends AbstractPropertyEditor> getEditorClass(Class dataType) {
        return typeToEditor.get(dataType);
    }

    /** Editors are mutable, so DON'T cache their instances */
    public static synchronized AbstractPropertyEditor createEditorInstance(Class<? extends AbstractPropertyEditor> editorClass) {
        try {
            return editorClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static synchronized void registerEditor(Class type, Class<? extends AbstractPropertyEditor> editorClass) {
        Class<? extends AbstractPropertyEditor> value = typeToEditor.get(type);
        if (value != null && !value.equals(editorClass))
            throw new IllegalArgumentException("Class " + type.getName() + " already have editor " + value.getName());

        typeToEditor.put(type, editorClass);
    }
}