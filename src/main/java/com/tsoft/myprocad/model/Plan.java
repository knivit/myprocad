package com.tsoft.myprocad.model;

import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.model.property.PlanProperties;
import com.tsoft.myprocad.swing.dialog.TableDialogPanelSupport;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.StringUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.Plane;
import com.tsoft.myprocad.util.linealg.Seg2;
import com.tsoft.myprocad.util.linealg.Seg3;
import com.tsoft.myprocad.util.linealg.Vec2;
import com.tsoft.myprocad.util.linealg.Vec3;
import com.tsoft.myprocad.util.printer.PaperSize;
import com.tsoft.myprocad.viewcontroller.PasteOperation;
import com.tsoft.myprocad.viewcontroller.PlanController;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

public class Plan extends ProjectItem implements Cloneable {
    public static transient final float MINIMUM_SCALE = 0.01f;
    public static transient final float MAXIMUM_SCALE = 1f;

    private boolean gridVisible = true;
    private boolean rulersVisible = true;
    private float scale = 0.12f;
    private int originLocationId = CoordinatesOriginLocation.TOP_LEFT.getId();

    private WallList walls = new WallList();
    private ItemList<Beam> beams = new ItemList<>();
    private ItemList<DimensionLine> dimensionLines = new ItemList<>();
    private ItemList<Label> labels = new ItemList<>();
    private ItemList<LevelMark> levelMarks = new ItemList<>();

    private LevelList levels;
    private long levelId;

    /* For paste operations */
    private int pasteOffsetX = 500; // mm
    private int pasteOffsetY = 300;
    private int pasteOffsetZ = 0;
    private int pasteOperationId = PasteOperation.FIRST_SELECTED_PLUS_OFFSET.getId();

    private PageSetup pageSetup = new PageSetup();
    private String script;

    private transient PlanController planController;
    private transient CoordinatesOriginLocation originLocation;
    private transient Level level;
    private transient PasteOperation pasteOperation;

    Plan() {
        levels = new LevelList(this);
    }

    /** Invoked on Project loading or Project creation */
    public void afterOpen() {
        if (levels.isEmpty()) {
            Level customLevel = new Level(L10.get(L10.PLAN_CUSTOM_LEVEL_PROPERTY), Item.MIN_COORDINATE / 10, Item.MAX_COORDINATE / 10);
            customLevel.setId(getProject().generateNextId());
            levelId = customLevel.getId();
            levels.add(customLevel);
        }
    }

    public void beforeClose() { }

    @Override
    public PlanController getController() {
        if (planController == null) {
            planController = PlanController.createPlanController(this);
        }
        return planController;
    }

    @Override
    public void setName(String value) {
        if (ObjectUtil.equals(getName(), value)) return;
        super.setName(value);

        if (planController != null) planController.planNameChanged();
        if (getProject() != null) getProject().planChanged();
    }

    public int getPasteOffsetX() {
        return pasteOffsetX;
    }

    public void setPasteOffsetX(int value) {
        if (value == pasteOffsetX) return;
        pasteOffsetX = value;

        planController.planChanged(PlanProperties.PASTE_OFFSET_X, value);
        getProject().planChanged();
    }

    public int getPasteOffsetY() {
        return pasteOffsetY;
    }

    public void setPasteOffsetY(int value) {
        if (value == pasteOffsetY) return;
        pasteOffsetY = value;

        planController.planChanged(PlanProperties.PASTE_OFFSET_Y, value);
        getProject().planChanged();
    }

    public int getPasteOffsetZ() {
        return pasteOffsetZ;
    }

    public void setPasteOffsetZ(int value) {
        if (value == pasteOffsetZ) return;
        pasteOffsetZ = value;

        planController.planChanged(PlanProperties.PASTE_OFFSET_Z, value);
        getProject().planChanged();
    }

    public PasteOperation getPasteOperation() {
        if (pasteOperation == null) pasteOperation = PasteOperation.findById(pasteOperationId);
        return pasteOperation;
    }

    public void setPasteOperation(PasteOperation value) {
        if (ObjectUtil.equals(getPasteOperation(), value)) return;

        pasteOperationId = value.getId();
        pasteOperation = value;

        planController.planChanged(PlanProperties.PASTE_OPERATION, value);
        getProject().planChanged();
    }

    public boolean isGridVisible() {
        return gridVisible;
    }

    public void setGridVisible(boolean value) {
        if (value == this.gridVisible) return;
        gridVisible = value;

        planController.planChanged(PlanProperties.GRID_VISIBLE, value);
        getProject().planChanged();
    }

    public boolean isRulersVisible() {
        return rulersVisible;
    }

    public void setRulersVisible(boolean value) {
        if (value == this.rulersVisible) return;
        this.rulersVisible = value;

        planController.planChanged(PlanProperties.RULERS_VISIBLE, value);
        getProject().planChanged();
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float value) {
        value = Math.max(MINIMUM_SCALE, Math.min(value, MAXIMUM_SCALE));
        if (value == scale) return;
        scale = value;

        planController.planChanged(PlanProperties.SCALE, value);
        getProject().planChanged();
    }

    public CoordinatesOriginLocation getOriginLocation() {
        if (originLocation == null) originLocation = CoordinatesOriginLocation.findById(originLocationId);
        return originLocation;
    }

    public void undoItem(Item item) {
        if (item != null) itemChanged(item);
    }

    // Don't move this method to Item
    // because item's plan can be null (when inserting from a clipboard, for example)
    public void addItem(Item item) {
        if (item == null) return;

        getProject().setModified(true);
        item.plan = this;

        if (item instanceof Wall) walls.add((Wall) item);
        else if (item instanceof Beam) beams.add((Beam)item);
        else if (item instanceof DimensionLine) dimensionLines.add((DimensionLine) item);
        else if (item instanceof Label) labels.add((Label) item);
        else if (item instanceof LevelMark) levelMarks.add((LevelMark) item);
        else throw new IllegalArgumentException("Unknown item " + item.getClass().getName());

        planController.itemListChanged(CollectionEvent.Type.ADD, new ItemList<Item>(item));
    }

    public void deleteItem(Item item) {
        if (item == null) return;

        getProject().setModified(true);
        planController.deselectItem(item);

        if (item instanceof Wall) walls.deleteItem((Wall) item);
        else if (item instanceof Beam) beams.deleteItem((Beam) item);
        else if (item instanceof DimensionLine) dimensionLines.deleteItem((DimensionLine) item);
        else if (item instanceof Label) labels.deleteItem((Label) item);
        else if (item instanceof LevelMark) levelMarks.deleteItem((LevelMark) item);
        else throw new IllegalArgumentException("Unknown item " + item.getClass().getName());

        item.plan = null;
        planController.itemListChanged(CollectionEvent.Type.DELETE, new ItemList<Item>(item));
    }

    public void deleteItems(ItemList<Item> items) {
        items.forEach(this, this::deleteItem);
    }

    private void deleteItemsById(ItemList<Item> items) {
        Consumer<? super Item> action = this::deleteItem;

        planController.startBatchUpdate();
        try {
            ItemList<Wall> wl = items.getWallsSubList();
            wl.stream().forEach(item -> action.accept(walls.findById(item.getId())));

            ItemList<Beam> bl = items.getBeamsSubList();
            bl.stream().forEach(item -> action.accept(beams.findById(item.getId())));

            ItemList<DimensionLine> dl = items.getDimensionLinesSubList();
            dl.stream().forEach(item -> action.accept(dimensionLines.findById(item.getId())));

            ItemList<Label> ll = items.getLabelsSubList();
            ll.stream().forEach(item -> action.accept(labels.findById(item.getId())));

            ItemList<LevelMark> lm = items.getLevelMarksSubList();
            lm.stream().forEach(item -> action.accept(levelMarks.findById(item.getId())));

            if (items.size() != (wl.size() + dl.size() + ll.size() + lm.size())) {
                throw new IllegalArgumentException("Not all items were processed");
            }
        } finally {
            planController.stopBatchUpdate();
        }
    }

    public void itemChanged(Item item) {
        getProject().setModified(true);
        planController.itemChanged(item);
    }

    public void undoAddItems(ItemList<Item> items) {
        deleteItemsById(items);
    }

    public void undoDeleteItems(ItemList<Item> items) {
        items.forEach(this, this::addItem);
    }

    public ItemList<Wall> getWalls() {
        return walls.getCopy();
    }

    public Wall createWall(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd) {
        Wall wall = (Wall)ItemType.WALL.newInstance();
        wall.setXStart(xStart);
        wall.setXEnd(xEnd);
        wall.setYStart(yStart);
        wall.setYEnd(yEnd);
        wall.setZStart(Math.round(zStart));
        wall.setZEnd(Math.round(zEnd));
        wall.setMaterial(getProject().getMaterials().getDefault());
        return wall;
    }

    public Wall addWall(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd) {
        Wall wall = createWall(xStart, yStart, zStart, xEnd, yEnd, zEnd);
        addItem(wall);
        return wall;
    }

    public WallList findWallsWithMaterial(MaterialList materials) {
        WallList levelWalls = new WallList(getLevelWalls());
        return levelWalls.filterByMaterials(materials);
    }

    public WallList findWallsWithPattern(Pattern pattern) {
        WallList levelWalls = new WallList(getLevelWalls());
        return levelWalls.filterByPattern(pattern);
    }

    public WallList findWallsWithMaterial(Material material) {
        WallList levelWalls = new WallList(getLevelWalls());
        return levelWalls.filterByMaterial(material);
    }

    public ItemList<Beam> getBeams() {
        return beams.getCopy();
    }

    public Beam createBeam(float xStart, float yStart, int zStart, float xEnd, float yEnd, int zEnd, int width, int height) {
        Beam beam = (Beam)ItemType.BEAM.newInstance();
        beam.setXStart(xStart);
        beam.setYStart(yStart);
        beam.setZStart(zStart);
        beam.setXEnd(xEnd);
        beam.setYEnd(yEnd);
        beam.setZEnd(zEnd);
        beam.setWidth(width);
        beam.setHeight(height);
        beam.setMaterial(getProject().getMaterials().getDefault());
        return beam;
    }

    public Beam addBeam(float xStart, float yStart, int zStart, float xEnd, float yEnd, int zEnd, int width, int height) {
        Beam beam = createBeam(xStart, yStart, zStart, xEnd, yEnd, zEnd, width, height);
        addItem(beam);
        return beam;
    }

    /** Create a vertical beam which links two beams at their intersection point */
    public Beam addLink(Beam beam1, Beam beam2, int width, int height) {
        Seg3 bseg1 = beam1.getBeamCoreSegment();
        Seg3 bseg2 = beam2.getBeamCoreSegment();
        Seg2 seg1 = bseg1.get2dProjectionOnPlane(Plane.XOY);
        Seg2 seg2 = bseg2.get2dProjectionOnPlane(Plane.XOY);
        Vec2 intPt = seg1.getIntersectionPoint(seg2);
        float z1 = Math.min(bseg1.p0().z(), bseg1.p1().z());
        float z2 = Math.min(bseg2.p0().z(), bseg2.p1().z());
        float z = Math.min(z1, z2);
        Vec3 intPtZ0 = new Vec3(intPt.x(), intPt.y(), z);
        float db1 = bseg1.getDistanceToPoint(intPtZ0);
        float db2 = bseg2.getDistanceToPoint(intPtZ0);
        Beam link = addBeam(intPt.x(), intPt.y(), (int)(z + db1), intPt.x(), intPt.y(), (int)(z + db2), width, height);
        return link;
    }

    public ItemList<AbstractMaterialItem> getMaterialItems() {
        ItemList<AbstractMaterialItem> items = new ItemList<>();
        items.addAll(walls);
        items.addAll(beams);
        return items;
    }

    public ItemList<DimensionLine> getDimensionLines() {
        return dimensionLines.getCopy();
    }

    public DimensionLine createDimensionLine(float xStart, float xEnd, float yStart, float yEnd, int offset) {
        DimensionLine dimensionLine = (DimensionLine)ItemType.DIMENSION_LINE.newInstance();
        dimensionLine.setXStart(xStart);
        dimensionLine.setXEnd(xEnd);
        dimensionLine.setYStart(yStart);
        dimensionLine.setYEnd(yEnd);
        dimensionLine.setZStart(getLevel().getStart());
        dimensionLine.setZEnd(getLevel().getEnd());
        dimensionLine.setOffset(offset);

        addItem(dimensionLine);
        return dimensionLine;
    }

    public ItemList<Label> getLabels() {
        return labels.getCopy();
    }

    public Label createLabel(float x, float y, String text) {
        Label label = (Label)ItemType.LABEL.newInstance();
        label.setXStart(x);
        label.setXEnd(x + 1200);
        label.setYStart(y);
        label.setYEnd(y + 250);
        label.setZStart(getLevel().getStart());
        label.setZEnd(getLevel().getEnd());
        label.setText(text);

        addItem(label);
        return label;
    }

    public ItemList<LevelMark> getLevelMarks() {
        return levelMarks.getCopy();
    }

    public LevelMark createLevelMark(int xStart, int xEnd, int yStart, int yEnd) {
        LevelMark levelMark = (LevelMark)ItemType.LEVEL_MARK.newInstance();
        levelMark.setXStart(xStart);
        levelMark.setXEnd(xEnd);
        levelMark.setYStart(yStart);
        levelMark.setYEnd(yEnd);
        levelMark.setZStart(getLevel().getStart());
        levelMark.setZEnd(getLevel().getEnd());

        addItem(levelMark);
        return levelMark;
    }

    public WallList getLevelWalls() {
        return new WallList(getWalls().atLevel(this));
    }

    public ItemList<Beam> getLevelBeams() {
        return getBeams().atLevel(this);
    }

    public ItemList<DimensionLine> getLevelDimensionLines() {
        return getDimensionLines().atLevel(this);
    }

    public ItemList<Label> getLevelLabels() {
        return getLabels().atLevel(this);
    }

    public ItemList<LevelMark> getLevelLevelMarks() {
        return getLevelMarks().atLevel(this);
    }

    public ItemList<Item> getLevelItems() {
        return getLevelItems(getLevel());
    }

    public ItemList<Item> getLevelItems(Level level) {
        ItemList<Item> items = new ItemList<>();
        items.addAll(getWalls().atLevel(level));
        items.addAll(getBeams().atLevel(level));
        items.addAll(getDimensionLines().atLevel(level));
        items.addAll(getLabels().atLevel(level));
        items.addAll(getLevelMarks().atLevel(level));
        return items;
    }

    public void moveItemsToLevel(Level level, ItemList<Item> items) {
        int zMin = items.getZMin();
        items.forEach(this, e -> {
            int zStart = e.getZStart() - zMin + level.getStart();
            int dz = e.getZDistance();
            int zEnd = zStart + dz;
            if (zEnd > level.getEnd()) zEnd = level.getEnd();
            e.setZStart(zStart);
            e.setZEnd(zEnd);
        });
    }

    public Level getLevel() {
        if (level == null) level = levels.findById(levelId);
        return level;
    }

    public void setLevel(Level value) {
        if (ObjectUtil.equals(getLevel(), value)) return;

        levelId = value.getId();
        level = value;
        planController.planChanged(PlanProperties.LEVEL, level);
        getProject().setModified(true);
    }

    public LevelList getLevels() {
        return levels;
    }

    public void setLevels(LevelList levels) {
        this.levels = levels;

        planController.planChanged(PlanProperties.LEVEL, levels);
        getProject().setModified(true);
    }

    public PageSetup getPageSetup() {
        if (pageSetup == null) pageSetup = new PageSetup();
        return pageSetup;
    }

    public void setPageSetupRulersPrinted(boolean rulersPrinted) {
        if (pageSetup.isRulersPrinted() == rulersPrinted) return;
        pageSetup.setRulersPrinted(rulersPrinted);
        getProject().setModified(true);
    }

    public void setPageSetupPaperWidth(int paperWidth) {
        if (pageSetup.getPaperWidth() == paperWidth) return;
        pageSetup.setPaperWidth(paperWidth);
        getProject().setModified(true);
    }

    public void setPageSetupPaperHeight(int paperHeight) {
        if (pageSetup.getPaperHeight() == paperHeight) return;
        pageSetup.setPaperHeight(paperHeight);
        getProject().setModified(true);
    }

    public void setPageSetupPaperSize(PaperSize paperSize) {
        if (pageSetup.getPaperSize().equals(paperSize)) return;
        pageSetup.setPaperSize(paperSize);
        getProject().setModified(true);
    }

    public void setPageSetupPaperTopMargin(int paperTopMargin) {
        if (pageSetup.getPaperTopMargin() == paperTopMargin) return;
        pageSetup.setPaperTopMargin(paperTopMargin);
        getProject().setModified(true);
    }

    public void setPageSetupPaperLeftMargin(int paperLeftMargin) {
        if (pageSetup.getPaperLeftMargin() == paperLeftMargin) return;
        pageSetup.setPaperLeftMargin(paperLeftMargin);
        getProject().setModified(true);
    }

    public void setPageSetupPaperRightMargin(int paperRightMargin) {
        if (pageSetup.getPaperRightMargin() == paperRightMargin) return;
        pageSetup.setPaperRightMargin(paperRightMargin);
        getProject().setModified(true);
    }

    public void setPageSetupPaperBottomMargin(int paperBottomMargin) {
        if (pageSetup.getPaperBottomMargin() == paperBottomMargin) return;
        pageSetup.setPaperBottomMargin(paperBottomMargin);
        getProject().setModified(true);
    }

    public void setPageSetupGridPrinted(boolean gridPrinted) {
        if (pageSetup.isGridPrinted() == gridPrinted) return;
        pageSetup.setGridPrinted(gridPrinted);
        getProject().setModified(true);
    }

    public void setPageSetupPrintScale(PageSetup.PrintScale printScale) {
        if (pageSetup.getPrintScale().equals(printScale)) return;
        pageSetup.setPrintScale(printScale);
        getProject().setModified(true);
    }

    public void setPageSetupPaperOrientation(PaperOrientation paperOrientation) {
        if (pageSetup.getPaperOrientation().equals(paperOrientation)) return;
        pageSetup.setPaperOrientation(paperOrientation);
        getProject().setModified(true);
    }

    public Selection getSelection() {
        return new Selection(planController.getSelectedItems());
    }

    public String validateLevels(TableDialogPanelSupport<Level> levels) {
        if (levels.size() == 0) return L10.get(L10.LEVEL_AT_LEAST_ONE_MUST_EXIST);

        int row = 0;
        for (int i = 0; i < levels.size(); i ++) {
            Level level = levels.get(i);
            if (StringUtil.isEmpty(level.getName())) return L10.get(L10.LEVEL_NAME_CANT_BE_EMPTY, row);
            if (level.getStart() < Item.MIN_COORDINATE || level.getEnd() > Item.MAX_COORDINATE)
                return L10.get(L10.LEVEL_INVALID_Z_COORD, row, Item.MIN_COORDINATE, Item.MAX_COORDINATE);

            // check for duplicates
            boolean found = false;
            for (int k = i + 1; k < levels.size(); k ++) {
                if (level.equals(levels.get(k))) {
                    found = true;
                    break;
                }
            }

            if (found) return L10.get(L10.LEVEL_ALREADY_EXISTS, row);

            // don't remove a level with items
            ItemList<Item> items = getLevelItems(level);
            if (!items.isEmpty()) return L10.get(L10.CANT_REMOVE_LEVEL, row, level.toString(), items.size());

            row ++;
        }
        return null;
    }

    public String getScript() {
        return script;
    }

    public void setScript(String value) {
        if (ObjectUtil.equals(script, value)) return;
        this.script = value;
        getProject().setModified(true);
    }

    public void exportToObjFile(String fileName) {
        String userHome = System.getProperty("user.home") + File.separator;
        planController.exportToObjFile(userHome + fileName);
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("gridVisible", gridVisible)
                .write("rulersVisible", rulersVisible)
                .write("scale", scale)
                .write("originLocationId", originLocationId)
                .write("walls", walls)
                .write("beams", beams)
                .write("dimensionLines", dimensionLines)
                .write("labels", labels)
                .write("levelMarks", levelMarks)
                .write("levels", levels)
                .write("levelId", levelId)
                .write("pasteOffsetX", pasteOffsetX)
                .write("pasteOffsetY", pasteOffsetY)
                .write("pasteOffsetZ", pasteOffsetZ)
                .write("pasteOperationId", pasteOperationId)
                .write("pageSetup", pageSetup)
                .write("script", script);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        walls = new WallList();
        beams = new ItemList<>();
        dimensionLines = new ItemList<>();
        labels = new ItemList<>();
        levelMarks = new ItemList<>();
        levels = new LevelList(this);
        pageSetup = new PageSetup();

        try {
            super.fromJson(reader);
            reader
                    .defBoolean("gridVisible", ((value) -> gridVisible = value))
                    .defBoolean("rulersVisible", ((value) -> rulersVisible = value))
                    .defFloat("scale", ((value) -> scale = value))
                    .defInteger("originLocationId", ((value) -> originLocationId = value))
                    .defCollection("walls", Wall::new, ((value) -> walls.add((Wall) value)))
                    .defCollection("beams", Beam::new, ((value) -> beams.add((Beam) value)))
                    .defCollection("dimensionLines", DimensionLine::new, ((value) -> dimensionLines.add((DimensionLine) value)))
                    .defCollection("labels", Label::new, ((value) -> labels.add((Label) value)))
                    .defCollection("levelMarks", LevelMark::new, ((value) -> levelMarks.add((LevelMark) value)))
                    .defCollection("levels", Level::new, ((value) -> levels.add((Level) value)))
                    .defLong("levelId", ((value) -> levelId = value))
                    .defInteger("pasteOffsetX", ((value) -> pasteOffsetX = value))
                    .defInteger("pasteOffsetY", ((value) -> pasteOffsetY = value))
                    .defInteger("pasteOffsetZ", ((value) -> pasteOffsetZ = value))
                    .defInteger("pasteOperationId", ((value) -> pasteOperationId = value))
                    .defObject("pageSetup", pageSetup::fromJson)
                    .defString("script", ((value) -> script = value))
                    .read();
        } finally {
            // update references
            walls.stream().forEach(e -> e.setPlan(this));
            beams.stream().forEach(e -> e.setPlan(this));
            dimensionLines.stream().forEach(e -> e.setPlan(this));
            labels.stream().forEach(e -> e.setPlan(this));
            levelMarks.stream().forEach(e -> e.setPlan(this));
        }
    }
}
