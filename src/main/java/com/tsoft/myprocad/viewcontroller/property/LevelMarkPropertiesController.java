package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.LevelMark;
import com.tsoft.myprocad.model.Rotation;
import com.tsoft.myprocad.model.Selection;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

import java.awt.Font;

public class LevelMarkPropertiesController extends AbstractComponentPropertiesController<LevelMark> {
    public LevelMarkPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_X_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((LevelMark) object).getX())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((LevelMark) object);
                ((LevelMark) object).setX((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateMoveX((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_Y_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((LevelMark)object).getY())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((LevelMark) object);
                ((LevelMark)object).setY((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateMoveY((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_Z_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((LevelMark)object).getZStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((LevelMark) object);
                ((LevelMark)object).setZStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LEVEL_MARK_COORDINATES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_Z_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((LevelMark)object).getZEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((LevelMark)object);
                ((LevelMark)object).setZEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_ROTATED_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(Rotation.values())
            .setValueGetter(object -> ((LevelMark) object).getRotation())
                .setValueSetter((object, value) -> ((LevelMark)object).setRotation((Rotation) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_TEXT_PROPERTY))
            .setType(String.class)
            .setValueGetter(object -> ((LevelMark)object).getText())
            .setValueSetter((object, value) -> ((LevelMark)object).setText((String) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_FONT_FAMILY_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(Font.DIALOG, Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED)
            .setValueGetter(object -> ((LevelMark) object).getFontFamily())
            .setValueSetter((object, value) -> { if (value != null) ((LevelMark) object).setFontFamily((String) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LEVEL_MARK_FONT_SIZE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((LevelMark)object).getFontSize())
            .setValueSetter((object, value) -> { if (value != null) ((LevelMark)object).setFontSize((int) value); });
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.LEVEL_MARK_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
