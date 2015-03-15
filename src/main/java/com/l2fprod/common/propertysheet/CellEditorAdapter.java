package com.l2fprod.common.propertysheet;

import com.l2fprod.common.beans.editor.AbstractPropertyEditor;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.util.SwingTools;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.EventObject;
import java.util.List;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.TreeCellEditor;

/**
 * Allows to use any PropertyEditor as a Table or Tree cell editor. <br>
 */
public class CellEditorAdapter extends AbstractCellEditor implements TableCellEditor, TreeCellEditor {
    protected AbstractPropertyEditor editor;
    protected int clickCountToStart = 1;

    class CommitEditing implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            ObjectProperty objectProperty = editor.getObjectProperty();
            ObjectProperty.Validator validator = objectProperty.getValueValidator();

            boolean isValid = true;
            if (validator != null) {
                Object value = editor.getValue();
                List selectedItems = objectProperty.getPropertiesController().getSelectedItems();
                for (Object item : selectedItems) {
                    String error = validator.validateValue(item, value);
                    if (error != null) {
                        SwingTools.showError(error);
                        isValid = false;
                        break;
                    }
                }
            }

            if (isValid) stopCellEditing();
            else CellEditorAdapter.this.cancelCellEditing();
        }
    }

    class CancelEditing implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            CellEditorAdapter.this.cancelCellEditing();
        }
    }

    /**
     * Select all text when focus gained, deselect when focus lost.
     */
    class SelectOnFocus implements FocusListener {
        public void focusGained(final FocusEvent e) {
            if (! (e.getSource() instanceof JTextField)) return;

            SwingUtilities.invokeLater(() -> ((JTextField) e.getSource()).selectAll());
        }

        public void focusLost(final FocusEvent e) {
            if (! (e.getSource() instanceof JTextField)) return;

            SwingUtilities.invokeLater(() -> ((JTextField) e.getSource()).select(0, 0));
        }
    }

    public CellEditorAdapter(AbstractPropertyEditor editor) {
        this.editor = editor;
        editor.setCellEditorAdapter(this);

        Component component = editor.getCustomEditor();
        if (component instanceof JTextField) {
            JTextField field = (JTextField)component;
            field.addFocusListener(new SelectOnFocus());
            field.addActionListener(new CommitEditing());
            field.registerKeyboardAction(new CancelEditing(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_FOCUSED);
        }

        // when the editor notifies a change, commit the changes
        editor.addPropertyChangeListener(evt -> stopCellEditing());
    }

    @Override
    public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row) {
        return getEditor(value);
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value, boolean selected, int row, int column) {
        return getEditor(value);
    }

    @Override
    public Object getCellEditorValue() {
        return editor.getValue();
    }

    @Override
    public boolean isCellEditable(EventObject event) {
        if (event instanceof MouseEvent) {
            return ((MouseEvent)event).getClickCount() >= clickCountToStart;
        }
        return true;
    }

    @Override
    public boolean shouldSelectCell(EventObject event) {
        return true;
    }

    @Override
    public boolean stopCellEditing() {
        fireEditingStopped();
        return true;
    }

    @Override
    public void cancelCellEditing() {
        fireEditingCanceled();
    }

    private Component getEditor(Object value) {
        editor.setValue(value);
        final Component cellEditor = editor.getCustomEditor();

        // request focus later so the editor can be used to enter value as soon as made visible
        SwingUtilities.invokeLater(() -> cellEditor.requestFocus());
        return cellEditor;
    }
}
