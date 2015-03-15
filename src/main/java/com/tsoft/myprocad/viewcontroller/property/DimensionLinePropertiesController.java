package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

import java.awt.*;

public class DimensionLinePropertiesController extends AbstractComponentPropertiesController<DimensionLine> {
    public DimensionLinePropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_X_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine) object).getXStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setXStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_X_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine)object).getXEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setXEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_X_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveX((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_X_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftX(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Y_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine) object).getYStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setYStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Y_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine)object).getYEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setYEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Y_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveY((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Y_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setValueValidator((item, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((wall, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftY(plan, (int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Z_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine) object).getZStart())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setZStart((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_LINE_Z_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine)object).getZEnd())
            .setValueValidator((item, value) -> { return ((Item) item).validateCoordinate((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine) object).setZEnd((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_LENGTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(dimensionLine -> Math.round(((DimensionLine) dimensionLine).getLength()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_ANGLE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(dimensionLine -> ((DimensionLine) dimensionLine).getAngle(plan.getSelection()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(object -> new Color(((DimensionLine) object).getColor()))
            .setValueSetter((object, value) -> ((DimensionLine) object).setColor(((Color) value).getRGB()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_LINE_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine) object).getLineWidth())
            .setValueValidator((object, value) -> { return ((DimensionLine) object).validateLineWidth((Integer) value); })
            .setValueSetter((object, value) -> ((DimensionLine)object).setLineWidth((int) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_TEXT_PROPERTY))
            .setType(String.class)
            .setValueGetter(object -> ((DimensionLine)object).getText())
            .setValueSetter((object, value) -> ((DimensionLine)object).setText((String) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_OFFSET_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine)object).getOffset())
            .setValueValidator((object, value) -> { return ((DimensionLine) object).validateOffset((Integer) value); })
            .setValueSetter((object, value) -> {
                addToHistory((DimensionLine) object);
                ((DimensionLine)object).setOffset((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_FONT_FAMILY_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(new String[]{Font.DIALOG, Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED})
            .setValueGetter(object -> ((DimensionLine) object).getFontFamily())
            .setValueSetter((object, value) -> { if (value != null) ((DimensionLine) object).setFontFamily((String) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_FONT_SIZE_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((DimensionLine)object).getFontSize())
            .setValueValidator((object, value) -> { return ((DimensionLine) object).validateFontSize((Integer) value); })
            .setValueSetter((object, value) -> ((DimensionLine)object).setFontSize((int) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_START_POINT_SHAPE_TYPE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PointShapeType.values())
            .setValueGetter(dimensionLine -> ((DimensionLine) dimensionLine).getStartPointShapeType())
            .setValueSetter((dimensionLine, value) -> {
                if (value != null) ((DimensionLine)dimensionLine).setStartPointShapeType((PointShapeType) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.DIMENSION_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.DIMENSION_END_POINT_SHAPE_TYPE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PointShapeType.values())
            .setValueGetter(dimensionLine -> ((DimensionLine) dimensionLine).getEndPointShapeType())
            .setValueSetter((dimensionLine, value) -> {
                if (value != null) ((DimensionLine) dimensionLine).setEndPointShapeType((PointShapeType) value);
            });
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.DIMENSION_LINE_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
