/**
 * L2FProd.com Common Components 7.3 License.
 *
 * Copyright 2005-2007 L2FProd.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.l2fprod.common.propertysheet;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.swing.renderer.BooleanCellRenderer;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;
import com.l2fprod.common.swing.renderer.DateRenderer;
import com.l2fprod.common.swing.renderer.DefaultCellRenderer;
import com.l2fprod.common.beans.ExtendedPropertyDescriptor;
import com.tsoft.myprocad.swing.properties.PatternComboBoxPropertyEditor;
import com.tsoft.myprocad.swing.properties.PatternComboBoxRenderer;
import com.tsoft.myprocad.swing.properties.SheetProperty;

import java.awt.Color;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.swing.table.TableCellRenderer;

/**
 * Mapping between Properties, Property Types and Renderers.
 */
public class PropertyRendererRegistry {
    private static Map<Class, TableCellRenderer> typeToRenderer = new HashMap<>();
    private static DefaultCellRenderer defaultRenderer;

    /**
     * Adds default renderers. This method is called by the constructor
     * but may be called later to reset any customizations made through
     * the <code>registerRenderer</code> methods. <b>Note: if overriden,
     * <code>super.registerDefaults()</code> must be called before
     * plugging custom defaults. </b>
     */
    static {
        // use the default renderer for Object and all primitives
        defaultRenderer = new DefaultCellRenderer();
        defaultRenderer.setShowOddAndEvenRows(false);

        ColorCellRenderer colorRenderer = new ColorCellRenderer();
        colorRenderer.setShowOddAndEvenRows(false);

        BooleanCellRenderer booleanRenderer = new BooleanCellRenderer();

        DateRenderer dateRenderer = new DateRenderer();
        dateRenderer.setShowOddAndEvenRows(false);

        registerRenderer(Object.class, defaultRenderer);
        registerRenderer(Color.class, colorRenderer);
        registerRenderer(ColorPropertyEditor.class, colorRenderer);
        registerRenderer(boolean.class, booleanRenderer);
        registerRenderer(Boolean.class, booleanRenderer);
        registerRenderer(byte.class, defaultRenderer);
        registerRenderer(Byte.class, defaultRenderer);
        registerRenderer(char.class, defaultRenderer);
        registerRenderer(Character.class, defaultRenderer);
        registerRenderer(double.class, defaultRenderer);
        registerRenderer(Double.class, defaultRenderer);
        registerRenderer(float.class, defaultRenderer);
        registerRenderer(Float.class, defaultRenderer);
        registerRenderer(int.class, defaultRenderer);
        registerRenderer(Integer.class, defaultRenderer);
        registerRenderer(long.class, defaultRenderer);
        registerRenderer(Long.class, defaultRenderer);
        registerRenderer(short.class, defaultRenderer);
        registerRenderer(Short.class, defaultRenderer);
        registerRenderer(Date.class, dateRenderer);

        registerRenderer(PatternComboBoxPropertyEditor.class, new PatternComboBoxRenderer());
    }

    public static synchronized TableCellRenderer getRenderer(Class type) {
        TableCellRenderer renderer = typeToRenderer.get(type);
        if (renderer == null) return defaultRenderer;
        return renderer;
    }

    private static synchronized void registerRenderer(Class type, TableCellRenderer renderer) {
        typeToRenderer.put(type, renderer);
    }
}
