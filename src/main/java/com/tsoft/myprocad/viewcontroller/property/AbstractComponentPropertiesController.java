package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.model.*;

public abstract class AbstractComponentPropertiesController<T> extends AbstractPropertiesController<T> {
    protected Plan plan;;

    protected abstract void setPanelProperties();

    public AbstractComponentPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager, planPropertiesManager.propertiesManagerPanel);

        plan = planPropertiesManager.plan;

        init();
    }

    protected Project getProject() { return plan.getProject(); }

    protected void addToHistory(Item item) {
        plan.getController().history.cloneAndPush(item);
    }

    protected void addToHistory(ItemList<Item> items) {
        plan.getController().history.cloneAndPush(items);
    }
}
