package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;
import com.l2fprod.common.util.ResourceManager;

import java.awt.Color;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

public class ColorPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;
    private ColorCellRenderer label;
    private JButton button;
    private Color color;

    public ColorPropertyEditor() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));
        editorComponent.add("*", label = new ColorCellRenderer());
        label.setOpaque(false);

        editorComponent.add(button = ComponentFactory.Helper.getFactory().createMiniButton());
        button.addActionListener(e -> selectColor());
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public Object getValue() {
        return color;
    }

    @Override
    public void setValue(Object value) {
        color = (Color)value;
        label.setValue(color);
    }

    protected void selectColor() {
        ResourceManager rm = ResourceManager.all(FilePropertyEditor.class);
        String title = rm.getString("ColorPropertyEditor.title");
        Color selectedColor = JColorChooser.showDialog(editorComponent, title, color);

        if (selectedColor != null) {
            Color oldColor = color;
            Color newColor = selectedColor;
            setValue(newColor);
            firePropertyChange(oldColor, newColor);
        }
    }
}
