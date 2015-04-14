package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ColorPropertyEditor;
import com.l2fprod.common.propertysheet.CellEditorAdapter;
import com.l2fprod.common.swing.renderer.ColorCellRenderer;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.swing.dialog.DialogButton;
import com.tsoft.myprocad.swing.dialog.TableDialogPanel;
import com.tsoft.myprocad.swing.dialog.TableDialogSupport;
import com.tsoft.myprocad.util.printer.PaperSize;
import com.tsoft.myprocad.viewcontroller.PasteOperation;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableColumn;
import java.util.List;

public class PlanPropertiesController extends AbstractComponentPropertiesController<Plan> {
    private ObjectProperty level;

    public PlanPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_NAME_PROPERTY))
            .setType(String.class)
            .setValueGetter(plan1 -> ((Plan) plan1).getName())
            .setValueSetter((plan1, value) -> { if (value != null) ((Plan) plan1).setName((String) value); });

        level = new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .addEditorButton(e -> plan.getController().modifyLevels())
            .setAvailableValues(plan.getLevels().toArray())
            .setValueGetter(plan -> ((Plan) plan).getLevel())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setLevel((Level) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getLevel().getStart());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getLevel().getEnd());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LIGHTS_PROPERTY))
            .setType(Integer.class)
            .addEditorButton(e -> modifyLights())
            .setValueGetter(entity -> ((Plan) entity).getLights().size());

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_RULERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).isRulersVisible())
            .setValueSetter((plan, value) -> ((Plan) plan).setRulersVisible((boolean) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_GRID_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).isGridVisible())
            .setValueSetter((plan, value) -> ((Plan) plan).setGridVisible((boolean) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_SCALE_PROPERTY))
            .setType(Float.class)
            .setValueGetter(plan -> ((Plan) plan).getScale())
            .setValueValidator((plan, value) -> { return ((Plan)plan).validateScale((Float)value); })
            .setValueSetter((plan, value) -> ((Plan) plan).setScale((float) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_X_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetX())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetX((int) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_Y_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetY())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetY((int) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_Z_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetZ())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetZ((int) value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OPERATION_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PasteOperation.values())
            .setValueGetter(plan -> ((Plan) plan).getPasteOperation())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOperation((PasteOperation) value); });

        /* Printing */
        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_ORIENTATION_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PaperOrientation.values())
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperOrientation())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupPaperOrientation((PaperOrientation) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_SIZE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PaperSize.values())
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperSize())
            .setValueSetter((obj, value) -> {
                Plan plan = (Plan)obj;
                plan.setPageSetupPaperSize((PaperSize)value);
                if (!PaperSize.Custom.equals(value)) {
                    plan.setPageSetupPaperWidth(((PaperSize)value).getWidth());
                    plan.setPageSetupPaperHeight(((PaperSize)value).getHeight());
                }
            });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperWidth())
            .setValueSetter((plan, value) -> {
            if (value == null) return;

            ((Plan) plan).setPageSetupPaperWidth((int)value);
            ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
        });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_HEIGHT_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperHeight())
            .setValueSetter((plan, value) -> {
            if (value == null) return;

            ((Plan) plan).setPageSetupPaperHeight((int)value);
            ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
        });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_TOP_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperTopMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperTopMargin((int)value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_LEFT_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperLeftMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperLeftMargin((int)value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_RIGHT_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperRightMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperRightMargin((int)value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_BOTTOM_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperBottomMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperBottomMargin((int)value); });

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_RULERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().isRulersPrinted())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupRulersPrinted((boolean) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_GRID_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().isGridPrinted())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupGridPrinted((boolean) value));

        new ObjectProperty(this)
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_SCALE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PageSetup.PrintScale.values())
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPrintScale())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupPrintScale((PageSetup.PrintScale)value));
    }

    @Override
    public void refreshValues() {
        refreshLevelList();
        super.refreshValues();
    }

    public void refreshLevelList() {
        level.setAvailableValues(plan.getLevels().toArray());
        loadValues();
    }

    @Override
    protected void setPanelProperties() {
        propertiesManagerPanel.setInfoMessage(L10.get(L10.PLAN_PROPERTIES_INFO_MESSAGE));
        propertiesManagerPanel.setProperties(panelProperties);
    }

    private void modifyLights() {
        List<Light> lights = plan.getLights();
        String[] names = { L10.get(L10.LIGHT_TYPE), L10.get(L10.LIGHT_COLOR), L10.get(L10.LIGHT_CX), L10.get(L10.LIGHT_CY), L10.get(L10.LIGHT_CZ), L10.get(L10.LIGHT_DX), L10.get(L10.LIGHT_DY), L10.get(L10.LIGHT_DZ) };
        Class[] classes = { LightType.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class, Integer.class };
        boolean[] editable = { true, true, true, true, true, true, true, true };

        TableDialogSupport tableDialog = new TableDialogSupport(names, classes, editable) {
            @Override
            public void setupCustomColumns(JTable table) {
                // Light Type
                JComboBox<LightType> comboBox = new JComboBox<>();
                for (LightType lightType : LightType.values()) {
                    comboBox.addItem(lightType);
                }
                TableColumn lightTypeColumn = table.getColumnModel().getColumn(0);
                lightTypeColumn.setCellEditor(new DefaultCellEditor(comboBox));

                // Color Editor
                TableColumn colorColumn = table.getColumnModel().getColumn(1);
                CellEditorAdapter colorEditor = new CellEditorAdapter(new ColorPropertyEditor());
                colorColumn.setCellEditor(colorEditor);
                colorColumn.setCellRenderer(new ColorCellRenderer());
            }
        };

        for (Light light : lights) {
            tableDialog.addElement(light.getLightType(), light.getColor(),
                    light.getCx(), light.getCy(), light.getCz(),
                    light.getDx(), light.getDy(), light.getDz());
        }

        TableDialogPanel dialogPanel = new TableDialogPanel(plan, tableDialog,
                (entity, value) -> { return ((Plan)entity).validateLevels((TableDialogSupport)value); });
        DialogButton result = dialogPanel.displayView(L10.get(L10.PLAN_LIGHTS_PROPERTY), DialogButton.SAVE, DialogButton.CANCEL);
        if (DialogButton.SAVE.equals(result)) {
            plan.setLights(tableDialog);
        }
    }
}
