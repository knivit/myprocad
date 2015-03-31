package com.tsoft.myprocad.swing.dialog;

import javax.swing.*;

public class InputTableElement<T> extends AbstractInputElement<T, JComponent> {
    private TableDialogPanelSupport<T> values;

    public InputTableElement(String caption, TableDialogPanelSupport<T> values) {
        super(caption);
        this.values = values;
    }

    @Override
    protected JComponent createComponent() {
        JTable table = new JTable();
        table.setModel(values.getTableModel());

        // add custom editors/renderers
        values.setupCustomColumns(table);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);
        return scrollPane;
    }

    @Override
    public T getValue() {
        return null;
    }
}
