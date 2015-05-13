package com.tsoft.myprocad.model;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.propertysheet.CellEditorAdapter;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;

import javax.swing.*;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightTableDialogSupport extends TableDialogSupport {
    private static final String[] names = { L10.get(L10.LIGHT_TYPE), L10.get(L10.LIGHT_COLOR), L10.get(L10.LIGHT_CX), L10.get(L10.LIGHT_CY), L10.get(L10.LIGHT_CZ), L10.get(L10.LIGHT_DX), L10.get(L10.LIGHT_DY), L10.get(L10.LIGHT_DZ) };
    private static final Class[] classes = { LightType.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
    private static final boolean[] editable = { true, true, true, true, true, true, true, true };

    public LightTableDialogSupport() {
        super(names, classes, editable);
    }

    @Override
    public void setupCustomColumns(JTable table) {
        // Light Type
        JComboBox<LightType> comboBox = new JComboBox<>();
        for (LightType lightType : LightType.values()) {
            comboBox.addItem(lightType);
        }
        TableColumn lightTypeColumn = table.getColumnModel().getColumn(0);
        lightTypeColumn.setCellEditor(new DefaultCellEditor(comboBox));

        // Color Editor
        TableColumn colorColumn = table.getColumnModel().getColumn(1);
        CellEditorAdapter colorEditor = new CellEditorAdapter(new ColorPropertyEditor());
        colorColumn.setCellEditor(colorEditor);
        colorColumn.setCellRenderer(new ColorCellRenderer());
    }

    public void setElements(List<Light> lights) {
        for (Light light : lights) {
            addElement(light.getLightType(), light.getColor(),
                    light.getCx(), light.getCy(), light.getCz(),
                    light.getDx(), light.getDy(), light.getDz());
        }
    }

    public List<Light> getElements() {
        List<Light> list = new ArrayList<>();
        for (Object[] data : this) {
            Light light = new Light();
            light.setLightType((LightType)data[0]);
            light.setColor((Color)data[1]);
            light.setCenter((int)data[2], (int)data[3], (int)data[4]);
            light.setDirection((int)data[5], (int)data[6], (int)data[7]);
            list.add(light);
        }
        return list;
    }
}
