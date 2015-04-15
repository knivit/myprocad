package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PropertiesManagerPanel;
import com.tsoft.myprocad.swing.properties.SheetProperty;
import com.tsoft.myprocad.util.ObjectUtil;

import java.util.*;
import java.util.stream.Collectors;

public abstract class AbstractPropertiesController<T> {
    private PlanPropertiesManager planPropertiesManager;
    protected PropertiesManagerPanel propertiesManagerPanel;
    protected List<SheetProperty> panelProperties;

    private List<T> selectedItems = new ArrayList<>();

    private Map<ObjectProperty, Object> values = new HashMap<>();
    private Set<ObjectProperty> changedProperties = new HashSet<>();

    private ObjectPropertyList properties = new ObjectPropertyList();

    protected abstract void initObjectProperties();
    protected abstract void setPanelProperties();

    public AbstractPropertiesController(PlanPropertiesManager planPropertiesManager, PropertiesManagerPanel propertiesManagerPanel) {
        super();

        this.planPropertiesManager = planPropertiesManager;
        this.propertiesManagerPanel = propertiesManagerPanel;
    }

    protected void init() {
        initObjectProperties();
        initPanelProperties();
    }

    public void addObjectProperty(ObjectProperty objectProperty) {
        properties.add(objectProperty);
    }

    private void initPanelProperties() {
        panelProperties = new ArrayList<>();
        panelProperties.addAll(properties.stream().map(this::createProperty).collect(Collectors.toList()));
    }

    public List<T> getSelectedItems() { return selectedItems; }

    public void selectObjects(List<T> selectedItems) {
        this.selectedItems = selectedItems;

        setPanelProperties();
        loadAndRefreshValues();
    }

    public SheetProperty createProperty(ObjectProperty property) {
        return new SheetProperty(this, property);
    }

    public Object getPropertyValue(ObjectProperty property) {
        return values.get(property);
    }

    public void setPropertyValue(ObjectProperty property, Object value) {
        values.put(property, value);
        changedProperties.add(property);
    }

    public void refreshValues() {
        propertiesManagerPanel.refreshValues();
    }

    public void loadValues() {
        values.clear();
        changedProperties.clear();
        if (selectedItems.isEmpty()) return;

        // Copy first selected object's properties
        // Do not use setPropertyValue as it changes changedProperties
        Iterator<T> iterator = selectedItems.iterator();
        T firstObject = iterator.next();
        for (ObjectProperty property : properties) {
            Object value = getObjectPropertyValue(firstObject, property);
            values.put(property, value);
        }

        // Reset properties with different values
        while (iterator.hasNext()) {
            T object = iterator.next();
            for (ObjectProperty property : properties) {
                Object currValue = getPropertyValue(property);
                Object newValue = getObjectPropertyValue(object, property);
                if (!ObjectUtil.equals(currValue, newValue)) {
                    values.put(property, null);
                }
            }
        }
    }

    public void loadAndRefreshValues() {
        loadValues();
        refreshValues();
    }

    private Object getObjectPropertyValue(T object, ObjectProperty property) {
        ObjectProperty.Getter getter = property.getValueGetter();
        if (getter == null) return null;

        try {
            return getter.getValue(object);
        } catch (Exception ex) {
            System.err.println("Can't get value for " + object.getClass().getName() + ":" + property.getLabelName());
            ex.printStackTrace();
        }
        return null;
    }

    public void applyChanges() {
        if (planPropertiesManager != null) applyChangesWithPropertiesManager();
        else applyChangesWithoutPropertiesManager();
    }

    // Common usage
    private void applyChangesWithPropertiesManager() {
        planPropertiesManager.disableReload();
        try {
            // set items property
            applyChangesInternal();

            // reload calculable properties
            loadValues();
            refreshValues();

            // reload Selection properties
            planPropertiesManager.selectionStateChanged();
        } finally {
            planPropertiesManager.enableReload();
        }
    }

    // Without PropertiesManager installed, i.e. ApplicationPropertiesController
    private void applyChangesWithoutPropertiesManager() {
        applyChangesInternal();
    }

    private void applyChangesInternal() {
        for (T object : selectedItems) {
            for (ObjectProperty property : properties) {
                if (!changedProperties.contains(property) || !property.isEditable()) {
                    continue;
                }

                Object value = values.get(property);
                property.getValueSetter().setValue(object, value);
            }
        }
    }
}
