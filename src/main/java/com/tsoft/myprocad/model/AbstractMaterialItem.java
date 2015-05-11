package com.tsoft.myprocad.model;

import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.tsoft.myprocad.l10n.L10;
import com.tsoft.myprocad.lib.mm.MMLib;
import com.tsoft.myprocad.model.calculation.BeamSag;
import com.tsoft.myprocad.model.calculation.Load1List;
import com.tsoft.myprocad.model.calculation.Load2List;
import com.tsoft.myprocad.swing.BeamPanel;
import com.tsoft.myprocad.util.ObjectUtil;
import com.tsoft.myprocad.util.json.JsonReader;
import com.tsoft.myprocad.util.json.JsonWriter;
import com.tsoft.myprocad.util.linealg.Vec3;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractMaterialItem extends Item {
    public static transient final int MAX_BORDER_WIDTH = 5;

    private long materialId;
    private Color backgroundColor = Color.WHITE;
    private Color foregroundColor = Color.BLACK;
    private Color borderColor = Color.BLACK;
    private int borderWidth = 1;
    private int patternId = Pattern.HATCH_UP.getId();

    // Сопротивление материалов
    // - для двутавровых балок
    private Double leftSupport = 0d;         // Левая опора, расстояние от начала балки, м
    private Double rightSupport = 0d;        // Правая опора, расстояние от конца балки, м
    private double elasticStrength = 200000; // Elastic Strength E, MPa (модуль упругости Юнга E (МПа))
                                             // 200000 - металл
                                             // 10000 - дерево
                                             // 14 - фанера 5-7-слойная, толщиной 5-8 мм и более
    private double allowableStress = 160;    // Allowable Stress, [σ] MPa (допускаемое напряжение [σ] (МПа))
                                             // 160 - металл
                                             // 10 - древесина на изгиб (7 - растяжение вдоль волокон, 10 - сжатие вдоль волокон)
    private List<Moment> moments = new ArrayList<>(); // Изгибающие моменты, kNm
    private List<Force> forces = new ArrayList<>();    // Точечные нагрузки, kN
    private List<DistributedForce> distributedForces = new ArrayList<>(); // Распределенные нагрузки, kN/m

    // - для перекрытий из дерева
    private double b = 600; // расстояние между балками, мм
    private Load1List permanentLoad = new Load1List(); // постоянная нагрузка (т.е. состав перекрытия)
    private Load2List temporaryLoad = new Load2List(); // временная нагрузка (люди, перегородки, снеговая и ветровая нагрузки)
    private int sagId = BeamSag.ATTIC_LAP.getId(); // элемент здания (для определения максимального прогиба)

    // 3D properties
    private boolean showWired;
    private Color Ka = new Color(50, 50, 50); // The ambient color reflected off the surface of the material
    private Color Kd = Color.WHITE; // The diffuse color of the material when illuminated
    private Color Ks = Color.GRAY; // specular color
    private Color Ke = Color.BLACK; // color of the light the material emits, if any
    private float shininess = 64.0f; // range [1.0, 128.0] with 1.0 being not shiny and 128.0 being very shiny
    private float transparency = 0.0f; // range [0.0, 1.0] with 0.0 being fully opaque and 1.0 being

    private transient Material material;
    private transient Pattern pattern;

    /* Inner props */
    public transient Vec3[] vertexes = new Vec3[8];

    public abstract double getLength();
    public abstract double getArea();
    public abstract double getVolume();

    public AbstractMaterialItem() {
        super();
    }

    public long getMaterialId() { return materialId; }

    public Material getMaterial() {
        if (material == null && plan != null) material = plan.getProject().getMaterials().findById(materialId);
        return material;
    }

    public AbstractMaterialItem setMaterial(Material value) {
        if (!ObjectUtil.equals(getMaterial(), value)) {
            materialId = value.getId();
            material = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public float getDensity() {
        Material material = getMaterial();
        if (material != null) return material.getDensity();
        return 1;
    }

    public Pattern getPattern() {
        if (pattern == null) pattern = Pattern.findById(patternId);
        return pattern;
    }

    public AbstractMaterialItem setPattern(Pattern value) {
        if (!ObjectUtil.equals(getPattern(), value)) {
            patternId = value.getId();
            pattern = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setPattern(String patternName) {
        Pattern pattern = Pattern.findByName(patternName);
        if (pattern == null) throw new IllegalArgumentException("Unknown pattern '" + patternName + "'");
        return setPattern(pattern);
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public AbstractMaterialItem setBackgroundColor(Color value) {
        if (!backgroundColor.equals(value)) {
            backgroundColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setBackgroundColor(int r, int g, int b) {
        return setBackgroundColor(new Color(r, g, b));
    }

    public Color getForegroundColor() {
        return foregroundColor;
    }

    public AbstractMaterialItem setForegroundColor(Color value) {
        if (!foregroundColor .equals(value)) {
            foregroundColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public AbstractMaterialItem setBorderColor(Color value) {
        if (!borderColor.equals(value)) {
            borderColor = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public int getBorderWidth() {
        return borderWidth;
    }

    public String validateBorderWidth(Integer value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0 || value > MAX_BORDER_WIDTH) return L10.get(L10.PROPERTY_MUST_BE_BETWEEN_VALUES, 0, MAX_BORDER_WIDTH);
        return null;
    }

    public AbstractMaterialItem setBorderWidth(int value) {
        if (borderWidth != value) {
            borderWidth = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public boolean getShowWired() {
        return showWired;
    }

    public AbstractMaterialItem setShowWired(boolean value) {
        if (showWired != value) {
            showWired = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }
    public Color getKaColor() {
        return Ka;
    }

    public AbstractMaterialItem setKaColor(Color value) {
        if (!Ka.equals(value)) {
            Ka = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setKaColor(int r, int g, int b) {
        return setKaColor(new Color(r, g, b));
    }

    public Color getKdColor() {
        return Kd;
    }

    public AbstractMaterialItem setKdColor(Color value) {
        if (!Kd.equals(value)) {
            Kd = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setKdColor(int r, int g, int b) {
        return setKdColor(new Color(r, g, b));
    }

    public Color getKsColor() {
        return Ks;
    }

    public AbstractMaterialItem setKsColor(Color value) {
        if (!Ks.equals(value)) {
            Ks = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setKsColor(int r, int g, int b) {
        return setKsColor(new Color(r, g, b));
    }

    public Color getKeColor() {
        return Ke;
    }

    public AbstractMaterialItem setKeColor(Color value) {
        if (!Ke.equals(value)) {
            Ke = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public AbstractMaterialItem setKeColor(int r, int g, int b) {
        return setKeColor(new Color(r, g, b));
    }

    public float getShininess() {
        return shininess;
    }

    public String validateShininess(Float value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 1.0f || value > 128.0f) return L10.get(L10.ITEM_INVALID_FLOAT_PROPERTY, 1.0f, 128.0f);
        return null;
    }

    public AbstractMaterialItem setShininess(float value) {
        if (shininess != value) {
            shininess = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public float getTransparency() {
        return transparency;
    }

    public String validateTransparency(Float value) {
        if (value == null) return L10.get(L10.PROPERTY_CANT_BE_EMPTY);
        if (value < 0f || value > 1f) return L10.get(L10.ITEM_INVALID_FLOAT_PROPERTY, 0f, 1f);
        return null;
    }

    public AbstractMaterialItem setTransparency(float value) {
        if (transparency != value) {
            transparency = value;
            if (plan != null) plan.itemChanged(this);
        }
        return this;
    }

    public double getWeight() {
        return getVolume() * getDensity();
    }

    public double getPrice() {
        Material material = getMaterial();
        if (material != null) return getVolume() * material.getPrice();
        return 0;
    }

    public String validateMechanicsProperties(double length) {
        if (leftSupport > length) leftSupport = length;
        if (rightSupport > length) rightSupport = length;

        moments.stream().filter(moment -> moment.zm >= length).forEach(moment -> moment.zm = length);
        forces.stream().filter(force -> force.zs >= length).forEach(force -> force.zs = length);
        distributedForces.stream().filter(distributedForce -> distributedForce.z2 >= length).forEach(distributedForce -> distributedForce.z2 = length);

        return null;
    }

    public Double getLeftSupport() {
        return leftSupport;
    }

    public String validateLeftSupport(Double leftSupport) {
        if (leftSupport == null) return null;
        if ((leftSupport < 0) || (leftSupport > getLength())) return "Левая опора должна быть на балке";
        return null;
    }

    public void setLeftSupport(Double value) {
        leftSupport = value;
    }

    public Double getRightSupport() {
        return rightSupport;
    }

    public String validateRightSupport(Double rightSupport) {
        if (rightSupport == null) return null;
        if (Math.abs(rightSupport) > getLength()) return "Правая опора должна быть на балке";
        return null;
    }

    public void setRightSupport(Double value) {
        rightSupport = value;
    }

    public double getElasticStrength() {
        return elasticStrength;
    }

    public void setElasticStrength(double value) {
        if (value <= 0.001) value = 0.001;
        elasticStrength = value;
    }

    public double getAllowableStress() {
        return allowableStress;
    }

    public void setAllowableStress(double value) {
        if (value <= 0.001) value = 0.001;
        allowableStress = value;
    }

    public List<Moment> getMoments() {
        return moments;
    }

    public void setMoments(List<Moment> value) {
        moments = value;
    }

    public String validateMoments(List<Moment> moments) {
        int row = 1;
        for (Moment moment : moments) {
            if (moment.vm == 0) return String.format("Строка %d. Момент равен нулю - не задаём его", row);
            if ((moment.zm < 0) || (moment.zm > getLength())) return String.format("Строка %d. Момент должен быть на балке", row);
            row ++;
        }
        return null;
    }

    public List<Force> getForces() {
        return forces;
    }

    public void setForces(List<Force> value) {
        forces = value;
    }

    public String validateForces(List<Force> forces) {
        int row = 1;
        for (Force force : forces) {
            if (force.vs == 0) return L10.get(L10.CALCULATION_BEAM_ERROR_FORCE_IS_ZERO, row);
            if ((force.zs < 0) || (force.zs > getLength())) return L10.get(L10.CALCULATION_BEAM_ERROR_FORCE_IS_OUT, row);
            row ++;
        }
        return null;
    }

    public List<DistributedForce> getDistributedForces() {
        return distributedForces;
    }

    public void setDistributedForces(List<DistributedForce> value) {
        distributedForces = value;
    }

    public String validateDistributedForces(List<DistributedForce> distributedForces) {
        int row = 1;
        for (DistributedForce df : distributedForces) {
            df.normalize();

            if ((df.z1 < 0) || (df.z1 > getLength())) return String.format("Строка %d. Распределённая нагрузка должна быть на балке", row);
            if ((df.z2 < 0) || (df.z2 > getLength())) return String.format("Строка %d. Распределённая нагрузка должна быть на балке", row);
            if (df.z1 == df.z2) return String.format("Строка %d. Интервал нулевой - не задаём нагрузку", row);
            if ((df.q1 == 0) && (df.q2 == 0)) return String.format("Строка %d. Распределённая нагрузка равна нулю - не задаём её", row);

            row ++;
        }
        return null;
    }

    public double getB() { return b; }

    public void setB(double b) {
        this.b = b;
    }

    /**
     * The coordinate system of the Java 3D virtual universe is right-handed. The x-axis is positive to the right,
     * y-axis is positive up, and z-axis is positive toward the viewer, with all units in meters
     *
     *         5-----------6
     *        / |         /|
     *    Y  /  |        / |
     *      2----------1   |
     *      |  4-------|--7
     *      |/         | /
     *      3----------0/    --> X
     *     /
     *   Z
     *
     * front face (0,1,2,3)
     * back face (4,5,6,7)
     * right face (7,6,1,0)
     * left face (3,2,5,4)
     * top face (1,6,5,2)
     * bottom face (3,4,7,0)
     *
     * scale - scale to [-1, 1]
     * oix, oy, oz - offsets to center (0, 0, 0)
     * showWıred - do not use materıal, show the figure as wıred lınes
     */
    public Shape3D getShape3D(float scale, int ox, int oy, int oz) {
        // MyProCAD and Java3D Y and Z axes differ
        int[] n = {
                7,3,0,4,
                5,1,2,6,
                6,2,3,7,
                4,0,1,5,
                3,2,1,0,
                4,5,6,7
        };

        float[] verts = new float[3*(4*6)];
        for (int i = 0; i < 4*6; i ++ ) {
            verts[i*3] = (vertexes[n[i]].x() - ox) / scale;
            verts[i*3 + 1] = (vertexes[n[i]].y() - oy) / scale;
            verts[i*3 + 2] = (vertexes[n[i]].z() - oz) / scale;
        }

        // http://www.cs.stir.ac.uk/courses/ITNP3B/Java3D/Tutorial/j3d_tutorial_ch6.pdf
        Geometry geom;
        Appearance app = new Appearance();
        if (showWired) {
            /**
             * Default values
             *
             * ColoringAttributes: color = white (1, 1, 1), shade model = SHADE_GOURAUD
             * LineAttributes: line width = 1.0, line pattern = PATTERN_SOLID, line antialiasing enable = false
             * PointAttributes: point size = 1.0, point antialiasing enable = false
             * PolygonAttributes: cull face = CULL_BACK, backFaceNormalFlip = false
             *                    polygon mode = POLYGON_FILL, polygonOffset = 0.0, polygonOffsetFactor = 0.0
             * RenderingAttributes: depthBufferEnable true, depthBufferWriteEnable true, alphaTestFunction ALWAYS
             *                      alphaTestValue 0.0, visible true, ignoreVertexColors false, rasterOpEnable false
             *                      rasterOp ROP_COPY
             * TextureAttributes: textureMode REPLACE, textureBlendColor black (0, 0, 0, 0), transform identity
             *                    perspectiveCorrectionMode NICEST, textureColorTable null
             * TransparencyAttributes: transparencyMode NONE, transparencyValue 0.0, srcBlendFunction SRC_BLEND_ALPHA
             *                    dstBlendFunction BLEND_ONE_MINUS_ALPHA
             */
            ColoringAttributes ca = new ColoringAttributes(new Color3f(borderColor), ColoringAttributes.SHADE_GOURAUD);
            app.setColoringAttributes(ca);

            PolygonAttributes pa = new PolygonAttributes();
            pa.setPolygonMode(PolygonAttributes.POLYGON_LINE);
            app.setPolygonAttributes(pa);

            LineAttributes la = new LineAttributes(borderWidth, LineAttributes.PATTERN_SOLID, true);
            app.setLineAttributes(la);

            QuadArray cube = new QuadArray(4*6, QuadArray.COORDINATES);
            cube.setCoordinates(0, verts);
            geom = cube;
        } else {
            javax.media.j3d.Material m3d = new javax.media.j3d.Material();
            m3d.setAmbientColor(new Color3f(Ka));
            m3d.setDiffuseColor(new Color3f(Kd));
            m3d.setEmissiveColor(new Color3f(Ke));
            m3d.setSpecularColor(new Color3f(Ks));
            m3d.setShininess(shininess);
            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, transparency);
            app.setTransparencyAttributes(ta);
            app.setMaterial(m3d);

            GeometryInfo gi = new GeometryInfo(GeometryInfo.QUAD_ARRAY);
            gi.setCoordinates(verts);
            NormalGenerator ng = new NormalGenerator();
            ng.generateNormals(gi);
            geom = gi.getGeometryArray();
        }

        Shape3D shape = new Shape3D();
        shape.setAppearance(app);
        shape.setGeometry(geom);
        return shape;
    }

    public String toObjString(int vno) {
        // For 3d modelling, the item's triangles
        int[][] faces = new int[][] {
                {1, 7, 5}, {1, 3, 7}, {1, 4, 3},
                {1, 2, 4}, {3, 8, 7}, {3, 4, 8},
                {5, 7, 8}, {5, 8, 6}, {1, 5, 6},
                {1, 6, 2}, {2, 6, 8}, {2, 8, 4}
        };

        // vertexes
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < 8; i ++) {
            buf.append("v " + vertexes[i].x() + " " + vertexes[i].y() + " " + vertexes[i].z()).append('\n');
        }

        // faces
        for (int i = 0; i < 12; i ++) {
            buf.append("f " + (vno + faces[i][0]) + " " + (vno + faces[i][1]) + " " + (vno + faces[i][2])).append('\n');
        }
        return buf.toString();
    }

    public boolean applyMechanicsSolution(BeamPanel beamPanel) {
        MMLib mm = new MMLib(this);
        String calculation = mm.calculate();
        if (calculation != null) {
            beamPanel.setText(calculation);
            for (BeamSolution solution : mm.solutions) beamPanel.addImage(solution.getImage());
            return true;
        }
        return false;
    }

    @Override
    public String getPopupItemName() {
        return getMaterial().getName();
    }

    @Override
    public void toJson(JsonWriter writer) throws IOException {
        super.toJson(writer);
        writer
                .write("materialId", materialId)
                .write("patternId", patternId)
                .write("backgroundColor", backgroundColor.getRGB())
                .write("foregroundColor", foregroundColor.getRGB())
                .write("borderColor", borderColor.getRGB())
                .write("borderWidth", borderWidth)

                .write("leftSupport", leftSupport)
                .write("rightSupport", rightSupport)
                .write("elasticStrength", elasticStrength)
                .write("allowableStress", allowableStress)
                .write("moments", moments)
                .write("forces", forces)
                .write("distributedForces", distributedForces)

                .write("showWired", showWired)
                .write("Ka", Ka.getRGB())
                .write("Kd", Kd.getRGB())
                .write("Ks", Ks.getRGB())
                .write("Ke", Ke.getRGB())
                .write("shininess", shininess)
                .write("transparency", transparency);
    }

    @Override
    public void fromJson(JsonReader reader) throws IOException {
        moments = new ArrayList();
        forces = new ArrayList<>();
        distributedForces = new ArrayList();

        super.fromJson(reader);
        reader
                .defLong("materialId", ((value) -> materialId = value))
                .defInteger("patternId", ((value) -> patternId = value))
                .defInteger("backgroundColor", ((value) -> backgroundColor = new Color(value)))
                .defInteger("foregroundColor", ((value) -> foregroundColor = new Color(value)))
                .defInteger("borderColor", ((value) -> borderColor = new Color(value)))
                .defInteger("borderWidth", ((value) -> borderWidth = value))

                .defDouble("leftSupport", ((value) -> leftSupport = value))
                .defDouble("rightSupport", ((value) -> rightSupport = value))
                .defDouble("elasticStrength", ((value) -> elasticStrength = value))
                .defDouble("allowableStress", ((value) -> allowableStress = value))
                .defCollection("moments", Moment::new, ((value) -> moments.add((Moment) value)))
                .defCollection("forces", Force::new, ((value) -> forces.add((Force) value)))
                .defCollection("distributedForces", DistributedForce::new, ((value) -> distributedForces.add((DistributedForce) value)))

                .defBoolean("showWired", ((value) -> showWired = value))
                .defInteger("Ka", ((value) -> Ka = new Color(value)))
                .defInteger("Kd", ((value) -> Kd = new Color(value)))
                .defInteger("Ks", ((value) -> Ks = new Color(value)))
                .defInteger("Ke", ((value) -> Ke = new Color(value)))
                .defFloat("shininess", ((value) -> shininess = value))
                .defFloat("transparency", ((value) -> transparency = value));
    }
}
