package com.l2fprod.common.propertysheet;

import com.l2fprod.common.swing.ObjectTableModel;
import com.tsoft.myprocad.swing.properties.SheetProperty;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * PropertySheetTableModel. <br>
 *
 */
public class PropertySheetTableModel extends AbstractTableModel implements PropertyChangeListener, ObjectTableModel {
    public static final int NAME_COLUMN = 0;
    public static final int VALUE_COLUMN = 1;
    public static final int NUM_COLUMNS = 2;

    private PropertyChangeSupport listeners = new PropertyChangeSupport(this);
    private List model;
    private List publishedModel;
    private List<SheetProperty> properties;
    private int mode;
    private boolean sortingCategories;
    private boolean sortingProperties;
    private boolean restoreToggleStates;
    private Comparator categorySortingComparator;
    private Comparator propertySortingComparator;
    private Map toggleStates;

    public PropertySheetTableModel() {
        model = new ArrayList();
        publishedModel = new ArrayList();
        properties = new ArrayList<>();
        mode = PropertySheet.VIEW_AS_FLAT_LIST;
        sortingCategories = false;
        sortingProperties = false;
        restoreToggleStates = false;
        toggleStates=new HashMap();
    }

    public void setProperties(List<SheetProperty> newProperties) {
        // unregister the listeners from previous properties
        for (SheetProperty prop : properties) {
            prop.removePropertyChangeListener(this);
        }

        // replace the current properties
        properties.clear();
        properties.addAll(newProperties);

        // add listeners
        for (SheetProperty prop : properties) {
            prop.addPropertyChangeListener(this);
        }

        buildModel();
    }

    public List<SheetProperty> getProperties() {
        return properties;
    }

    public void addProperty(SheetProperty property) {
        properties.add(property);
        property.addPropertyChangeListener(this);
        buildModel();
    }

    public void addProperty(int index, SheetProperty property) {
        properties.add(index, property);
        property.addPropertyChangeListener(this);
        buildModel();
    }

    public void removeProperty(SheetProperty property) {
        properties.remove(property);
        property.removePropertyChangeListener(this);
        buildModel();
    }

    public int getPropertyCount() {
        return properties.size();
    }

    public Iterator propertyIterator() {
        return properties.iterator();
    }

    /**
     * Set the current mode, either {@link PropertySheet#VIEW_AS_CATEGORIES}
     * or {@link PropertySheet#VIEW_AS_FLAT_LIST}.
     */
    public void setMode(int mode) {
        if (this.mode == mode) {
            return;
        }
        this.mode = mode;
        buildModel();
    }

    /**
     * Get the current mode, either {@link PropertySheet#VIEW_AS_CATEGORIES}
     * or {@link PropertySheet#VIEW_AS_FLAT_LIST}.
     */
    public int getMode() {
        return mode;
    }

    public Class getColumnClass(int columnIndex) {
        return super.getColumnClass(columnIndex);
    }

    public int getColumnCount() {
        return NUM_COLUMNS;
    }

    public int getRowCount() {
        return publishedModel.size();
    }

    public Object getObject(int rowIndex) {
        return getPropertySheetElement(rowIndex);
    }

    /**
     * Get the current property sheet element, of type {@link Item}, at
     * the specified row.
     */
    public Item getPropertySheetElement(int rowIndex) {
        return (Item) publishedModel.get(rowIndex);
    }

    /**
     * Get whether this model is currently sorting categories.
     */
    public boolean isSortingCategories() {
        return sortingCategories;
    }

    /**
     * Set whether this model is currently sorting categories.
     * If this changes the sorting, the model will be rebuilt.
     */
    public void setSortingCategories(boolean value) {
        boolean old = sortingCategories;
        sortingCategories = value;
        if (sortingCategories != old)
            buildModel();
    }

    /**
     * Get whether this model is currently sorting properties.
     */
    public boolean isSortingProperties() {
        return sortingProperties;
    }

    /**
     * Set whether this model is currently sorting properties.
     * If this changes the sorting, the model will be rebuilt.
     */
    public void setSortingProperties(boolean value) {
        boolean old = sortingProperties;
        sortingProperties = value;
        if (sortingProperties != old)
            buildModel();
    }

    /**
     * Set the comparator used for sorting categories.  If this
     * changes the comparator, the model will be rebuilt.
     */
    public void setCategorySortingComparator(Comparator comp) {
        Comparator old = categorySortingComparator;
        categorySortingComparator = comp;
        if (categorySortingComparator != old)
            buildModel();
    }

    /**
     * Set the comparator used for sorting properties.  If this
     * changes the comparator, the model will be rebuilt.
     */
    public void setPropertySortingComparator(Comparator comp) {
        Comparator old = propertySortingComparator;
        propertySortingComparator = comp;
        if (propertySortingComparator != old)
            buildModel();
    }

    /**
     * Set whether or not this model will restore the toggle states when new
     * properties are applied.
     */
    public void setRestoreToggleStates(boolean value) {
        restoreToggleStates = value;
        if (!restoreToggleStates) {
            toggleStates.clear();
        }
    }

    /**
     * Get whether this model is restoring toggle states
     */
    public boolean isRestoreToggleStates() {
        return restoreToggleStates;
    }

    /**
     * @return the category view toggle states.
     */
    public Map getToggleStates() {
        // Call visibilityChanged to populate the toggleStates map
        visibilityChanged(restoreToggleStates);
        return toggleStates;
    }

    /**
     * Sets the toggle states for the category views. Note this <b>MUST</b> be
     * called <b>BEFORE</b> setting any properties.
     * @param toggleStates the toggle states as returned by getToggleStates
     */
    public void setToggleStates(Map toggleStates) {
        // We are providing a toggleStates map - so by definition we must want to
        // store the toggle states
        setRestoreToggleStates(true);
        this.toggleStates.clear();
        this.toggleStates.putAll(toggleStates);
    }

    /**
     * Retrieve the value at the specified row and column location.
     * When the row contains a category or the column is
     * {@link #NAME_COLUMN}, an {@link Item} object will be returned.
     * If the row is a property and the column is {@link #VALUE_COLUMN},
     * the value of the property will be returned.
     *
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int rowIndex, int columnIndex) {
        Object result = null;
        Item item = getPropertySheetElement(rowIndex);

        if (item.isProperty()) {
            switch (columnIndex) {
                case NAME_COLUMN:
                    result = item;
                    break;

                case VALUE_COLUMN:
                    try {
                        result = item.getProperty().getValue();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;

                default:
                    // should not happen
            }
        }
        else {
            result = item;
        }
        return result;
    }

    /**
     * Sets the value at the specified row and column.  This will have
     * no effect unless the row is a property and the column is
     * {@link #VALUE_COLUMN}.
     *
     * @see javax.swing.table.TableModel#setValueAt(java.lang.Object, int, int)
     */
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        Item item = getPropertySheetElement(rowIndex);
        if (item.isProperty() ) {
            if (columnIndex == VALUE_COLUMN) {
                try {
                    item.getProperty().setValue(value);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Add a {@link PropertyChangeListener} to the current model.
     */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        listeners.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        listeners.removePropertyChangeListener(listener);
    }

    public void propertyChange(PropertyChangeEvent evt) {
        // forward the event to registered listeners
        listeners.firePropertyChange(evt);
    }

    protected void visibilityChanged(final boolean restoreOldStates) {
        // Store the old visibility states
        if (restoreOldStates) {
            for (Iterator iter = publishedModel.iterator(); iter.hasNext(); ) {
                final Item item=(Item)iter.next();
                toggleStates.put(item.getKey(), item.isVisible() ? Boolean.TRUE : Boolean.FALSE);
            }
        }

        publishedModel.clear();
        for (Iterator iter = model.iterator(); iter.hasNext();) {
            Item item = (Item) iter.next();
            Item parent = item.getParent();
            if (restoreOldStates) {
                Boolean oldState=(Boolean)toggleStates.get(item.getKey());
                if (oldState!=null) {
                    item.setVisible(oldState.booleanValue());
                }
                if (parent!=null) {
                    oldState=(Boolean)toggleStates.get(parent.getKey());
                    if (oldState!=null) {
                        parent.setVisible(oldState.booleanValue());
                    }
                }
            }
            if (parent == null || parent.isVisible())
                publishedModel.add(item);
        }
    }

    private void buildModel() {
        model.clear();

        if (properties != null && properties.size() > 0) {
            List sortedProperties = sortProperties(properties);

            switch (mode) {
                case PropertySheet.VIEW_AS_FLAT_LIST:
                    // just add all the properties without categories
                    addPropertiesToModel(sortedProperties, null);
                    break;

                case PropertySheet.VIEW_AS_CATEGORIES: {
                    // add properties by category
                    List categories = sortCategories(getPropertyCategories(sortedProperties));

                    for (Iterator iter = categories.iterator(); iter.hasNext();) {
                        String category = (String) iter.next();
                        Item categoryItem = new Item(category, null);
                        model.add(categoryItem);
                        addPropertiesToModel(
                                sortProperties(getPropertiesForCategory(properties, category)),
                                categoryItem);
                    }
                    break;
                }

                default:
                    // should not happen
            }
        }

        visibilityChanged(restoreToggleStates);
        fireTableDataChanged();
    }

    protected List sortProperties(List localProperties) {
        List sortedProperties = new ArrayList(localProperties);
        if (sortingProperties) {
            if (propertySortingComparator == null) {
                // if no comparator was defined by the user, use the default
                propertySortingComparator = new PropertyComparator();
            }
            Collections.sort(sortedProperties, propertySortingComparator);
        }
        return sortedProperties;
    }

    protected List sortCategories(List localCategories) {
        List sortedCategories = new ArrayList(localCategories);
        if (sortingCategories) {
            if (categorySortingComparator == null) {
                // if no comparator was defined by the user, use the default
                categorySortingComparator = STRING_COMPARATOR;
            }
            Collections.sort(sortedCategories, categorySortingComparator);
        }
        return sortedCategories;
    }

    protected List<String> getPropertyCategories(List<SheetProperty> localProperties) {
        List<String> categories = new ArrayList<>();
        for (Iterator<SheetProperty> iter = localProperties.iterator(); iter.hasNext();) {
            SheetProperty property = iter.next();
            if (!categories.contains(property.getCategory()))
                categories.add(property.getCategory());
        }
        return categories;
    }

    /**
     * Add the specified properties to the model using the specified parent.
     *
     * @param localProperties the properties to add to the end of the model
     * @param parent the {@link Item} parent of these properties, null if none
     */
    private void addPropertiesToModel(List<SheetProperty> localProperties, Item parent) {
        for (Iterator iter = localProperties.iterator(); iter.hasNext();) {
            SheetProperty property = (SheetProperty) iter.next();
            Item propertyItem = new Item(property, parent);
            model.add(propertyItem);

            // add any sub-properties
            List<SheetProperty> subProperties = property.getSubProperties();
            if (subProperties != null && !subProperties.isEmpty())
                addPropertiesToModel(subProperties, propertyItem);
        }
    }

    /**
     * Convenience method to get all the properties of one category.
     */
    private List<SheetProperty> getPropertiesForCategory(List<SheetProperty> localProperties, String category) {
        List<SheetProperty> categoryProperties = new ArrayList<>();
        for (Iterator<SheetProperty> iter = localProperties.iterator(); iter.hasNext(); ) {
            SheetProperty property = iter.next();
            if ((category == property.getCategory())
                    || (category != null && category.equals(property.getCategory()))) {
                categoryProperties.add(property);
            }
        }
        return categoryProperties;
    }

    public class Item {
        private String name;
        private SheetProperty property;
        private Item parent;
        private boolean hasToggle = true;
        private boolean visible = true;

        private Item(String name, Item parent) {
            this.name = name;
            this.parent = parent;
            // this is not a property but a category, always has toggle
            this.hasToggle = true;
        }

        private Item(SheetProperty property, Item parent) {
            this.name = property.getDisplayName();
            this.property = property;
            this.parent = parent;
            this.visible = (property == null);

            // properties toggle if there are sub-properties
            List<SheetProperty> subProperties = property.getSubProperties();
            hasToggle = subProperties != null && !subProperties.isEmpty();
        }

        public String getName() {
            return name;
        }

        public boolean isProperty() {
            return property != null;
        }

        public SheetProperty getProperty() {
            return property;
        }

        public Item getParent() {
            return parent;
        }

        public int getDepth() {
            int depth = 0;
            if (parent != null) {
                depth = parent.getDepth();
                if (parent.isProperty())
                    ++depth;
            }
            return depth;
        }

        public boolean hasToggle() {
            return hasToggle;
        }

        public void toggle() {
            if (hasToggle()) {
                visible = !visible;
                visibilityChanged(false);
                fireTableDataChanged();
            }
        }

        public void setVisible(final boolean visible) {
            this.visible = visible;
        }

        public boolean isVisible() {
            return (parent == null || parent.isVisible()) && (!hasToggle || visible);
        }

        public String getKey() {
            StringBuffer key = new StringBuffer(name);
            Item itemParent = parent;
            while (itemParent != null) {
                key.append(":");
                key.append(itemParent.getName());
                itemParent = itemParent.getParent();
            }
            return key.toString();
        }

    }

    /**
     * The default comparator for Properties. Used if no other comparator is
     * defined.
     */
    public static class PropertyComparator implements Comparator<SheetProperty> {
        public int compare(SheetProperty prop1, SheetProperty prop2) {
            if (prop1 == null && prop2 == null) return 0;

            if (prop1 == null) return prop2==null?0:-1;
            return STRING_COMPARATOR.compare(prop1.getDisplayName()==null?null:prop1.getDisplayName().toLowerCase(),
                prop2.getDisplayName() == null ? null : prop2.getDisplayName().toLowerCase());
        }
    }

    private static final Comparator STRING_COMPARATOR = new NaturalOrderStringComparator();

    public static class NaturalOrderStringComparator implements Comparator {
        public int compare(Object o1, Object o2) {
            String s1 = (String) o1;
            String s2 = (String) o2;
            if (s1 == null) {
                return s2==null?0:-1;
            } else {
                if (s2 == null) {
                    return 1;
                } else {
                    return s1.compareTo(s2);
                }
            }
        }
    }
}
