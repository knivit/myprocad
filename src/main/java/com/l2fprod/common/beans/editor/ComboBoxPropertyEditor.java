package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.tsoft.myprocad.model.property.ObjectProperty;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.event.*;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

public class ComboBoxPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;
    protected JComboBox combobox;
    private Object oldValue;
    private Icon[] icons;

    private ItemListener itemListener = e -> {
        switch (e.getStateChange()) {
            case ItemEvent.DESELECTED: {
                oldValue = e.getItem();
                return;
            }

            case ItemEvent.SELECTED: {
                // invoke separately to prevent hanging under debugger
                EventQueue.invokeLater(() -> ComboBoxPropertyEditor.this.firePropertyChange(oldValue, e.getItem()));
                return;
            }
        }
    };

    public ComboBoxPropertyEditor() {
        JPanel panel = new JPanel(new BorderLayout());
        editorComponent = panel;

        combobox = new JComboBox();
        combobox.setMaximumRowCount(25);
        combobox.setRenderer(new Renderer());

        combobox.addItemListener(itemListener);
        combobox.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    ComboBoxPropertyEditor.this.firePropertyChange(oldValue, combobox.getSelectedItem());
                }
            }
        });

        panel.add(combobox, BorderLayout.CENTER);
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public void setObjectProperty(ObjectProperty objectProperty) {
        super.setObjectProperty(objectProperty);
        setAvailableValues(objectProperty.getAvailableValues());
    }

    @Override
    public void addButton(ActionListener listener) {
        JButton button = ComponentFactory.Helper.getFactory().createMiniButton();
        editorComponent.add(button, BorderLayout.EAST);
        button.addActionListener(listener);

        // stop editing so after the dialog done, the editor will be recreated with new available values
        button.addActionListener(l -> cellEditorAdapter.stopCellEditing());
    }

    @Override
    public Object getValue() {
        return combobox.getSelectedItem();
    }

    @Override
    public void setValue(Object value) {
        Object current;
        int index = -1;
        for (int i = 0, c = combobox.getModel().getSize(); i < c; i++) {
            current = combobox.getModel().getElementAt(i);
            if (value == current || (current != null && current.equals(value))) {
                index = i;
                break;
            }
        }

        try {
            combobox.removeItemListener(itemListener);
            combobox.setSelectedIndex(index);
        } finally {
            combobox.addItemListener(itemListener);
        }
    }

    protected void setAvailableValues(Object[] values) {
        combobox.setModel(new DefaultComboBoxModel(values));
    }

    public void setAvailableIcons(Icon[] icons) {
        this.icons = icons;
    }

    public class Renderer extends DefaultListCellRenderer {
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            Component component = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

            if (icons != null && index >= 0 && component instanceof JLabel)
                ((JLabel)component).setIcon(icons[index]);
            return component;
        }
    }
}
