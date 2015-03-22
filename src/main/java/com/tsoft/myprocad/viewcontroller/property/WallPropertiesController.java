package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Material;
import com.tsoft.myprocad.model.MaterialList;
import com.tsoft.myprocad.model.Pattern;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.model.WallShape;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.properties.PatternComboBoxPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

import java.awt.Color;
import java.util.Collections;

public class WallPropertiesController extends AbstractComponentPropertiesController<Wall> {
    private ObjectProperty material;

    public WallPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        super.initObjectProperties();
        addViewProperties();
        addInfoProperties();
    }

    private void addViewProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_SHAPE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(WallShape.values())
            .setValueGetter(wall -> ((Wall) wall).getWallShape())
            .setValueSetter((wall, value) -> {
                addToHistory((Wall) wall);
                ((Wall) wall).setWallShape(((WallShape) value));
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_DIAGONAL_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(wall -> ((Wall) wall).getDiagonalWidth())
            .setValueValidator((item, value) -> { return ((Wall)item).validateDiagonalWidth((Integer)value); })
            .setValueSetter((wall, value) -> {
                addToHistory((Wall) wall);
                ((Wall) wall).setDiagonalWidth((int) value);
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_PATTERN_PROPERTY))
            .setType(PatternComboBoxPropertyEditor.class)
            .setAvailableValues(Pattern.values())
            .setValueGetter(wall -> ((Wall) wall).getPattern())
            .setValueSetter((wall, value) -> ((Wall)wall).setPattern(((Pattern) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BACKGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(object -> new Color(((Wall) object).getBackgroundColor()))
            .setValueSetter((object, value) -> ((Wall) object).setBackgroundColor(((Color) value).getRGB()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.FOREGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(object -> new Color(((Wall) object).getForegroundColor()))
            .setValueSetter((object, value) -> ((Wall) object).setForegroundColor(((Color) value).getRGB()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(object -> new Color(((Wall) object).getBorderColor()))
            .setValueSetter((object, value) -> ((Wall) object).setBorderColor(((Color) value).getRGB()));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(object -> ((Wall) object).getBorderWidth())
            .setValueValidator((object, value) -> ((Wall) object).validateBorderWidth((Integer) value))
            .setValueSetter((object, value) -> ((Wall) object).setBorderWidth((int) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_ALWAYS_SHOW_BORDERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(wall -> ((Wall)wall).isAlwaysShowBorders())
            .setValueSetter((wall, value) -> ((Wall)wall).setAlwaysShowBorders(((boolean) value)));

        material = new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.WALL_MATERIAL_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(getAvailableMaterials())
            .setValueGetter(wall -> ((Wall)wall).getMaterial())
            .setValueSetter((wall, value) -> ((Wall)wall).setMaterial((Material) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.WALL_DENSITY_PROPERTY))
            .setType(Float.class)
            .setValueGetter(wall -> ((Wall)wall).getDensity());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.WALL_SKIP_IN_REPORTS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(wall -> ((Wall)wall).isSkipInReports())
            .setValueSetter((wall, value) -> ((Wall)wall).setSkipInReports((boolean) value));
    }

    private void addInfoProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.SELECTION_AMOUNT_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(wall -> plan.getSelection().getWallsAmount());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WALL_OUTER_LENGTH_PROPERTY))
            .setType(Float.class)
            .setValueGetter(wall -> plan.getSelection().getWallsOuterLength());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.AREA_PROPERTY))
            .setType(Double.class)
            .setValueGetter(wall -> plan.getSelection().getWallsArea());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WALL_VOLUME_PROPERTY))
            .setType(Double.class)
            .setValueGetter(wall -> plan.getSelection().getWallsVolume());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WALL_WEIGHT_PROPERTY))
            .setType(Double.class)
            .setValueGetter(wall -> plan.getSelection().getWallsWeight());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WALL_PRICE_PROPERTY))
            .setType(Double.class)
            .setValueGetter(wall -> plan.getSelection().getWallsPrice());
    }

    @Override
    public void refreshValues() {
        refreshMaterialList();
        super.refreshValues();
    }

    public void refreshMaterialList() {
        material.setAvailableValues(getAvailableMaterials());
    }

    public Object[] getAvailableMaterials() {
        MaterialList list = getProject().getMaterials();
        Collections.sort(list);
        return list.toArray();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.WALL_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}