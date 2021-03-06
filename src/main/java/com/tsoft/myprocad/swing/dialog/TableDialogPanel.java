package com.tsoft.myprocad.swing.dialog;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.menu.MenuAction;
import com.tsoft.myprocad.util.SwingTools;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/** A panel with a table and controls to add/delete items */
public class TableDialogPanel extends AbstractDialogPanel {
    public TableDialogPanel(Object entity, TableDialogPanelSupport values, ObjectProperty.Validator validator) {
        super(new BorderLayout());

        JTable table = new JTable();
        table.setModel(values.getTableModel());

        // add custom editors/renderers
        values.setupCustomColumns(table);

        JScrollPane scrollPane = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        JPanel toolbar = new JPanel(new GridLayout(0, 1));
        toolbar.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));

        JButton add = new JButton(L10.get(L10.ADD_BUTTON));
        add.addActionListener(e -> {
            Object obj = values.addDialog();
            if (obj == null) return;

            values.getTableModel().fireTableDataChanged();

            // do not use values.indexOf() as all of the added element's fields can be null/empty
            // so its equals() won't work
            for (int index = 0; index < values.size(); index ++) {
                if (values.get(index) == obj) {
                    table.setRowSelectionInterval(index, index);
                    break;
                }
            }
        });
        toolbar.add(add);

        JButton delete = new JButton(L10.get(L10.DELETE_BUTTON));
        delete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow == -1) return;

            if (values.deleteDialog(selectedRow)) values.getTableModel().fireTableDataChanged();
        });
        toolbar.add(delete);

        // custom buttons
        List<MenuAction> customButtons = values.getCustomButtons();
        if (customButtons != null) {
            for (MenuAction menuAction : customButtons) {
                JButton button = menuAction.getMenu().createMenuButton();
                button.addActionListener(menuAction);
                button.addActionListener(e -> values.getTableModel().fireTableDataChanged());
                toolbar.add(button);
            }
        }

        toolbar.add(Box.createVerticalGlue());
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        toolBarPanel.add(toolbar, BorderLayout.NORTH);

        add(BorderLayout.CENTER, scrollPane);
        add(BorderLayout.EAST, toolBarPanel);

        if (validator != null) {
            setBeforeCloseValidator(() -> {
                String error = validator.validateValue(entity, values);
                if (error != null) {
                    SwingTools.showError(error);
                    return false;
                }
                return true;
            });
        }
    }

    @Override
    public Dimension getDialogPreferredSize() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new Dimension((int)Math.round(screenSize.width * 0.6), (int)Math.round(screenSize.height * 0.6));
    }
}
