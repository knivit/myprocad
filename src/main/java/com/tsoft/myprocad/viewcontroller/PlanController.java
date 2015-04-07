package com.tsoft.myprocad.viewcontroller;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.*;
import com.tsoft.myprocad.report.MaterialReport;
import com.tsoft.myprocad.report.MaterialReportParams;
import com.tsoft.myprocad.swing.*;
import com.tsoft.myprocad.swing.dialog.*;
import com.tsoft.myprocad.swing.menu.Menu;
import com.tsoft.myprocad.swing.menu.PlanPanelMenu;
import com.tsoft.myprocad.swing.properties.PatternComboBoxRenderer;
import com.tsoft.myprocad.util.ContentManager;
import com.tsoft.myprocad.util.SwingTools;
import com.tsoft.myprocad.viewcontroller.component.*;
import com.tsoft.myprocad.viewcontroller.property.PlanPropertiesManager;

import java.awt.geom.Rectangle2D;
import java.io.*;

import com.tsoft.myprocad.model.property.PlanProperties;

import com.tsoft.myprocad.model.property.ListenedField;
import com.tsoft.myprocad.viewcontroller.state.ControllerState;
import com.tsoft.myprocad.viewcontroller.state.PanningState;
import com.tsoft.myprocad.viewcontroller.state.RectangleSelectionState;
import com.tsoft.myprocad.viewcontroller.state.SelectionMoveState;
import com.tsoft.myprocad.viewcontroller.state.SelectionState;

import javax.swing.JComponent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class PlanController implements ProjectItemController {
    public static class Mode {
        public static final Mode SELECTION = new Mode("SELECTION", L10.get(L10.STATUS_PANEL_SELECTION_MODE));
        public static final Mode PANNING = new Mode("PANNING", L10.get(L10.STATUS_PANEL_PANNING_MODE));
        public static final Mode WALL_CREATION = new Mode("WALL_CREATION", L10.get(L10.STATUS_PANEL_WALL_CREATION_MODE));
        public static final Mode BEAM_CREATION = new Mode("BEAM_CREATION", L10.get(L10.STATUS_PANEL_BEAM_CREATION_MODE));
        public static final Mode DIMENSION_LINE_CREATION = new Mode("DIMENSION_LINE_CREATION", L10.get(L10.STATUS_PANEL_DIMENSION_LINE_MODE));
        public static final Mode LABEL_CREATION = new Mode("LABEL_CREATION", L10.get(L10.STATUS_PANEL_LABEL_MODE));
        public static final Mode LEVEL_MARK_CREATION = new Mode("LEVEL_MARK_CREATION", L10.get(L10.STATUS_PANEL_LEVEL_MARK_MODE));

        private final String name;
        private final String localizedName;

        protected Mode(String name, String localizedName) {
            this.name = name;
            this.localizedName = localizedName;
        }

        public String name() {
            return name;
        }

        public String getLocalizedName() { return localizedName; }

        @Override
        public String toString() {
            return name;
        }
    }

    public static final int PIXEL_MARGIN = 4;
    public static final int INDICATOR_PIXEL_MARGIN = 4;

    private ProjectController projectController;
    private Project project;
    private Plan plan;
    private boolean isInitialized;

    public PlanPanel planPanel;
    public PlanPropertiesManager planPropertiesManager;

    private ItemList<Item> selectedItems = new ItemList<>();
    public History history = new History(128);

    // Possibles states
    private SelectionState selectionState;
    private ControllerState rectangleSelectionState;
    private ControllerState selectionMoveState;
    private ControllerState panningState;

    // Current state
    private ControllerState state;

    // Mouse cursor position at last mouse press
    private float xLastMousePress;
    private float yLastMousePress;
    private boolean shiftDownLastMousePress;
    private float xLastMouseMove;
    private float yLastMouseMove;

    // Sub-controllers
    public WallController wallController;
    public BeamController beamController;
    public DimensionLineController dimensionLineController;
    public LabelController labelController;
    public LevelMarkController levelMarkController;
    private LevelListController levelListController;

    public static PlanController createPlanController(Plan plan) {
        PlanController planController = new PlanController(plan);
        planController.planPanel = PlanPanel.createPlanPanel(planController);

        // PlanPanel is needed in sub-controllers
        planController.createSubControllers();

        return planController;
    }

    private PlanController(Plan plan) {
        this.plan = plan;
        this.project = plan.getProject();

        levelListController = new LevelListController(plan);
    }

    private void createSubControllers() {
        wallController = new WallController(this);
        beamController = new BeamController(this);
        dimensionLineController = new DimensionLineController(this);
        labelController = new LabelController(this);
        levelMarkController = new LevelMarkController(this);
        planPropertiesManager = PlanPropertiesManager.createPropertiesController(plan);
    }

    @Override
    public void setProjectController(ProjectController projectController) {
        this.projectController = projectController;
    }

    @Override
    public JComponent getParentComponent() {
        return planPanel.getParentComponent();
    }

    @Override
    public void afterOpen() {
        if (isInitialized) return;
        isInitialized = true;

        // Initialize states
        selectionState = new SelectionState(this);
        selectionMoveState = new SelectionMoveState(this);
        rectangleSelectionState = new RectangleSelectionState(this);
        panningState = new PanningState(this);

        // Set default state to selectionState
        setState(selectionState);
        setMode(Mode.SELECTION);

        planPanel.afterOpen(planPropertiesManager.propertiesManagerPanel);
        plan.afterOpen();

        // show Plan's properties
        planPropertiesManager.selectionChanged(selectedItems);
    }

    @Override
    public void beforeClose() {
        beforeDeactivation();
        plan.beforeClose();
    }

    @Override
    public void beforeDeactivation() {
        projectController.setStatusPanelVisible(false);
        PlanPanelMenu.setVisible(false);
    }

    @Override
    public void afterActivation() {
        projectController.setStatusPanelVisible(true);
        PlanPanelMenu.setVisible(true);
    }

    public Plan getPlan() { return plan; }

    public void setState(ControllerState state) {
        if (this.state != null) {
            this.state.exit();
        }

        this.state = state;
        state.enter();
    }

    public void startBatchUpdate() {
        planPropertiesManager.disableReload();
        planPanel.disableRevalidate();
    }

    public void stopBatchUpdate() {
        planPropertiesManager.enableReload();
        planPropertiesManager.selectionChanged(getSelectedItems());
        planPanel.enableRevalidate();
    }

    public void selectionChanged() {
        selectionState.selectionChanged(selectedItems);
        planPropertiesManager.selectionChanged(selectedItems);
        planPanel.selectionChanged();
    }

    public void selectionStateChanged() {
        planPropertiesManager.selectionStateChanged();
    }

    public void planNameChanged() {
        projectController.planNameChanged(plan);
    }

    public <T> void planChanged(ListenedField property, T newValue) {
        if (PlanProperties.SCALE.equals(property)) {
            int x = planPanel.convertXModelToScreen(getXLastMouseMove());
            int y = planPanel.convertXModelToScreen(getYLastMouseMove());
            planPanel.setScale((Float) newValue);

            // Update mouse location
            moveMouse(planPanel.convertXPixelToModel(x), planPanel.convertYPixelToModel(y));
        }

        planPropertiesManager.planChanged();
        planPanel.planChanged(property);
    }

    public void itemChanged(Item item) {
        planPropertiesManager.itemChanged(item);
        planPanel.itemChanged();
    }

    public void itemListChanged() {
        planPanel.itemListChanged();
    }

    public void materialListChanged() {
        planPropertiesManager.materialListChanged();
    }

    public Mode getMode() {
        return state.getMode();
    }

    public void setMode(Mode mode) {
        state.setMode(mode);
        projectController.modeChanged(mode.getLocalizedName());
    }

    public void zoomOut() {
        float newScale = getScale() / 1.2f;
        plan.setScale(newScale);
    }

    public void zoomIn() {
        float newScale = getScale() * 1.2f;
        plan.setScale(newScale);
    }

    private void delete() {
        deleteSelection();
        setMode(Mode.SELECTION);
    }

    private void rotate() {
        // find out the center of the selection
        Rectangle2D r2d = planPanel.getSelectionBounds();
        if (r2d != null) state.rotateSelection((int)Math.round(r2d.getCenterX()), (int)Math.round(r2d.getCenterY()));
    }

    private void splitInTwo() {
        ItemList<Item> newItems = new ItemList<>();
        ItemList<Item> oldItems = selectedItems.getCopy();

        for (Wall item : selectedItems.getWallsSubList()) newItems.add(splitInTwo(item));
        for (Beam item : selectedItems.getBeamsSubList()) newItems.add(splitInTwo(item));
        for (DimensionLine item : selectedItems.getDimensionLinesSubList()) newItems.add(splitInTwo(item));

        history.push(CollectionEvent.Type.ADD, newItems);
        history.push(oldItems);
    }

    private Item splitInTwo(Item originItem) {
        originItem.normalizeStartAndEnd();

        Item newItem = originItem.clone();
        plan.addItem(newItem);

        if (originItem.getXDistance() >= originItem.getYDistance()) {
            // split vertically
            int xs = originItem.getXStart() + originItem.getXDistance() / 2;
            newItem.setXStart(xs);
            originItem.setXEnd(xs);
        } else {
            // split horizontally
            int ys = originItem.getYStart() + originItem.getYDistance() / 2;
            newItem.setYStart(ys);
            originItem.setYEnd(ys);
        }
        return newItem;
    }

    private void generateScript() {
        ItemList<AbstractMaterialItem> items;
        if (selectedItems.isEmpty()) items = plan.getMaterialItems();
        else {
            items = new ItemList<>();
            items.addAll(selectedItems.getWallsSubList());
            items.addAll(selectedItems.getBeamsSubList());
        }

        StringBuilder buf = new StringBuilder();
        buf.append("// Walls\n");
        for (Wall wall : items.getWallsSubList()) {
            buf.append(String.format("plan.addWall(%d, %d, %d, %d, %d, %d).setPattern(\"%s\");\n",
                    wall.getXStart(), wall.getYStart(), wall.getZStart(),
                    wall.getXEnd(), wall.getYEnd(), wall.getZEnd(),
                    wall.getPattern().getResourceName()));
        }

        buf.append("// Beams\n");
        for (Beam beam : items.getBeamsSubList()) {
            buf.append(String.format("plan.addBeam(%d, %d, %d, %d, %d, %d, %d, %d).setPattern(\"%s\");\n",
                    beam.getXStart(), beam.getYStart(), beam.getZStart(),
                    beam.getXEnd(), beam.getYEnd(), beam.getZEnd(),
                    beam.getWidth(), beam.getHeight(),
                    beam.getPattern().getResourceName()));
        }

        TextDialog dialog = new TextDialog();
        dialog.setText(buf.toString());
        dialog.displayView(L10.get(L10.MENU_GENERATE_SCRIPT_NAME), DialogButton.CLOSE);
    }

    public void deleteSelection() {
        state.deleteSelection();
    }

    public void escape() {
        state.escape();
    }

    public void moveSelection(int dx, int dy) {
        state.moveSelection(dx, dy);
    }

    public void pressMouse(float x, float y, int clickCount, boolean shiftDown, boolean duplicationActivated) {
        // Store the last coordinates of a mouse press
        xLastMousePress = x;
        yLastMousePress = y;
        xLastMouseMove = x;
        yLastMouseMove = y;
        shiftDownLastMousePress = shiftDown;

        state.pressMouse(x, y, clickCount, shiftDown, duplicationActivated);
    }

    public void releaseMouse(float x, float y) {
        state.releaseMouse(x, y);
    }

    public void moveMouse(float x, float y) {
        // Store the last coordinates of a mouse move
        xLastMouseMove = x;
        yLastMouseMove = y;

        state.moveMouse(x, y);
    }

    public void zoom(float factor) {
        state.zoom(factor);
    }

    public ControllerState getSelectionState() {
        return selectionState;
    }

    public ControllerState getSelectionMoveState() {
        return selectionMoveState;
    }

    public ControllerState getRectangleSelectionState() {
        return rectangleSelectionState;
    }

    public ControllerState getPanningState() {
        return panningState;
    }

    public float getXLastMousePress() {
        return xLastMousePress;
    }

    public float getYLastMousePress() {
        return yLastMousePress;
    }

    public boolean wasShiftDownLastMousePress() {
        return shiftDownLastMousePress;
    }

    public float getXLastMouseMove() {
        return xLastMouseMove;
    }

    public float getYLastMouseMove() {
        return yLastMouseMove;
    }

    public float getScale() {
        return plan.getScale();
    }

    private void undo() {
        History.Operation op = history.pop();
        if (op != null) {
            op.undo(plan);
            selectionChanged();
        }
    }

    private void redo() { }

    private void cut() { copy(); delete(); }

    public void copy() {
        selectedItems.copyToClipboard();
    }

    public void paste() {
        ClipboardItems clipboard = ClipboardItems.readFromClipboard();
        if (clipboard == null || clipboard.getItems().isEmpty()) return;

        // check is pasted items have known materials
        WallList walls = new WallList(clipboard.getItems().getWallsSubList());
        MaterialList materials = clipboard.getMaterials();
        for (Wall wall : walls) {
            long materialId = wall.getMaterialId();
            Material material = materials.findById(materialId);
            if (material == null) {
                // don't throw the exception as it will be impossible to paste items before the code is fixed
                Exception ex = new IllegalStateException("Clipboard have walls with unknown materials, check " + ClipboardItems.class.getName());
                ex.printStackTrace();

                material = project.getMaterials().getDefault();
            }

            Material existingMaterial = project.getMaterials().findByName(material.getName());
            if (existingMaterial == null) {
                // project don't have such a material, add it
                Material newMaterial = material.clone();
                newMaterial.setId(project.generateNextId());
                project.getMaterials().add(newMaterial);
                wall.setMaterial(newMaterial);
            } else {
                // use project's material - ignore possible difference in material's attributes
                wall.setMaterial(existingMaterial);
            }
        }

        PasteOperation pasteOperation = plan.getPasteOperation();
        pasteOperation.paste(plan, clipboard.getItems());
        history.push(CollectionEvent.Type.ADD, clipboard.getItems());

        // clear previous selection and select the pasted items
        deselectAll();

        // switch to selection mode
        selectAndShowItems(clipboard.getItems());
        setMode(Mode.SELECTION);
    }

    /**
     * Returns the selected item at (<code>x</code>, <code>y</code>) point.
     */
    public boolean isItemSelectedAt(float x, float y) {
        float margin = PIXEL_MARGIN / getScale();
        Optional<Item> item = selectedItems.stream().filter(e -> e.containsPoint(plan, x, y, margin)).findFirst();
        return item.isPresent();
    }

    /** Returns items at the given location sorted by Z level */
    public ItemList<Item> getSelectableItemsAt(float x, float y) {
        ItemList<Item> items = plan.getLevelItems();
        items.sortByZLevel(false);

        float margin = PIXEL_MARGIN / getScale();
        ItemList<Item> result = new ItemList<>();
        for (Item item : items) {
            if (item.containsPoint(plan, x, y, margin)) {
                result.add(item);
            }
        }

        return result;
    }

    private void selectAll() {
        setSelectedItems(plan.getLevelItems());
    }

    private void selectWalls() {
        selectAndShowItems(new ItemList(plan.getLevelWalls()));
    }

    public List<String> getLevelMaterialsNames() {
        return ItemList.getMaterialsNames(plan.getLevelMaterialItems());
    }

    private void selectByMaterial() {
        List<String> levelMaterials = getLevelMaterialsNames();
        if (levelMaterials.isEmpty()) return;

        InputListElement<String> listElement = new InputListElement<>(L10.get(L10.MATERIAL_COLUMN_NAME), levelMaterials);
        InputDialogPanel inputDialogPanel = new InputDialogPanel(Arrays.asList(listElement));
        DialogButton result = inputDialogPanel.displayView(L10.get(L10.FIND_BY_MATERIAL_NAME), DialogButton.OK, DialogButton.CANCEL);
        if (!DialogButton.OK.equals(result)) return;

        MaterialList materials = projectController.findMaterialByName(listElement.getValue());
        WallList walls = plan.findWallsWithMaterial(materials);
        if (walls.isEmpty()) {
            SwingTools.showMessage(L10.get(L10.WALLS_NOT_FOUND));
            return;
        }

        selectAndShowItems(new ItemList(walls));
    }

    private void commandWindow() {
        planPanel.toggleCommandWindow();
    }

    private void selectByPattern() {
        List<Pattern> patterns = ItemList.getPatterns(plan.getLevelMaterialItems());
        if (patterns.isEmpty()) return;

        InputListElement<Pattern> listElement = new InputListElement<>(L10.get(L10.PATTERN_NAME), patterns);
        listElement.setRenderer(new PatternComboBoxRenderer());
        InputDialogPanel inputDialogPanel = new InputDialogPanel(Arrays.asList(listElement));
        DialogButton result = inputDialogPanel.displayView(L10.get(L10.FIND_BY_PATTERN), DialogButton.OK, DialogButton.CANCEL);
        if (!DialogButton.OK.equals(result)) return;

        WallList walls = plan.findWallsWithPattern(listElement.getValue());
        if (walls.isEmpty()) {
            SwingTools.showMessage(L10.get(L10.WALLS_NOT_FOUND));
            return;
        }

        selectAndShowItems(new ItemList(walls));
    }

    public void rotateSelectedItems(int ox, int oy) {
        if (selectedItems.isEmpty()) return;

        history.cloneAndPush(selectedItems);
        selectedItems.forEach(plan, e -> e.rotate(ox, oy, -90));

        selectionStateChanged();
    }

    /**
     * Moves and shows selected items in plan component of (<code>dx</code>,
     * <code>dy</code>) units and record it as undoable operation.
     */
    public void moveAndShowSelectedItems(int dx, int dy, int dz) {
        if (selectedItems.isEmpty()) return;

        ItemList<Item> movedItems = selectedItems.getCopy();
        movedItems.forEach(plan, e -> e.move(dx, dy, dz));

        selectAndShowItems(movedItems);
        selectionStateChanged();
    }

    public void selectAndShowItems(ItemList<Item> items) {
        selectItems(items);
        planPanel.makeSelectionVisible();
    }

    public ItemList<Item> getSelectedItems() {
        return selectedItems.getCopy();
    }

    public void setSelectedItems(ItemList<Item> value) {
        if (selectedItems.isSame(value)) return;

        selectedItems = value;
        selectionChanged();
    }

    public void deselectItem(Item item) {
        selectedItems.deleteItem(item);
        selectionChanged();
    }

    public void selectItems(ItemList<Item> items) {
        setSelectedItems(items);
    }

    public void selectItem(Item item) {
        ItemList<Item> list = new ItemList<Item>(item);
        selectItems(list);
    }

    public void deselectAll() {
        selectItems(new ItemList<>());
    }

    public void modifyLevels() {
        levelListController.edit();
    }

    private void printPlanToPDF() {
        String fileName = project.getFolders().size() > 1 ? String.format("%s_%s", project.getActiveFolder().name, plan.getName()) : plan.getName();
        fileName = fileName.replace(' ', '_');
        String pdfName = SwingTools.showSaveDialog(fileName, ContentManager.ContentType.PDF);
        if (pdfName == null) return;

        // Print to PDF in a threaded task
        ThreadedTaskController task = new ThreadedTaskController(L10.get(L10.PRINT_TO_PDF_MESSAGE));
        task.execute(() -> {
            PlanPDFPrinter pdfPrinter = new PlanPDFPrinter(plan, planPanel.getFont());
            try (OutputStream outputStream = new FileOutputStream(pdfName)) {
                pdfPrinter.write(outputStream, pdfName);
            }
            return null;
        });
    }

    private void printPreview() {
        PrintableComponent printableComponent = new PlanPrintableComponent(this, planPanel.getFont());
        new PrintPreviewController(printableComponent).displayView();
    }

    private void print() {
        ThreadedTaskController task = new ThreadedTaskController(L10.get(L10.PRINT_MESSAGE));
        task.execute(() -> {
            planPanel.showPrintDialog();
            return null;
        });
    }

    /**
     * Export to 3D Model OBJ file
     * See OBJ file format at http://en.wikipedia.org/wiki/Wavefront_.obj_file
     * Java code samples: git://github.com/sgothel/jogl-demos.git
     * Online 3D Obj Viewer: http://masc.cs.gmu.edu/wiki/GViewer
     */
    private void exportToObj() {
        String fileName = project.getFolders().size() > 1 ? String.format("%s_%s", project.getActiveFolder().name, plan.getName()) : plan.getName();
        fileName = fileName.replace(' ', '_');
        String objFileName = SwingTools.showSaveDialog(fileName, ContentManager.ContentType.OBJ);
        if (objFileName != null) exportToObjFile(objFileName);
    }

    public void exportToObjFile(String fileName) {
        int vno = 0; // vertex's no
        try (PrintWriter out = new PrintWriter(fileName)) {
            // Walls
            for (Wall wall : plan.getWalls()) {
                out.println(wall.toObjString(vno));
                vno += 8;
            }

            // Beams
            for (Beam beam : plan.getBeams()) {
                out.println(beam.toObjString(vno));
                vno += 8;
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            SwingTools.showError("Can't write to file '" + fileName + "'. " + ex.getMessage());
        }
    }

    private void exportToObjFile() {
        try {
            File objFile = File.createTempFile("3dScene", "obj");
            String objFileName = objFile.getAbsolutePath();
            exportToObjFile(objFileName);
        } catch (IOException ex) {
            ex.printStackTrace();
            SwingTools.showError(ex.getMessage());
        }
    }

    private void show3D() {
        ItemList<AbstractMaterialItem> items;
        if (selectedItems.isEmpty()) items = plan.getMaterialItems();
        else {
            items = new ItemList<>();
            items.addAll(selectedItems.getWallsSubList());
            items.addAll(selectedItems.getBeamsSubList());
        }

        J3dDialog j3d = new J3dDialog();
        j3d.addModelToUniverse(items);
        j3d.setVisible(true);
    }

    private void addPlan() { projectController.addProjectItem(); }

    private void deletePlan() {
        projectController.deleteProjectItem(plan);
    }

    private void materialsReport() {
        List<String> options = new ArrayList<>();
        for (MaterialReport report : MaterialReport.reports) {
            options.add(report.getLocalizedName());
        }
        String reportType = SwingTools.showInputDialog(L10.get(L10.SELECT_MATERIAL_REPORT), options, null);

        int index = options.indexOf(reportType);
        final MaterialReport report = MaterialReport.reports[index];
        final MaterialReportParams params = new MaterialReportParams(project, plan);

        String text = report.generate(params);
        ReportDialogPanel reportDialogPanel = new ReportDialogPanel(text);
        reportDialogPanel.displayView(report.getLocalizedName(), DialogButton.CLOSE);
    }

    @Override
    public boolean doMenuAction(Menu menu) {
        /* Plan */
        if (Menu.ADD_PLAN.equals(menu)) { addPlan(); return true; }
        if (Menu.DELETE_PLAN.equals(menu)) { deletePlan(); return true; }

        if (Menu.SHOW_PLAN_IN_3D.equals(menu)) { show3D(); return true; }
        if (Menu.PLAN_PRINT_TO_PDF.equals(menu)) { printPlanToPDF(); return true; }
        if (Menu.PLAN_PRINT_PREVIEW.equals(menu)) { printPreview(); return true; }
        if (Menu.PLAN_PRINT.equals(menu)) { print(); return true; }
        if (Menu.PLAN_EXPORT_TO_OBJ.equals(menu)) { exportToObj(); return true; }
        if (Menu.ZOOM_IN.equals(menu)) { zoomIn(); return true; }
        if (Menu.ZOOM_OUT.equals(menu)) { zoomOut(); return true; }

        if (Menu.UNDO.equals(menu)) { undo(); return true; }
        if (Menu.REDO.equals(menu)) { redo(); return true; }

        if (Menu.SELECT.equals(menu)) { setMode(PlanController.Mode.SELECTION); return true; }
        if (Menu.PAN.equals(menu)) { setMode(PlanController.Mode.PANNING); return true; }
        if (Menu.CREATE_WALLS.equals(menu)) { setMode(PlanController.Mode.WALL_CREATION); return true; }
        if (Menu.CREATE_BEAMS.equals(menu)) { setMode(PlanController.Mode.BEAM_CREATION); return true; }
        if (Menu.CREATE_DIMENSION_LINES.equals(menu)) { setMode(PlanController.Mode.DIMENSION_LINE_CREATION); return true; }
        if (Menu.CREATE_LABELS.equals(menu)) { setMode(PlanController.Mode.LABEL_CREATION); return true; }
        if (Menu.CREATE_LEVEL_MARKS.equals(menu)) { setMode(PlanController.Mode.LEVEL_MARK_CREATION); return true; }

        if (Menu.FIND_MATERIALS_USAGE.equals(menu)) { selectByMaterial(); return true; }
        if (Menu.COMMAND_WINDOW.equals(menu)) { commandWindow(); return true; }

        /* Selection */
        if (Menu.CUT.equals(menu)) { cut(); return true; }
        if (Menu.COPY.equals(menu)) { copy(); return true; }
        if (Menu.PASTE.equals(menu)) { paste(); return true; }
        if (Menu.DELETE.equals(menu)) { delete(); return true; }

        if (Menu.ROTATE_CLOCKWISE.equals(menu)) { rotate(); return true; }
        if (Menu.SPLIT_IN_TWO.equals(menu)) { splitInTwo(); return true; }

        if (Menu.GENERATE_SCRIPT.equals(menu)) { generateScript(); return true; }

        if (Menu.MOVE_SELECTION_LEFT.equals(menu)) { moveSelection(-1, 0); return true; }
        if (Menu.MOVE_SELECTION_UP.equals(menu)) { moveSelection(0, -1); return true; }
        if (Menu.MOVE_SELECTION_DOWN.equals(menu)) { moveSelection(0, 1); return true; }
        if (Menu.MOVE_SELECTION_RIGHT.equals(menu)) { moveSelection(1, 0); return true; }
        if (Menu.MOVE_SELECTION_FAST_LEFT.equals(menu)) { moveSelection(-10, 0); return true; }
        if (Menu.MOVE_SELECTION_FAST_UP.equals(menu)) { moveSelection(0, -10); return true; }
        if (Menu.MOVE_SELECTION_FAST_DOWN.equals(menu)) { moveSelection(0, 10); return true; }
        if (Menu.MOVE_SELECTION_FAST_RIGHT.equals(menu)) { moveSelection(10, 0); return true; }

        if (Menu.SELECT_BY_MATERIAL.equals(menu)) { selectByMaterial(); return true; }
        if (Menu.SELECT_BY_PATTERN.equals(menu)) { selectByPattern(); return true; }
        if (Menu.SELECT_ALL.equals(menu)) { selectAll(); return true; }
        if (Menu.SELECT_WALLS.equals(menu)) { selectWalls(); return true; }
        if (Menu.ESCAPE.equals(menu)) { escape(); return true; }

        /* Reports */
        if (Menu.MATERIALS_REPORT.equals(menu)) { materialsReport(); return true; }

        return false;
    }
}