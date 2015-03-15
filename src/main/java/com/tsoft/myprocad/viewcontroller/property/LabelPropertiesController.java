package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.Label;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.util.StringUtil;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.beans.editor.TextPropertyEditor;

import java.awt.*;

public class LabelPropertiesController extends AbstractComponentPropertiesController<Label> {
    public LabelPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_X_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label) object).getXStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setXStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_X_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label)object).getXEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setXEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_X_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(label -> ((Label) label).getXDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_X_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveX((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_X_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Y_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label)object).getYStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setYStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Y_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label)object).getYEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setYEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Y_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(label -> ((Label) label).getYDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Y_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveY((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Y_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Z_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label) object).getZStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setZStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_Z_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label)object).getZEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((Label) object);
                ((Label) object).setZEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_ROTATION_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(Rotation.values())
            .setValueGetter(object -> ((Label) object).getRotation())
            .setValueSetter((object, value) -> ((Label)object).setRotation((Rotation) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(object -> new Color(((Label) object).getBorderColor()))
            .setValueSetter((object, value) -> ((Label) object).setBorderColor(((Color) value).getRGB()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label) object).getBorderWidth())
            .setValueValidator((object, value) -> ((Label) object).validateBorderWidth((Integer) value))
            .setValueSetter((object, value) -> ((Label) object).setBorderWidth((int) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_TEXT_PROPERTY))
            .setType(TextPropertyEditor.class)
            .setValueGetter(object -> StringUtil.replaceAll(((Label)object).getText(), Character.toString('\n'), "\\n"))
            .setValueSetter((object, value) -> ((Label)object).setText(StringUtil.replaceAll((String) value, "\\n", Character.toString('\n'))));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_FONT_FAMILY_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(Font.DIALOG, Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED)
            .setValueGetter(object -> ((Label) object).getFontFamily())
            .setValueSetter((object, value) -> { if (value != null) ((Label) object).setFontFamily((String) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.LABEL_FONT_SIZE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Label)object).getFontSize())
            .setValueSetter((object, value) -> { if (value != null) ((Label)object).setFontSize((int)value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.LABEL_PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.AREA_PROPERTY))
            .setType(Float.class)
            .setValueGetter(item -> ((Label)item).getArea());
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.LABEL_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
