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

import java.awt.Color;
import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Consumer;

public class Plan extends ProjectItem implements Cloneable {
    public static transient final float MINIMUM_SCALE = 0.01f;
    public static transient final float MAXIMUM_SCALE = 1f;

    private boolean gridVisible = true;
    private boolean rulersVisible = true;
    private float scale = 0.12f;
    private int originLocationId = CoordinatesOriginLocation.TOP_LEFT.getId();

    private ItemList<Wall> walls = new ItemList<>();
    private ItemList<Beam> beams = new ItemList<>();
    private ItemList<DimensionLine> dimensionLines = new ItemList<>();
    private ItemList<Label> labels = new ItemList<>();
    private ItemList<LevelMark> levelMarks = new ItemList<>();
    private List<Light> lights = new ArrayList<>();

    private LevelList levels;
    private long levelId;

    /* For paste operations */
    private int pasteOffsetX = 0; // mm
    private int pasteOffsetY = 0;
    private int pasteOffsetZ = 0;
    private int pasteOperationId = PasteOperation.MOVE_TO.getId();

    private PageSetup pageSetup = new PageSetup();
    private String script;

    private transient PlanController planController;
    private transient CoordinatesOriginLocation originLocation;
    private transient Level level;
    private transient PasteOperation pasteOperation;

    /* Default settings for JavaScript commands */
    private transient String defaultPatternName;
    private transient Color defaultBackgroundColor;
    private transient Material defaultMaterial;
    private transient Color defaultKeColor;

    Plan() {
        levels = new LevelList(this);
    }

    /** Invoked on Project loading or Project creation */
    public void afterOpen() {
        if (levels.isEmpty()) {
            Level customLevel = new Level(L10.get(L10.PLAN_CUSTOM_LEVEL_PROPERTY), Item.MIN_COORDINATE, Item.MAX_COORDINATE);
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

    public String validateScale(Float value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < MINIMUM_SCALE || value > MAXIMUM_SCALE) return L10.get(L10.ITEM_INVALID_FLOAT_PROPERTY, MINIMUM_SCALE, MAXIMUM_SCALE);
        return null;
    }

    public void setScale(float value) {
        if (value != scale) {
            scale = value;

            planController.planChanged(PlanProperties.SCALE, value);
            getProject().planChanged();
        }
    }

    public CoordinatesOriginLocation getOriginLocation() {
        if (originLocation == null) originLocation = CoordinatesOriginLocation.findById(originLocationId);
        return originLocation;
    }

    public List<Light> getLights() {
        List<Light> ls = new ArrayList<>();
        for (Light li : lights) ls.add(li.getDeepClone());
        return ls;
    }

    public Light addLight(String lightTypeName) {
        Light light = new Light();
        light.setLightType(lightTypeName);
        lights.add(light);
        getProject().planChanged();
        return light;
    }

    public String validateLights(List<Light> lights) {
        for (Light light : lights) {
            if (light.getCx() < Item.MIN_COORDINATE || light.getCx() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
            if (light.getCy() < Item.MIN_COORDINATE || light.getCy() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
            if (light.getCz() < Item.MIN_COORDINATE || light.getCz() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
            if (light.getDx() < Item.MIN_COORDINATE || light.getDx() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
            if (light.getDy() < Item.MIN_COORDINATE || light.getDy() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
            if (light.getDz() < Item.MIN_COORDINATE || light.getDz() > Item.MAX_COORDINATE) return L10.get(L10.ITEM_INVALID_COORDINATE, Item.MIN_COORDINATE, Item.MAX_COORDINATE);
        }
        return null;
    }

    public void setLights(List<Light> lights) {
        if (this.lights.equals(lights)) return;
        this.lights = lights;
        getProject().planChanged();
    }

    public void undoItem(Item item) {
        if (item != null) itemChanged(item);
    }

    // Don't move this method to Item
    // because item's plan can be null (when inserting from a clipboard, for example)
    public void addItem(Item item) {
        if (item == null) return;

        getProject().planChanged();
        item.plan = this;

        if (item instanceof Wall) walls.add((Wall) item);
        else if (item instanceof Beam) beams.add((Beam)item);
        else if (item instanceof DimensionLine) dimensionLines.add((DimensionLine) item);
        else if (item instanceof Label) labels.add((Label) item);
        else if (item instanceof LevelMark) levelMarks.add((LevelMark) item);
        else throw new IllegalArgumentException("Unknown item " + item.getClass().getName());

        planController.itemListChanged();
    }

    public void deleteItem(Item item) {
        if (item == null) return;

        getProject().planChanged();
        planController.deselectItem(item);

        if (item instanceof Wall) walls.deleteItem((Wall) item);
        else if (item instanceof Beam) beams.deleteItem((Beam) item);
        else if (item instanceof DimensionLine) dimensionLines.deleteItem((DimensionLine) item);
        else if (item instanceof Label) labels.deleteItem((Label) item);
        else if (item instanceof LevelMark) levelMarks.deleteItem((LevelMark) item);
        else throw new IllegalArgumentException("Unknown item " + item.getClass().getName());

        item.plan = null;
        planController.itemListChanged();
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
        getProject().planChanged();
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

    private void populateItem(AbstractMaterialItem item, float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd) {
        item.setXStart(xStart);
        item.setXEnd(xEnd);
        item.setYStart(yStart);
        item.setYEnd(yEnd);
        item.setZStart(Math.round(zStart));
        item.setZEnd(Math.round(zEnd));

        if (defaultBackgroundColor != null) item.setBackgroundColor(defaultBackgroundColor);
        if (defaultPatternName != null) item.setPattern(defaultPatternName);
        if (defaultMaterial != null) item.setMaterial(defaultMaterial);
        else item.setMaterial(getProject().getMaterials().getDefault());
        if (defaultKeColor != null) item.setKeColor(defaultKeColor);
    }

    public Wall createWall(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd) {
        Wall wall = (Wall)ItemType.WALL.newInstance();
        populateItem(wall, xStart, yStart, zStart, xEnd, yEnd, zEnd);
        return wall;
    }

    public Wall addWall(float xStart, float yStart, float zStart, float xEnd, float yEnd, float zEnd) {
        Wall wall = createWall(xStart, yStart, zStart, xEnd, yEnd, zEnd);
        addItem(wall);
        return wall;
    }

    public ItemList<Beam> getBeams() {
        return beams.getCopy();
    }

    public Beam createBeam(float xStart, float yStart, int zStart, float xEnd, float yEnd, int zEnd, int width, int height) {
        Beam beam = (Beam)ItemType.BEAM.newInstance();
        populateItem(beam, xStart, yStart, zStart, xEnd, yEnd, zEnd);
        beam.setWidth(width);
        beam.setHeight(height);
        return beam;
    }

    public Beam addBeam(float xStart, float yStart, int zStart, float xEnd, float yEnd, int zEnd, int width, int height) {
        Beam beam = createBeam(xStart, yStart, zStart, xEnd, yEnd, zEnd, width, height);
        addItem(beam);
        return beam;
    }

    public Beam addBeam(Vec3 p0, Vec3 p1, int w, int h) {
        return addBeam(p0.x(), p0.y(), (int) p0.z(), p1.x(), p1.y(), (int) p1.z(), w, h);
    }

    /** Create a vertical beam which links two beams at their intersection point */
    public Beam addCrossBeam(Beam beam1, Beam beam2, int width, int height) {
        Seg3 bseg1 = beam1.getSegment();
        Seg3 bseg2 = beam2.getSegment();
        Seg2 seg1 = bseg1.get2dProjectionOnPlane(Plane.XOY);
        Seg2 seg2 = bseg2.get2dProjectionOnPlane(Plane.XOY);
        Vec2 intPt = seg1.getIntersectionPoint(seg2);
        float z1 = Math.min(bseg1.p0().z(), bseg1.p1().z());
        float z2 = Math.min(bseg2.p0().z(), bseg2.p1().z());
        float z = Math.min(z1, z2);
        Vec3 intPtZ0 = new Vec3(intPt.x(), intPt.y(), z);
        float db1 = bseg1.getDistanceToPoint(intPtZ0);
        float db2 = bseg2.getDistanceToPoint(intPtZ0);

        Beam result = addBeam(intPt.x(), intPt.y(), (int) (z + db1), intPt.x(), intPt.y(), (int) (z + db2), width, height);
        return result;
    }

    /** Create a vertical beam which connects two beams
     * at the given distance from the beam1 start point
     *
     * Return null if the distance is behind Beam's #1 end
     */
    public Beam addConnectBeam(Beam beam1, float distance, Beam beam2, int width, int height) {
        Seg3 bseg1 = beam1.getSegment();
        Vec3 p0 = bseg1.getPointOnSeg(distance);
        if (p0 == null) return null;

        Seg3 bseg2 = beam2.getSegment();
        float dist = bseg2.getDistanceToPoint(p0);

        Beam result = addBeam(p0.x(), p0.y(), (int) (p0.z()), p0.x(), p0.y(), (int) (p0.z() + dist), width, height);
        return result;
    }

    /** Create a beam from the given start point to the given beam keeping the same X */
    public Beam addConnectXBeam(float xs, float ys, float zs, Beam beam, int w, int h) {
        Vec3 p0 = new Vec3(xs, ys, zs);
        Plane xyz = new Plane(p0, new Vec3(xs, ys + 1, zs), new Vec3(xs, ys, zs + 1));
        Vec3 p1 = new Vec3();
        Seg3 bseg = beam.getSegment();
        int res = xyz.intersectSegment(bseg, p1);
        if (res == 0) return null;
        if (res == 1) return addBeam(p0, p1, w, h);

        // the beam lies in the same plane, find out it's nearest vertex and connect to it
        Seg3 s0 = new Seg3(p0, bseg.p0());
        Seg3 s1 = new Seg3(p0, bseg.p1());
        if (s0.getLength() < s1.getLength()) return addBeam(p0, bseg.p0(), w, h);
        return addBeam(p0, bseg.p1(), w, h);
    }

    /** apertures are given as (xs, ys, xe, ye), i.e. zs and ze must not be defined
     * Can't return a wall, as it will be one of many
     * So, use setDefault... to operate their props
     */
    public void addWallWithAperturesXY(float xs, float ys, float zs, float xe, float ye, float ze, float ... apertures) {
        addWallWithApertures(0, xs, ys, zs, xe, ye, ze, apertures);
    }

    public void addWallWithAperturesXZ(float xs, float ys, float zs, float xe, float ye, float ze, float ... apertures) {
        addWallWithApertures(1, xs, ys, zs, xe, ye, ze, apertures);
    }

    public void addWallWithAperturesYZ(float xs, float ys, float zs, float xe, float ye, float ze, float ... apertures) {
        addWallWithApertures(2, xs, ys, zs, xe, ye, ze, apertures);
    }

    private void addWallWithApertures(int mode, float xs, float ys, float zs, float xe, float ye, float ze, float ... apertures) {
        List<Wall> walls = new ArrayList<>();
        Wall baseWall = createWall(xs, ys, zs, xe, ye, ze);
        walls.add(baseWall);
        if (apertures != null) {
            for (int i = 0; i < apertures.length; i += 4) {
                float axs = apertures[i];
                float ays = apertures[i + 1];
                float axe = apertures[i + 2];
                float aye = apertures[i + 3];
                if (mode == 0) walls = addApertureXY(walls, axs, ays, axe, aye); else
                if (mode == 1) walls = addApertureXZ(walls, axs, ays, axe, aye);
                else walls = addApertureYZ(walls, axs, ays, axe, aye);
            }
        }

        for (Wall wall : walls) addItem(wall);
    }

    private List<Wall> addApertureXY(List<Wall> walls, float xs, float ys, float xe, float ye) {
        List<Wall> inters = new ArrayList<>();
        for (Wall wall : walls) {
            boolean isxs = (wall.getXStart() < xs && wall.getXEnd() > xs);
            boolean isxe = (wall.getXStart() < xe && wall.getXEnd() > xe);
            boolean isys = (wall.getYStart() < ys && wall.getYEnd() > ys);
            boolean isye = (wall.getYStart() < ye && wall.getYEnd() > ye);

            // [ * ] *
            //   *   *
            if (isxs && !isxe && isys && !isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                continue;
            }

            //   * [ * ]
            //   *   *
            if (!isxs && isxe && isys && !isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xe, ye, wall.getZEnd()));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ * ] *
            if (isxs && !isxe && !isys && isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            //   * [ * ]
            if (!isxs && isxe && !isys && isye) {
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), xe, wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            //   *   *
            if (isxs && isxe && isys && !isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, wall.getYStart(), wall.getZStart(), xe, ys, wall.getZEnd()));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ *   * ]
            if (isxs && isxe && !isys && isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, ye, wall.getZStart(), xe, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ * ] *
            // [ * ] *
            if (isxs && !isxe && isys && isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                inters.add(createWall(xe, ys, wall.getZStart(), wall.getXEnd(), ye, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   * [ * ]
            //   * [ * ]
            if (!isxs && isxe && isys && isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ys, wall.getZStart(), xe, ye, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            // [ *   * ]
            if (isxs && isxe && isys && isye) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ys, wall.getZStart(), xs, ye, wall.getZEnd()));
                inters.add(createWall(xe, ys, wall.getZStart(), wall.getXEnd(), ye, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // the aperture misses the wall, add the wall
            inters.add(wall);
        }
        return inters;
    }

    private List<Wall> addApertureXZ(List<Wall> walls, float xs, float zs, float xe, float ze) {
        List<Wall> inters = new ArrayList<>();
        for (Wall wall : walls) {
            boolean isxs = (wall.getXStart() <= xs && wall.getXEnd() >= xs);
            boolean isxe = (wall.getXStart() <= xe && wall.getXEnd() >= xe);
            boolean iszs = (wall.getZStart() <= zs && wall.getZEnd() >= zs);
            boolean isze = (wall.getZStart() <= ze && wall.getZEnd() >= ze);

            // [ * ] *
            //   *   *
            if (isxs && !isxe && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                continue;
            }

            //   * [ * ]
            //   *   *
            if (!isxs && isxe && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xe, wall.getYEnd(), ze));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ * ] *
            if (isxs && !isxe && !iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xs, wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            //   * [ * ]
            if (!isxs && isxe && !iszs && isze) {
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, xe, wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            //   *   *
            if (isxs && isxe && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                if (!eqRounded(xs, xe)) inters.add(createWall(xs, wall.getYStart(), wall.getZStart(), xe, wall.getYEnd(), zs));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ *   * ]
            if (isxs && isxe && !iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), xs, wall.getYEnd(), wall.getZEnd()));
                if (!eqRounded(xs, xe)) inters.add(createWall(xs, wall.getYStart(), ze, xe, wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(xe, wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ * ] *
            // [ * ] *
            if (isxs && !isxe && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(xe, wall.getYStart(), zs, wall.getXEnd(), wall.getYEnd(), ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   * [ * ]
            //   * [ * ]
            if (!isxs && isxe && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), wall.getYStart(), zs, xe, wall.getYEnd(), ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            // [ *   * ]
            if (isxs && isxe && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), wall.getYStart(), zs, xs, wall.getYEnd(), ze));
                if (!eqRounded(zs, ze)) inters.add(createWall(xe, wall.getYStart(), zs, wall.getXEnd(), wall.getYEnd(), ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // the aperture misses the wall, add the wall
            inters.add(wall);
        }
        return inters;
    }

    private List<Wall> addApertureYZ(List<Wall> walls, float ys, float zs, float ye, float ze) {
        List<Wall> inters = new ArrayList<>();
        for (Wall wall : walls) {
            boolean isys = (wall.getYStart() <= ys && wall.getYEnd() >= ys);
            boolean isye = (wall.getYStart() <= ye && wall.getYEnd() >= ye);
            boolean iszs = (wall.getZStart() <= zs && wall.getZEnd() >= zs);
            boolean isze = (wall.getZStart() <= ze && wall.getZEnd() >= ze);

            // [ * ] *
            //   *   *
            if (isys && !isye && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ys, zs, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   * [ * ]
            //   *   *
            if (!isys && isye && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ye, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ * ] *
            if (isys && !isye && !iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ys, ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            //   * [ * ]
            if (!isys && isye && !iszs && isze) {
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), ye, wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            //   *   *
            if (isys && isye && iszs && !isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                if (!eqRounded(ys, ye)) inters.add(createWall(wall.getXStart(), ys, wall.getZStart(), wall.getXEnd(), ye, zs));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   *   *
            // [ *   * ]
            if (isys && isye && !iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), ys, wall.getZEnd()));
                if (!eqRounded(ys, ye)) inters.add(createWall(wall.getXStart(), ys, ze, wall.getXEnd(), ye, wall.getZEnd()));
                inters.add(createWall(wall.getXStart(), ye, wall.getZStart(), wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ * ] *
            // [ * ] *
            if (isys && !isye && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), ye, zs, wall.getXEnd(), wall.getYEnd(), ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            //   * [ * ]
            //   * [ * ]
            if (!isys && isye && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), wall.getYStart(), zs, wall.getXEnd(), ye, ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // [ *   * ]
            // [ *   * ]
            if (isys && isye && iszs && isze) {
                inters.add(createWall(wall.getXStart(), wall.getYStart(), wall.getZStart(), wall.getXEnd(), wall.getYEnd(), zs));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), wall.getYStart(), zs, wall.getXEnd(), ys, ze));
                if (!eqRounded(zs, ze)) inters.add(createWall(wall.getXStart(), ye, zs, wall.getXEnd(), wall.getYEnd(), ze));
                inters.add(createWall(wall.getXStart(), wall.getYStart(), ze, wall.getXEnd(), wall.getYEnd(), wall.getZEnd()));
                continue;
            }

            // the aperture misses the wall, add the wall
            inters.add(wall);
        }
        return inters;
    }

    private boolean eqRounded(float a, float b) {
        return Math.round(a) == Math.round(b);
    }

    public ItemList<AbstractMaterialItem> getMaterialItems() {
        ItemList<AbstractMaterialItem> items = new ItemList<>();
        items.addAll(walls);
        items.addAll(beams);
        return items;
    }

    public ItemList<AbstractMaterialItem> getLevelMaterialItems() {
        ItemList<AbstractMaterialItem> items = new ItemList<>();
        items.addAll(walls.atLevel(this));
        items.addAll(beams.atLevel(this));
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

    public ItemList<Wall> getLevelWalls() {
        return getWalls().atLevel(this);
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
        getProject().planChanged();
    }

    public LevelList getLevels() {
        return levels;
    }

    public void setLevels(LevelList levels) {
        if (this.levels.equals(levels)) return;

        this.levels = levels;
        planController.planChanged(PlanProperties.LEVEL, levels);
        getProject().planChanged();
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
        getProject().planChanged();
    }

    public void setPageSetupGridPrinted(boolean gridPrinted) {
        if (pageSetup.isGridPrinted() == gridPrinted) return;
        pageSetup.setGridPrinted(gridPrinted);
        getProject().planChanged();
    }

    public void setPageSetupPrintScale(PageSetup.PrintScale printScale) {
        if (pageSetup.getPrintScale().equals(printScale)) return;
        pageSetup.setPrintScale(printScale);
        getProject().planChanged();
    }

    public void setPageSetupPaperOrientation(PaperOrientation paperOrientation) {
        if (pageSetup.getPaperOrientation().equals(paperOrientation)) return;
        pageSetup.setPaperOrientation(paperOrientation);
        getProject().planChanged();
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
        getProject().planChanged();
    }

    /* Methods invoked from JavaScript. Do not delete them. */
    public Plan setDefaultPattern(String patternName) {
        defaultPatternName = patternName;
        return this;
    }

    public Plan setDefaultBackgroundColor(int r, int g, int b) {
        defaultBackgroundColor = new Color(r, g, b);
        return this;
    }

    public Plan setDefaultMaterial(Material material) {
        defaultMaterial = material;
        return this;
    }

    public Plan setDefaultKeColor(int r, int g, int b) {
        defaultKeColor = new Color(r, g, b);
        return this;
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
                .write("lights", lights)
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
        walls = new ItemList();
        beams = new ItemList<>();
        dimensionLines = new ItemList<>();
        labels = new ItemList<>();
        levelMarks = new ItemList<>();
        lights = new ArrayList<>();
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
                    .defCollection("lights", Light::new, ((value) -> lights.add((Light) value)))
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
