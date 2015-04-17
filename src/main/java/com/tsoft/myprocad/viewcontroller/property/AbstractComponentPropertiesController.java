package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;
import com.l2fprod.common.beans.editor.ObjectListPropertyEditor;
import com.l2fprod.common.beans.editor.TextPropertyViewer;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.DistributedForceTableDialogSupport;
import com.tsoft.myprocad.model.ForceTableDialogSupport;
import com.tsoft.myprocad.model.Item;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Material;
import com.tsoft.myprocad.model.MaterialList;
import com.tsoft.myprocad.model.MomentTableDialogSupport;
import com.tsoft.myprocad.model.Pattern;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.Project;
import com.tsoft.myprocad.model.Selection;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.tsoft.myprocad.swing.BeamPanel;
import com.tsoft.myprocad.swing.properties.PatternComboBoxPropertyEditor;

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
    }

    private void addXProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.START_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
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
            .setCalculable(true)
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
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getXDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getXMin());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getXMax());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.X_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
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
            .setCalculable(true)
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
            .setCalculable(true)
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
            .setCalculable(true)
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
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getYDistance());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MIN_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getYMin());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MAX_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getYMax());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.Y_CATEGORY))
            .setLabelName(L10.get(L10.MOVE_PROPERTY))
            .setType(Integer.class)
            .setCalculable(true)
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
            .setCalculable(true)
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
            .setCalculable(true)
            .setValueGetter(item -> ((Item) item).getZDistance());
    }

    protected void addMaterialItemProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.WALL_PATTERN_PROPERTY))
            .setType(PatternComboBoxPropertyEditor.class)
            .setAvailableValues(Pattern.values())
            .setValueGetter(item -> ((AbstractMaterialItem) item).getPattern())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setPattern(((Pattern) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BACKGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBackgroundColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBackgroundColor((Color) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.FOREGROUND_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getForegroundColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setForegroundColor((Color) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBorderColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBorderColor((Color) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.VIEW_CATEGORY))
            .setLabelName(L10.get(L10.BORDER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getBorderWidth())
            .setValueValidator((item, value) -> ((AbstractMaterialItem) item).validateBorderWidth((Integer) value))
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setBorderWidth((int) value));

        material = new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.WALL_MATERIAL_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(getAvailableMaterials())
            .setValueGetter(item -> ((AbstractMaterialItem) item).getMaterial())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setMaterial((Material) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PROPERTIES_CATEGORY))
            .setLabelName(L10.get(L10.MATERIAL_DENSITY_PROPERTY))
            .setType(Float.class)
            .setCalculable(true)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getDensity());
    }

    protected void addCalculatedProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.LENGTH_PROPERTY))
            .setType(Float.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsLength());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.AREA_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsArea());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.VOLUME_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsVolume());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.WEIGHT_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsWeight());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.INFO_CATEGORY))
            .setLabelName(L10.get(L10.PRICE_PROPERTY))
            .setType(Double.class)
            .setCalculable(true)
            .setValueGetter(item -> plan.getSelection().getMaterialItemsPrice());
    }

    protected void addMechanicsProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_LEFT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getLeftSupport())
            .setValueValidator((entity, value) -> ((AbstractMaterialItem) entity).validateLeftSupport(((Double) value)))
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setLeftSupport((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_RIGHT_SUPPORT_PROPERTY))
            .setType(Double.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getRightSupport())
            .setValueValidator((entity, value) -> ((AbstractMaterialItem) entity).validateRightSupport(((Double) value)))
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setRightSupport((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ELASTIC_STRENGTH_PROPERTY))
            .setType(Double.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getElasticStrength())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setElasticStrength((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_ALLOWABLE_STRESS_PROPERTY))
            .setType(Double.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getAllowableStress())
            .setValueSetter((entity, value) -> ((AbstractMaterialItem) entity).setAllowableStress((Double) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_BENDING_MOMENTS_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> {
                MomentTableDialogSupport support = new MomentTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getMoments());
                return support;
            })
            .setValueValidator((entity, value) -> {
                MomentTableDialogSupport support = (MomentTableDialogSupport)value;
                return ((AbstractMaterialItem) entity).validateMoments(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                MomentTableDialogSupport support = (MomentTableDialogSupport)value;
                ((AbstractMaterialItem) entity).setMoments(support.getElements());
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setTooComplexForSelectionOperation(true)
            .setValueGetter(entity -> {
                ForceTableDialogSupport support = new ForceTableDialogSupport();
                support.setElements(((AbstractMaterialItem) entity).getForces());
                return support;
            })
            .setValueValidator((entity, value) -> {
                ForceTableDialogSupport support = (ForceTableDialogSupport)value;
                return ((AbstractMaterialItem) entity).validateForces(support.getElements());
            })
            .setValueSetter((entity, value) -> {
                ForceTableDialogSupport support = (ForceTableDialogSupport)value;
                ((AbstractMaterialItem) entity).setForces(support.getElements());
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_BEAM_DISTRIBUTED_FORCES_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setTooComplexForSelectionOperation(true)
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
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.CALCULATION_PARAMETERS_CATEGORY))
            .setLabelName(L10.get(L10.CALCULATION_PROPERTY))
            .setType(TextPropertyViewer.class)
            .setTooComplexForSelectionOperation(true)
            .setDialogPanel(new BeamPanel())
            .setValueGetter(entity -> ((AbstractMaterialItem) entity).getMechanicsSolution());
    }

    protected void add3dItemProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.SHOW_WIRED_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getShowWired())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setShowWired((boolean) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KA_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKaColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKaColor(((Color) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KD_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKdColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKdColor(((Color) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KS_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKsColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKsColor(((Color) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.KE_COLOR_PROPERTY))
            .setType(ColorPropertyEditor.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getKeColor())
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setKeColor(((Color) value)));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.SHININESS_PROPERTY))
            .setType(Float.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getShininess())
            .setValueValidator((item, value) -> { return ((AbstractMaterialItem)item).validateShininess((Float)value); })
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setShininess((float) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.J3D_CATEGORY))
            .setLabelName(L10.get(L10.TRANSPARENCY_PROPERTY))
            .setType(Float.class)
            .setValueGetter(item -> ((AbstractMaterialItem) item).getTransparency())
            .setValueValidator((item, value) -> { return ((AbstractMaterialItem)item).validateTransparency((Float)value); })
            .setValueSetter((item, value) -> ((AbstractMaterialItem) item).setTransparency((float) value));
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
