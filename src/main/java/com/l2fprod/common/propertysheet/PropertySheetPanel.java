package com.l2fprod.common.propertysheet;

import com.l2fprod.common.swing.IconPool;
import com.l2fprod.common.swing.LookAndFeelTweaks;
import com.l2fprod.common.util.ResourceManager;
import com.tsoft.myprocad.swing.properties.SheetProperty;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * An implementation of a PropertySheet which shows a table to
 * edit/view values, a description pane which updates when the
 * selection changes and buttons to toggle between a flat view and a
 * by-category view of the properties. A button in the toolbar allows
 * to sort the properties and categories by name.
 * <p>
 * Default sorting is by name (case-insensitive). Custom sorting can
 * be implemented through
 * {@link com.l2fprod.common.propertysheet.PropertySheetTableModel#setCategorySortingComparator(Comparator)}
 * and
 * {@link com.l2fprod.common.propertysheet.PropertySheetTableModel#setPropertySortingComparator(Comparator)}
 */
public class PropertySheetPanel extends JPanel implements PropertySheet, PropertyChangeListener {
    private PropertySheetTable table;
    private PropertySheetTableModel model;
    private JScrollPane tableScroll;
    private ListSelectionListener selectionListener = new SelectionListener();

    private JPanel actionPanel;
    private JToggleButton sortButton;
    private JToggleButton asCategoryButton;
    private JToggleButton descriptionButton;

    private JSplitPane split;
    private int lastDescriptionHeight;

    private JEditorPane descriptionPanel;
    private JScrollPane descriptionScrollPane;

    public PropertySheetPanel() {
        this(new PropertySheetTable());
    }

    public PropertySheetPanel(PropertySheetTable table) {
        buildUI();
        setTable(table);
    }

    /**
     * Sets the table used by this panel.
     *
     * Note: listeners previously added with
     * {@link PropertySheetPanel#addPropertySheetChangeListener(PropertyChangeListener)}
     * must be re-added after this call if the table model is not the
     * same as the previous table.
     *
     * @param table
     */
    public void setTable(PropertySheetTable table) {
        if (table == null) {
            throw new IllegalArgumentException("table must not be null");
        }

        // remove the property change listener from any previous model
        if (model != null)
            model.removePropertyChangeListener(this);

        // get the model from the table
        model = (PropertySheetTableModel)table.getModel();
        model.addPropertyChangeListener(this);

        // remove the listener from the old table
        if (this.table != null)
            this.table.getSelectionModel().removeListSelectionListener(selectionListener);

        // prepare the new table
        table.getSelectionModel().addListSelectionListener(selectionListener);
        tableScroll.getViewport().setView(table);

        // use the new table as our table
        this.table = table;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        repaint();
    }

    @Override
    public PropertySheetTable getTable() {
        return table;
    }

    public void setDescriptionVisible(boolean visible) {
        if (visible) {
            add("Center", split);
            split.setTopComponent(tableScroll);
            split.setBottomComponent(descriptionScrollPane);
            // restore the divider location
            split.setDividerLocation(split.getHeight() - lastDescriptionHeight);
        } else {
            // save the size of the description pane to restore it later
            lastDescriptionHeight = split.getHeight() - split.getDividerLocation();
            remove(split);
            add("Center", tableScroll);
        }
        descriptionButton.setSelected(visible);
        PropertySheetPanel.this.revalidate();
    }

    public void setToolBarVisible(boolean visible) {
        actionPanel.setVisible(visible);
        PropertySheetPanel.this.revalidate();
    }

    public void setMode(int mode) {
        model.setMode(mode);
        asCategoryButton.setSelected(PropertySheet.VIEW_AS_CATEGORIES == mode);
    }

    @Override
    public void setProperties(List<SheetProperty> properties) {
        model.setProperties(properties);
    }

    @Override
    public List<SheetProperty> getProperties() {
        return model.getProperties();
    }

    @Override
    public void addProperty(SheetProperty property) {
        model.addProperty(property);
    }

    @Override
    public void addProperty(int index, SheetProperty property) {
        model.addProperty(index, property);
    }

    public void addProperties(List<SheetProperty> properties) {
        if (properties != null) {
            for (SheetProperty property : properties) {
                addProperty(property);
            }
        }
    }

    @Override
    public void removeProperty(SheetProperty property) {
        model.removeProperty(property);
    }

    @Override
    public int getPropertyCount() {
        return model.getPropertyCount();
    }

    @Override
    public Iterator propertyIterator() {
        return model.propertyIterator();
    }

    public void addPropertySheetChangeListener(PropertyChangeListener listener) {
        model.addPropertyChangeListener(listener);
    }

    public void removePropertySheetChangeListener(PropertyChangeListener listener) {
        model.removePropertyChangeListener(listener);
    }

    /**
     * Sets sorting of categories enabled or disabled.
     *
     * @param value true to enable sorting
     */
    public void setSortingCategories(boolean value) {
        model.setSortingCategories(value);
        sortButton.setSelected(isSorting());
    }

    /**
     * Is sorting of categories enabled.
     *
     * @return true if category sorting is enabled
     */
    public boolean isSortingCategories() {
        return model.isSortingCategories();
    }

    /**
     * Sets sorting of properties enabled or disabled.
     *
     * @param value true to enable sorting
     */
    public void setSortingProperties(boolean value) {
        model.setSortingProperties(value);
        sortButton.setSelected(isSorting());
    }

    /**
     * Is sorting of properties enabled.
     *
     * @return true if property sorting is enabled
     */
    public boolean isSortingProperties() {
        return model.isSortingProperties();
    }

    /**
     * Sets sorting properties and categories enabled or disabled.
     *
     * @param value true to enable sorting
     */
    public void setSorting(boolean value) {
        model.setSortingCategories(value);
        model.setSortingProperties(value);
        sortButton.setSelected(value);
    }

    /**
     * @return true if properties or categories are sorted.
     */
    public boolean isSorting() {
        return model.isSortingCategories() || model.isSortingProperties();
    }

    /**
     * Sets the Comparator to be used with categories. Categories are
     * treated as String-objects.
     *
     * @param comp java.util.Comparator used to compare categories
     */
    public void setCategorySortingComparator(Comparator comp) {
        model.setCategorySortingComparator(comp);
    }

    /**
     * Sets the Comparator to be used with Property-objects.
     *
     * @param comp java.util.Comparator used to compare Property-objects
     */
    public void setPropertySortingComparator(Comparator comp) {
        model.setPropertySortingComparator(comp);
    }

    /**
     * Set wether or not toggle states are restored when new properties are
     * applied.
     *
     * @param value true to enable
     */
    public void setRestoreToggleStates(boolean value) {
        model.setRestoreToggleStates(value);
    }

    /**
     * @return true is toggle state restore is enabled
     */
    public boolean isRestoreToggleStates() {
        return model.isRestoreToggleStates();
    }

    /**
     * @return the category view toggle states.
     */
    public Map getToggleStates() {
        return model.getToggleStates();
    }

    /**
     * Sets the toggle states for the category views. Note this <b>MUST</b> be
     * called <b>BEFORE</b> setting any properties.
     * @param toggleStates the toggle states as returned by getToggleStates
     */
    public void setToggleStates(Map toggleStates) {
        model.setToggleStates(toggleStates);
    }

    private void buildUI() {
        LookAndFeelTweaks.setBorderLayout(this);
        LookAndFeelTweaks.setBorder(this);

        actionPanel = new JPanel(new FlowLayout(FlowLayout.LEADING, 2, 0));
        actionPanel.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
        actionPanel.setOpaque(false);
        add("North", actionPanel);

        sortButton = new JToggleButton(new ToggleSortingAction());
        sortButton.setText(null);
        sortButton.setOpaque(false);
        actionPanel.add(sortButton);

        asCategoryButton = new JToggleButton(new ToggleModeAction());
        asCategoryButton.setText(null);
        asCategoryButton.setOpaque(false);
        actionPanel.add(asCategoryButton);

        descriptionButton = new JToggleButton(new ToggleDescriptionAction());
        descriptionButton.setText(null);
        descriptionButton.setOpaque(false);
        actionPanel.add(descriptionButton);

        split = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        split.setBorder(null);
        split.setResizeWeight(1.0);
        split.setContinuousLayout(true);
        add("Center", split);

        tableScroll = new JScrollPane();
        tableScroll.setBorder(BorderFactory.createEmptyBorder());
        split.setTopComponent(tableScroll);

        descriptionPanel = new JEditorPane("text/html", "<html>");
        descriptionPanel.setBorder(BorderFactory.createEmptyBorder());
        descriptionPanel.setEditable(false);
        descriptionPanel.setBackground(UIManager.getColor("Panel.background"));
        LookAndFeelTweaks.htmlize(descriptionPanel);

        selectionListener = new SelectionListener();

        descriptionScrollPane = new JScrollPane(descriptionPanel);
        descriptionScrollPane.setBorder(LookAndFeelTweaks.addMargin(BorderFactory
                .createLineBorder(UIManager.getColor("controlDkShadow"))));
        descriptionScrollPane.getViewport().setBackground(
                descriptionPanel.getBackground());
        descriptionScrollPane.setMinimumSize(new Dimension(50, 50));
        split.setBottomComponent(descriptionScrollPane);

        // by default description is not visible, toolbar is visible.
        setDescriptionVisible(false);
        setToolBarVisible(true);
    }

    class SelectionListener implements ListSelectionListener {
        @Override
        public void valueChanged(ListSelectionEvent e) {
            int row = table.getSelectedRow();
            SheetProperty prop = null;
            if (row >= 0 && table.getRowCount() > row)
                prop = model.getPropertySheetElement(row).getProperty();
            if (prop != null) {
                descriptionPanel.setText("<html>"
                        + "<b>" + (prop.getDisplayName() == null ? "" :prop.getDisplayName())
                        + "</b><br>");
            } else {
                descriptionPanel.setText("<html>");
            }

            //position it at the top
            descriptionPanel.setCaretPosition(0);
        }
    }

    class ToggleModeAction extends AbstractAction {

        public ToggleModeAction() {
            super("toggle", IconPool.shared().get(
                    PropertySheet.class.getResource("icons/category.gif")));
            putValue(Action.SHORT_DESCRIPTION, ResourceManager.get(
                    PropertySheet.class).getString(
                    "PropertySheetPanel.category.shortDescription"));
        }

        public void actionPerformed(ActionEvent e) {
            if (asCategoryButton.isSelected()) {
                model.setMode(PropertySheet.VIEW_AS_CATEGORIES);
            } else {
                model.setMode(PropertySheet.VIEW_AS_FLAT_LIST);
            }
        }
    }

    class ToggleDescriptionAction extends AbstractAction {

        public ToggleDescriptionAction() {
            super("toggleDescription", IconPool.shared().get(
                    PropertySheet.class.getResource("icons/description.gif")));
            putValue(Action.SHORT_DESCRIPTION, ResourceManager.get(
                    PropertySheet.class).getString(
                    "PropertySheetPanel.description.shortDescription"));
        }

        public void actionPerformed(ActionEvent e) {
            setDescriptionVisible(descriptionButton.isSelected());
        }
    }

    class ToggleSortingAction extends AbstractAction {

        public ToggleSortingAction() {
            super("toggleSorting", IconPool.shared().get(
                    PropertySheet.class.getResource("icons/sort.gif")));
            putValue(Action.SHORT_DESCRIPTION, ResourceManager.get(
                    PropertySheet.class).getString(
                    "PropertySheetPanel.sort.shortDescription"));
        }

        public void actionPerformed(ActionEvent e) {
            setSorting(sortButton.isSelected());
        }
    }

}