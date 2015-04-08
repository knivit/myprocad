package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Wall;
import com.tsoft.myprocad.model.WallShape;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class WallPropertiesController extends AbstractComponentPropertiesController<Wall> {
    public WallPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        addCommonProperties();
        addViewProperties();
        addMaterialItemProperties();
        addCalculatedProperties();
        addInfoProperties();
        add3dItemProperties();
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
            .setLabelName(L10.get(L10.WALL_ALWAYS_SHOW_BORDERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(wall -> ((Wall)wall).isAlwaysShowBorders())
            .setValueSetter((wall, value) -> ((Wall)wall).setAlwaysShowBorders(((boolean) value)));

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
    }

    @Override
    public void refreshValues() {
        refreshMaterialList();
        super.refreshValues();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.WALL_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }
}
