package com.tsoft.myprocad.viewcontroller.property;

import com.l2fprod.common.beans.editor.ObjectListPropertyEditor;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.util.printer.PaperSize;
import com.tsoft.myprocad.viewcontroller.PasteOperation;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

public class PlanPropertiesController extends AbstractComponentPropertiesController<Plan> {
    private ObjectProperty level;

    public PlanPropertiesController(PlanPropertiesManager planPropertiesManager) {
        super(planPropertiesManager);
    }

    @Override
    protected void initObjectProperties() {
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_NAME_PROPERTY))
            .setType(String.class)
            .setValueGetter(plan1 -> ((Plan) plan1).getName())
            .setValueSetter((plan1, value) -> { if (value != null) ((Plan) plan1).setName((String) value); })
        );

        level = new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .addEditorButton(e -> plan.getController().modifyLevels())
            .setAvailableValues(plan.getLevels().toArray())
            .setValueGetter(plan -> ((Plan) plan).getLevel())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setLevel((Level) value); });
        addObjectProperty(level);

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_START_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getLevel().getStart())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LEVEL_END_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getLevel().getEnd())
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_LIGHTS_PROPERTY))
            .setType(ObjectListPropertyEditor.class)
            .setValueGetter(entity -> {
                LightTableDialogSupport support = new LightTableDialogSupport();
                support.setElements(((Plan) entity).getLights());
                return support;
            })
            .setValueValidator((plan, value) -> {
                LightTableDialogSupport support = (LightTableDialogSupport)value;
                return ((Plan) plan).validateLights(support.getElements());
            })
            .setValueSetter((plan, value) -> {
                LightTableDialogSupport support = (LightTableDialogSupport)value;
                ((Plan) plan).setLights(support.getElements());
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_RULERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).isRulersVisible())
            .setValueSetter((plan, value) -> ((Plan) plan).setRulersVisible((boolean) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_GRID_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).isGridVisible())
            .setValueSetter((plan, value) -> ((Plan) plan).setGridVisible((boolean) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_VIEW_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_SCALE_PROPERTY))
            .setType(Float.class)
            .setValueGetter(plan -> ((Plan) plan).getScale())
            .setValueValidator((plan, value) -> ((Plan)plan).validateScale((Float)value))
            .setValueSetter((plan, value) -> ((Plan) plan).setScale((float) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_X_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetX())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetX((int) value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_Y_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetY())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetY((int) value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OFFSET_Z_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPasteOffsetZ())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOffsetZ((int) value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PASTE_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PASTE_OPERATION_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PasteOperation.values())
            .setValueGetter(plan -> ((Plan) plan).getPasteOperation())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPasteOperation((PasteOperation) value); })
        );

        /* Printing */
        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_ORIENTATION_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PaperOrientation.values())
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperOrientation())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupPaperOrientation((PaperOrientation) value))
        );

        addObjectProperty(new ObjectProperty()
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
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_WIDTH_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperWidth())
            .setValueSetter((plan, value) -> {
                if (value == null) return;

                ((Plan) plan).setPageSetupPaperWidth((int)value);
                ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_HEIGHT_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperHeight())
            .setValueSetter((plan, value) -> {
                if (value == null) return;

                ((Plan) plan).setPageSetupPaperHeight((int)value);
                ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
            })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_TOP_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperTopMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperTopMargin((int)value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_LEFT_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperLeftMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperLeftMargin((int)value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_RIGHT_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperRightMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperRightMargin((int)value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_PAPER_BOTTOM_MARGIN_PROPERTY))
            .setType(Integer.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperBottomMargin())
            .setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperBottomMargin((int)value); })
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_RULERS_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().isRulersPrinted())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupRulersPrinted((boolean) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_GRID_PROPERTY))
            .setType(Boolean.class)
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().isGridPrinted())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupGridPrinted((boolean) value))
        );

        addObjectProperty(new ObjectProperty()
            .setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY))
            .setLabelName(L10.get(L10.PLAN_PRINT_SCALE_PROPERTY))
            .setType(ComboBoxPropertyEditor.class)
            .setAvailableValues(PageSetup.PrintScale.values())
            .setValueGetter(plan -> ((Plan) plan).getPageSetup().getPrintScale())
            .setValueSetter((plan, value) -> ((Plan) plan).setPageSetupPrintScale((PageSetup.PrintScale)value))
        );
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
}
