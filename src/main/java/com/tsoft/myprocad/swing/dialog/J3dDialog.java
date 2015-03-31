package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.tsoft.myprocad.j3d.Scene3D;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import javax.vecmath.Point3d;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

/**
 * From http://www.daltonfilho.com/articles/java3d/SimpleModelView.html
 * http://www.java3d.org/selection.html
 * https://java3d.java.net/
 */
public class J3dDialog extends AbstractDialogPanel {
    /** The canvas where the object is rendered. */
    private Canvas3D canvas;

    /** Simplifies the configuration of the scene. */
    private SimpleUniverse universe;

    /**
     * Creates a window with a 3D canvas on its center.
     *
     * @throws IOException if some error occur while loading the model
     */
    public J3dDialog() {
        super(new BorderLayout());

        configureCanvas();
        conigureUniverse();
    }

    /**
     * Defines basic properties of the canvas.
     */
    private void configureCanvas() {
        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        add(canvas, BorderLayout.CENTER);
    }

    /**
     * Defines basic properties of the universe.
     */
    private void conigureUniverse() {
        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
    }

    /**
     * Loads a model from disk and assign the root node of the scene
     *
     * @throws IOException if it's impossible to find the 3D model
     */
    public void addModelToUniverse(ItemList<AbstractMaterialItem> items) throws IOException {
        Scene3D scene = new Scene3D();//getSceneFromFile(objFileName);
        scene.addItems(items);

        BranchGroup branchGroup = scene.getSceneGroup();
        addLightsToUniverse(branchGroup);

        TransformGroup rotating = new TransformGroup();
        rotating.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        rotating.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        rotating.addChild(branchGroup);

        MouseRotate mouseRotate = new MouseRotate(rotating);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 10000.0);
        mouseRotate.setSchedulingBounds(bounds);

        TransformGroup zooming = new TransformGroup();
        zooming.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        zooming.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        //branchGroup.addChild();
        MouseWheelZoom mouseWheelZoom = new MouseWheelZoom(zooming);

        BranchGroup objRoot = new BranchGroup();
        objRoot.addChild(mouseRotate);
        objRoot.addChild(mouseWheelZoom);
        objRoot.addChild(rotating);
        objRoot.addChild(zooming);

        objRoot.compile();

        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(objRoot);

  //      root.compile();
  //      universe.addBranchGraph(root);
    }

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

    /**
     * Loads a scene from a Wavefront .obj model.
     *
     * @param location the path of the model
     * @return Scene the scene contained on the model
     * @throws IOException if some error occur while loading the model
     */
    public static Scene getSceneFromFile(String location) throws IOException {
        ObjectFile file = new ObjectFile(ObjectFile.RESIZE);
        return file.load(new FileReader(location));
    }

    @Override
    public Dimension getDialogPreferredSize() {
        return new Dimension(1000, 800);
    }
}
