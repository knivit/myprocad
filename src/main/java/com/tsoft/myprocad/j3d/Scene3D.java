package com.tsoft.myprocad.j3d;

import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;

import javax.media.j3d.Appearance;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.GeometryArray;
import javax.media.j3d.Material;
import javax.media.j3d.Shape3D;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import java.util.List;

public class Scene3D extends SceneBase {
    private BranchGroup group;

    public Scene3D() {
        super();

        group = new BranchGroup();
        setSceneGroup(group);
    }

    public void addItems(ItemList<AbstractMaterialItem> items) {
        for (AbstractMaterialItem item : items) addItem(item);
    }

    private void addItem(AbstractMaterialItem item) {
        List<Triangle3D> trigs = item.get3dTriangles();
        for (Triangle3D trig : trigs) {
            Point3f[] coordArray = trig.points;
            TexCoord2f texArray[] = null;

            GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
            //gi.setCoordinateIndices(groupIndices(coordIdxList, triList));
            gi.setCoordinates(coordArray);
            gi.setTextureCoordinateParams(1, 2);
            //gi.setTextureCoordinates(0, texArray);
            //gi.setTextureCoordinateIndices(0, groupIndices(texIdxList, triList));

            NormalGenerator ng = new NormalGenerator(0);
            ng.generateNormals(gi);

            Shape3D shape = new Shape3D();
            shape.setGeometry(gi.getGeometryArray(false, false, false));
            assignMaterial("sand_stone", shape);

            group.addChild(shape);
            //addNamedObject(curname, shape);
        }
    }

    private void assignMaterial(String matName, Shape3D shape) {
        Material m = new Material();
        Material3D p = DefaultMaterials.get(matName);
        Appearance a = new Appearance();

        if (p != null) {
            // Set ambient & diffuse color
            if (p.Ka != null) m.setAmbientColor(p.Ka);
            if (p.Kd != null) m.setDiffuseColor(p.Kd);

            // Set specular color
            if ((p.Ks != null) && (p.illum != 1)) m.setSpecularColor(p.Ks);
            else if (p.illum == 1) m.setSpecularColor(0.0f, 0.0f, 0.0f);

            if (p.illum >= 1) m.setLightingEnable(true);
            else if (p.illum == 0) m.setLightingEnable(false);

            if (p.Ns != -1.0f) m.setShininess(p.Ns);

            if (p.t != null) {
                a.setTexture(p.t);
                // Create Texture Coordinates if not already present
                if ((((GeometryArray)shape.getGeometry()).getVertexFormat() &
                GeometryArray.TEXTURE_COORDINATE_2) == 0) {
                    TexCoordGeneration tcg = new TexCoordGeneration();
                    a.setTexCoordGeneration(tcg);
                }
            }

            if (p.transparent) {
                TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, p.transparencyLevel);
                a.setTransparencyAttributes(ta);
            }
        }

        a.setMaterial(m);
        shape.setAppearance(a);
    }
}
