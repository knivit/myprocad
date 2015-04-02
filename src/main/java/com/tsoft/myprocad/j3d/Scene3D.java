package com.tsoft.myprocad.j3d;

import com.sun.j3d.loaders.SceneBase;
import com.sun.j3d.utils.geometry.GeometryInfo;
import com.sun.j3d.utils.geometry.NormalGenerator;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;

import javax.media.j3d.*;
import javax.vecmath.Point3f;
import javax.vecmath.TexCoord2f;
import java.util.List;

public class Scene3D extends SceneBase {
    private BranchGroup branchGroup;

    public Scene3D(BranchGroup branchGroup) {
        super();
        this.branchGroup = branchGroup;
    }

    public void addItems(ItemList<AbstractMaterialItem> items) {
        for (AbstractMaterialItem item : items) addItem(item);
    }

    private void addItem(AbstractMaterialItem item) {
        List<Triangle3D> trigs = item.get3dTriangles();
        for (Triangle3D trig : trigs) {
            Point3f[] coordArray = trig.points;
            TexCoord2f texArray[] = new TexCoord2f[] {
                    new TexCoord2f(1f, 0f), new TexCoord2f(0f, 0f), new TexCoord2f(0f, 1f),
            };

            GeometryInfo gi = new GeometryInfo(GeometryInfo.TRIANGLE_ARRAY);
            //gi.setCoordinateIndices(groupIndices(coordIdxList, triList));
            gi.setCoordinates(coordArray);
            gi.setTextureCoordinateParams(1, 2);
            gi.setTextureCoordinates(0, texArray);
            gi.recomputeIndices();
            //gi.setTextureCoordinateIndices(0, groupIndices(texIdxList, triList));

            NormalGenerator ng = new NormalGenerator();
            ng.generateNormals(gi);

            //TextureLoader loader = new TextureLoader(item.getPattern().getPatternImage(Color.WHITE.getRGB(), Color.BLACK.getRGB()));
            //ImageComponent2D image = loader.getImage();

            Shape3D shape = new Shape3D();
            shape.setGeometry(gi.getGeometryArray(false, false, false));
            //assignMaterial("plasma", shape);

            branchGroup.addChild(shape);
            //addNamedObject(curname, shape);
        }
    }
}
