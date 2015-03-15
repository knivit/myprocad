package com.l2fprod.common.beans.editor;

import com.l2fprod.common.util.converter.ConverterRegistry;

import java.awt.Dimension;

/**
 * DimensionPropertyEditor. <br>Editor for java.awt.Dimension object, where
 * the dimension is specified as "width x height"
 */
public class DimensionPropertyEditor extends StringConverterPropertyEditor {
    @Override
    protected Object convertFromString(String text) {
        return ConverterRegistry.instance().convert(Dimension.class, text);
    }
}
