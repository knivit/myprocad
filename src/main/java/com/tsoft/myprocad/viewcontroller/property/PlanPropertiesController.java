package com.tsoft.myprocad.viewcontroller.property;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.Level;
import com.tsoft.myprocad.model.PageSetup;
import com.tsoft.myprocad.model.PaperOrientation;
import com.tsoft.myprocad.util.printer.PaperSize;
import com.tsoft.myprocad.viewcontroller.PasteOperation;
import com.tsoft.myprocad.model.Plan;
import com.tsoft.myprocad.model.property.ObjectProperty;
import com.l2fprod.common.beans.editor.ComboBoxPropertyEditor;

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

        ObjectProperty printPaperWidth = new ObjectProperty(this);
        printPaperWidth.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperWidth.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_WIDTH_PROPERTY));
        printPaperWidth.setType(Integer.class);
        printPaperWidth.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperWidth());
        printPaperWidth.setValueSetter((plan, value) -> {
            if (value == null) return;

            ((Plan) plan).setPageSetupPaperWidth((int)value);
            ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
        });

        ObjectProperty printPaperHeight = new ObjectProperty(this);
        printPaperHeight.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperHeight.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_HEIGHT_PROPERTY));
        printPaperHeight.setType(Integer.class);
        printPaperHeight.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperHeight());
        printPaperHeight.setValueSetter((plan, value) -> {
            if (value == null) return;

            ((Plan) plan).setPageSetupPaperHeight((int)value);
            ((Plan) plan).setPageSetupPaperSize(PaperSize.Custom);
        });

        ObjectProperty printPaperTopMargin = new ObjectProperty(this);
        printPaperTopMargin.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperTopMargin.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_TOP_MARGIN_PROPERTY));
        printPaperTopMargin.setType(Integer.class);
        printPaperTopMargin.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperTopMargin());
        printPaperTopMargin.setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperTopMargin((int)value); });

        ObjectProperty printPaperLeftMargin = new ObjectProperty(this);
        printPaperLeftMargin.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperLeftMargin.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_LEFT_MARGIN_PROPERTY));
        printPaperLeftMargin.setType(Integer.class);
        printPaperLeftMargin.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperLeftMargin());
        printPaperLeftMargin.setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperLeftMargin((int)value); });

        ObjectProperty printPaperRightMargin = new ObjectProperty(this);
        printPaperRightMargin.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperRightMargin.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_RIGHT_MARGIN_PROPERTY));
        printPaperRightMargin.setType(Integer.class);
        printPaperRightMargin.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperRightMargin());
        printPaperRightMargin.setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperRightMargin((int)value); });

        ObjectProperty printPaperBottomMargin= new ObjectProperty(this);
        printPaperBottomMargin.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printPaperBottomMargin.setLabelName(L10.get(L10.PLAN_PRINT_PAPER_BOTTOM_MARGIN_PROPERTY));
        printPaperBottomMargin.setType(Integer.class);
        printPaperBottomMargin.setValueGetter(plan -> ((Plan) plan).getPageSetup().getPaperBottomMargin());
        printPaperBottomMargin.setValueSetter((plan, value) -> { if (value != null) ((Plan) plan).setPageSetupPaperBottomMargin((int)value); });

        ObjectProperty printRulers = new ObjectProperty(this);
        printRulers.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printRulers.setLabelName(L10.get(L10.PLAN_PRINT_RULERS_PROPERTY));
        printRulers.setType(Boolean.class);
        printRulers.setValueGetter(plan -> ((Plan) plan).getPageSetup().isRulersPrinted());
        printRulers.setValueSetter((plan, value) -> ((Plan) plan).setPageSetupRulersPrinted((boolean) value));

        ObjectProperty printGrid = new ObjectProperty(this);
        printGrid.setCategoryName(L10.get(L10.PLAN_PRINT_CATEGORY));
        printGrid.setLabelName(L10.get(L10.PLAN_PRINT_GRID_PROPERTY));
        printGrid.setType(Boolean.class);
        printGrid.setValueGetter(plan -> ((Plan) plan).getPageSetup().isGridPrinted());
        printGrid.setValueSetter((plan, value) -> ((Plan) plan).setPageSetupGridPrinted((boolean) value));

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
}
