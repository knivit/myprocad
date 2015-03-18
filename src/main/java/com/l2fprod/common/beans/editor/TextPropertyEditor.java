package com.l2fprod.common.beans.editor;

import com.l2fprod.common.swing.ComponentFactory;
import com.l2fprod.common.swing.PercentLayout;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TextDialog;

import java.awt.Component;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class TextPropertyEditor extends AbstractPropertyEditor {
    private JPanel editorComponent;
    private JTextField text;

    public TextPropertyEditor() {
        editorComponent = new JPanel(new PercentLayout(PercentLayout.HORIZONTAL, 0));

        text = new JTextField();
        editorComponent.add("*", text);

        JButton button = ComponentFactory.Helper.getFactory().createMiniButton();
        editorComponent.add(button);
        button.addActionListener(e -> showTextEditor());
    }

    @Override
    public Component getCustomEditor() {
        return editorComponent;
    }

    @Override
    public Object getValue() {
        return text.getText();
    }

    @Override
    public void setValue(Object value) {
        if (value == null) {
            text.setText("");
        } else {
            text.setText(String.valueOf(value));
        }
    }

    private void showTextEditor() {
        Object oldValue = getValue();
        TextDialog dialog = new TextDialog();
        dialog.setText((String)oldValue, true);
        String title = getObjectProperty().getLabelName();
        if (dialog.displayView(title, DialogButton.OK, DialogButton.CANCEL) == DialogButton.OK) {
            String value = dialog.getText();
            setValue(value);
            firePropertyChange(oldValue, value);
        }
    }
}