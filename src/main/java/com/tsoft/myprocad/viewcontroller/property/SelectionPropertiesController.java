package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Selection;
import com.tsoft.myprocad.model.property.ObjectProperty;

public class SelectionPropertiesController extends AbstractComponentPropertiesController<Selection> {
    public SelectionPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_INFO_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_AMOUNT_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getItems().size())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_X_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_X_MIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getXMin())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_X_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_X_MAX_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getXMax())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_X_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_X_PROPERTY))
            .setType(Integer.class)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).moveX(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_X_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_DX_PROPERTY))
            .setType(Integer.class)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).shiftX(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Y_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_Y_MIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getYMin())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Y_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_Y_MAX_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getYMax())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Y_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_Y_PROPERTY))
            .setType(Integer.class)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).moveY(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Y_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_DY_PROPERTY))
            .setType(Integer.class)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).shiftY(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_Z_MIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getZMin())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_Z_MAX_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> ((Selection)selection).getZMax())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_SET_Z_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> null)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).setZStart(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_SET_Z_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> null)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).setZEnd(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_Z_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(selection -> null)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection) selection).getItems());
                    ((Selection)selection).moveZ(plan, (int) value);
                }
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.SELECTION_Z_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_MOVE_DZ_PROPERTY))
            .setType(Integer.class)
            .setValueSetter((selection, value) -> {
                if (value != null) {
                    addToHistory(((Selection)selection).getItems());
                    ((Selection)selection).moveDz(plan, (int) value);
                }
            })
        );
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.SELECTION_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
