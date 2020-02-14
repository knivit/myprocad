package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.beans.editor.DialogButtonsPropertyEditor;
import com.l2fprod.common.beans.editor.ObjectListPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.model.BeamSag;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.BeamPanel;
import com.tsoft.myprocad.swing.WoodBeamPanel;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.MenuAction;
import com.tsoft.myprocad.swing.properties.PatternComboBoxPropertyEditor;
import com.tsoft.myprocad.util.SwingTools;

import java.awt.Color;
import java.util.Collections;

public abstract class AbstractComponentPropertiesController<T> extends AbstractPropertiesController<T> {
    protected Plan plan;
    private ObjectProperty material;

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

    protected void addCommonProperties() {
        addXProperties();
        addYProperties();
        addZProperties();
        addUserDefinedProperties();
    }

    private void addXProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getXStart())
            .setValueValidator((item, value) -> Item.validateCoordinate((Integer) value))
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setXStart((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getXEnd())
            .setValueValidator((item, value) -> Item.validateCoordinate((Integer) value))
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setXEnd((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getXDistance())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getXMin())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getXMax())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueValidator((item, value) -> plan.getSelection().validateMoveX((Integer) value))
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveX(plan, (int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueValidator((item, value) -> plan.getSelection().validateShift((Integer) value))
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftX(plan, (int) value);
            })
        );
    }

    private void addYProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getYStart())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setYStart((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getYEnd())
            .setValueValidator((item, value) -> { return ((Item)item).validateCoordinate((Integer)value); })
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setYEnd((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getYDistance())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getYMin())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getYMax())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueValidator((item, value) -> { return plan.getSelection().validateMoveY((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.moveY(plan, (int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.SHIFT_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueValidator((wall, value) -> { return plan.getSelection().validateShift((Integer) value); })
            .setValueSetter((item, value) -> {
                Selection selection = plan.getSelection();
                addToHistory(selection.getItems());
                selection.shiftY(plan, (int) value);
            })
        );
    }

    private void addZProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getZStart())
            .setValueValidator((item, value) -> ((Item)item).validateCoordinate((Integer)value))
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setZStart((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((Item) item).getZEnd())
            .setValueValidator((item, value) -> ((Item)item).validateCoordinate((Integer)value))
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setZEnd((int) value);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.Z_CATEGORY))
            .setLabelName(L10.get(L10.DISTANCE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getZDistance())
        );
    }


    private void addUserDefinedProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.USER_CATEGORY))
            .setLabelName(L10.get(L10.TAGS))
            .setType(String.class)
            .setValueGetter(item -> ((Item) item).getTags())
            .setValueSetter((item, value) -> {
                addToHistory((Item) item);
                ((Item) item).setTags((String) value);
            })
        );
    }

    protected void addMaterialItemProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_PATTERN_PROPERTY))
            .setType(PatternComboBoxPropertyEditor.class)
            .setAvailableValues(Pattern.values())
            .setValueGetter(item -> ((AbstractMaterialItem) item).getPattern())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setPattern(((Pattern) value)))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BACKGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBackgroundColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBackgroundColor((Color) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.FOREGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getForegroundColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setForegroundColor((Color) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBorderColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBorderColor((Color) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBorderWidth())
            .setValueValidator((item, value) -> ((AbstractMaterialItem) item).validateBorderWidth((Integer) value))
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBorderWidth((int) value))
        );

        material = new ObjectProperty()
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.WALL_MATERIAL_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(getAvailableMaterials())
            .setValueGetter(item -> ((AbstractMaterialItem) item).getMaterial())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setMaterial((Material) value));
        addObjectProperty(material);

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.MATERIAL_DENSITY_PROPERTY))
            .setType(Float.class)
            .setCalculable(true)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getDensity())
        );
    }

    protected void addCalculatedProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.LENGTH_PROPERTY))
            .setType(Float.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsLength())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.AREA_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsArea())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.VOLUME_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsVolume())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WEIGHT_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsWeight())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.PRICE_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsPrice())
        );
    }

    protected void addMechanicsProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_LEFT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setSingleSelection(true)
            .setNullable(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getLeftSupport())
            .setValueValidator((entity, value) -> ((AbstractMaterialItem) entity).validateLeftSupport(((Double) value)))
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setLeftSupport((Double) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_RIGHT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setSingleSelection(true)
            .setNullable(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getRightSupport())
            .setValueValidator((entity, value) -> ((AbstractMaterialItem) entity).validateRightSupport(((Double) value)))
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setRightSupport((Double) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ELASTIC_STRENGTH_PROPERTY))
            .setType(Double.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getElasticStrength())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setElasticStrength((Double) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ALLOWABLE_STRESS_PROPERTY))
            .setType(Double.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getAllowableStress())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setAllowableStress((Double) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_BENDING_MOMENTS_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> {
                MomentTableDialogSupport support = new MomentTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getMoments());
                return support;
            })
            .setValueValidator((entity, value) -> {
                MomentTableDialogSupport support = (MomentTableDialogSupport) value;
                return ((AbstractMaterialItem) entity).validateMoments(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                MomentTableDialogSupport support = (MomentTableDialogSupport)value;
                ((AbstractMaterialItem) entity).setMoments(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> {
                ForceTableDialogSupport support = new ForceTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getForces());
                return support;
            })
            .setValueValidator((entity, value) -> {
                ForceTableDialogSupport support = (ForceTableDialogSupport) value;
                return ((AbstractMaterialItem) entity).validateForces(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                ForceTableDialogSupport support = (ForceTableDialogSupport)value;
                ((AbstractMaterialItem) entity).setForces(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> {
                DistributedForceTableDialogSupport support = new DistributedForceTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getDistributedForces());
                return support;
            })
            .setValueValidator((entity, value) -> {
                DistributedForceTableDialogSupport support = (DistributedForceTableDialogSupport) value;
                return ((AbstractMaterialItem) entity).validateDistributedForces(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                DistributedForceTableDialogSupport support = (DistributedForceTableDialogSupport) value;
                ((AbstractMaterialItem) entity).setDistributedForces(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_B_PROPERTY))
            .setType(Double.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getB())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setB((Double) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_PERMANENT_LOAD_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> {
                PermanentLoadTableDialogSupport support = new PermanentLoadTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getPermanentLoad());
                return support;
            })
            .setValueValidator((entity, value) -> {
                PermanentLoadTableDialogSupport support = (PermanentLoadTableDialogSupport) value;
                return ((AbstractMaterialItem) entity).validatePermanentLoad(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                PermanentLoadTableDialogSupport support = (PermanentLoadTableDialogSupport) value;
                ((AbstractMaterialItem) entity).setPermanentLoad(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_TEMPORARY_LOAD_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> {
                TemporaryLoadTableDialogSupport support = new TemporaryLoadTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getTemporaryLoad());
                return support;
            })
            .setValueValidator((entity, value) -> {
                TemporaryLoadTableDialogSupport support = (TemporaryLoadTableDialogSupport) value;
                return ((AbstractMaterialItem) entity).validateTemporaryLoad(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                TemporaryLoadTableDialogSupport support = (TemporaryLoadTableDialogSupport) value;
                ((AbstractMaterialItem) entity).setTemporaryLoad(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_WOOD_BEAM_SAG_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(BeamSag.values())
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getBeamSag())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setBeamSag((BeamSag) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_PROPERTY))
            .setType(DialogButtonsPropertyEditor.class)
            .setSingleSelection(true)
            .setValueGetter(entity -> L10.get(L10.VIEW_VALUE))
            .addEditorButton(new MenuAction(Menu.VIEW_VALUE, e -> {
                BeamPanel beamPanel = new BeamPanel();
                AbstractMaterialItem materialItem = (AbstractMaterialItem) plan.getSelection().getItems().get(0);
                if (materialItem.applyMechanicsSolution(beamPanel)) {
                    beamPanel.displayView(L10.get(L10.CALCULATION_PROPERTY), DialogButton.CLOSE);
                } else {
                    SwingTools.showMessage(L10.get(L10.FILL_MECHANICS_PROPERTIES));
                }
            }))
            .addEditorButton(new MenuAction(Menu.VIEW_VALUE, e -> {
                WoodBeamPanel beamPanel = new WoodBeamPanel();
                AbstractMaterialItem materialItem = (AbstractMaterialItem) plan.getSelection().getItems().get(0);
                if (materialItem.applyWoodBeamSolution(beamPanel)) {
                    beamPanel.displayView(L10.get(L10.CALCULATION_PROPERTY), DialogButton.CLOSE);
                } else {
                    SwingTools.showMessage(L10.get(L10.FILL_MECHANICS_PROPERTIES));
                }
            }))
        );
    }

    protected void add3dItemProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.SHOW_WIRED_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getShowWired())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setShowWired((boolean) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KA_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKaColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKaColor(((Color) value)))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KD_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKdColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKdColor(((Color) value)))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KS_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKsColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKsColor(((Color) value)))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KE_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKeColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKeColor(((Color) value)))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.SHININESS_PROPERTY))
            .setType(Float.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getShininess())
            .setValueValidator((item, value) -> ((AbstractMaterialItem)item).validateShininess((Float)value))
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setShininess((float) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.TRANSPARENCY_PROPERTY))
            .setType(Float.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getTransparency())
            .setValueValidator((item, value) -> ((AbstractMaterialItem)item).validateTransparency((Float)value))
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setTransparency((float) value))
        );
    }

    protected void refreshMaterialList() {
        material.setAvailableValues(getAvailableMaterials());
    }

    protected Object[] getAvailableMaterials() {
        MaterialList list = getProject().getMaterials();
        Collections.sort(list);
        return list.toArray();
    }
}
