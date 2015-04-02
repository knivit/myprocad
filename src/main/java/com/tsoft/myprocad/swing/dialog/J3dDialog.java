package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.j3d.DefaultMaterials;
import com.tsoft.myprocad.j3d.Material3D;
import com.tsoft.myprocad.j3d.Scene3D;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3d;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

/**
 * From http://www.daltonfilho.com/articles/java3d/SimpleModelView.html
 * http://www.java3d.org/selection.html
 * https://java3d.java.net/
 */
public class J3dDialog extends AbstractDialogPanel {
    private Canvas3D canvas;
    private SimpleUniverse universe;

    public J3dDialog() {
        super(new BorderLayout());

        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        add(canvas, BorderLayout.CENTER);

        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    public void addModelToUniverse(ItemList<AbstractMaterialItem> items) throws IOException {
        BranchGroup branchGroup = new BranchGroup();

        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        branchGroup.addChild(transformGroup);

        //Appearance appearance = new Appearance();
        //appearance.setMaterial();
       // URL url = MyProCAD.class.getResource("resources/patterns/crossHatch.png");
       // Texture texture = new TextureLoader(url, TextureLoader.BY_REFERENCE | TextureLoader.Y_UP, this).getTexture();
        //appearance.setTexture(texture);
        //TextureAttributes texAttr = new TextureAttributes();
        //texAttr.setTextureMode(TextureAttributes.MODULATE);
       // appearance.setTextureAttributes(texAttr);

        Appearance appearance = DefaultMaterials.get("plasma").getAppearence(Pattern.CROSS_HATCH);
        Box textureCube = new Box(0.4f, 0.4f, 0.4f, Box.GENERATE_TEXTURE_COORDS | Box.GENERATE_TEXTURE_COORDS_Y_UP, appearance);
        transformGroup.addChild(textureCube);

        MouseRotate mouseRotate = new MouseRotate(transformGroup);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        mouseRotate.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseRotate);

        branchGroup.compile();

        universe.addBranchGraph(branchGroup);

/*        Scene3D scene = new Scene3D(branchGroup);
        scene.addItems(items);

        addLightsToUniverse(branchGroup);

        MouseRotate mouseRotate = new MouseRotate(transformGroup);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        mouseRotate.setSchedulingBounds(bounds);

        MouseWheelZoom mouseWheelZoom = new MouseWheelZoom(transformGroup);

        BranchGroup objRoot = new BranchGroup();
        objRoot.addChild(mouseRotate);
        objRoot.addChild(mouseWheelZoom);

        objRoot.compile();

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(objRoot);

        root.compile();
        universe.addBranchGraph(root);
*/    }

    private void addLightsToUniverse(BranchGroup branchGroup) {
  /*      Color3f ambientColor = new Color3f(0.2f, 0.2f, 0.2f);
        AmbientLight ambientLight = new AmbientLight(ambientColor);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0, 0, 0), 100000);
        branchGroup.addChild(ambientLight);
*/
        Bounds influenceRegion = new BoundingSphere();
        DirectionalLight light1 = new DirectionalLight(new Color3f(Color.YELLOW), new Vector3f(-1, -1, -1));
        light1.setInfluencingBounds(influenceRegion);
        branchGroup.addChild(light1);

        DirectionalLight light2 = new DirectionalLight(new Color3f(Color.WHITE), new Vector3f(1, 1, 1));
        light2.setInfluencingBounds(influenceRegion);
        branchGroup.addChild(light2);

        DirectionalLight light3 = new DirectionalLight(new Color3f(Color.GREEN), new Vector3f(1, 1, -1));
        light3.setInfluencingBounds(influenceRegion);
        branchGroup.addChild(light3);

        DirectionalLight light4 = new DirectionalLight(new Color3f(Color.BLUE), new Vector3f(1, -1, -1));
        light4.setInfluencingBounds(influenceRegion);
        branchGroup.addChild(light4);
    }

    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return new Dimension(1000, 800);
    }
}
