package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.property.ObjectProperty;

public abstract class AbstractComponentPropertiesController<T> extends AbstractPropertiesController<T> {
    protected Plan plan;

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

    @Override
    protected void initObjectProperties() {
        addXProperties();
        addYProperties();
        addZProperties();
    }

    private void addXProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getXStart())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setXStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getXEnd())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setXEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getXDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> plan.getSelection().getXMin());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> plan.getSelection().getXMax());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveX((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftX(plan, (int) value);
            });
    }

    private void addYProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getYStart())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setYStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getYEnd())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setYEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getYDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> plan.getSelection().getYMin());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> plan.getSelection().getYMax());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveY((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftY(plan, (int) value);
            });
    }

    private void addZProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getZStart())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setZStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getZEnd())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setZEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getZDistance());
    }
}
