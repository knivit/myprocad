package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.io.FileReader;
import java.io.IOException;

/** From http://www.daltonfilho.com/articles/java3d/SimpleModelView.html */
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
    public void addModelToUniverse(String objFileName) throws IOException {
        Scene scene = getSceneFromFile(objFileName);
        BranchGroup root = scene.getSceneGroup();
        addLightsToUniverse(root);

        root.compile();
        universe.addBranchGraph(root);
    }

    /**
     * Adds a dramatic blue light...
     */
    private void addLightsToUniverse(BranchGroup root) {
        Bounds influenceRegion = new BoundingSphere();
        Color3f lightColor = new Color3f(Color.YELLOW);
        Vector3f lightDirection = new Vector3f(-1F, -1F, -1F);
        DirectionalLight light = new DirectionalLight(lightColor, lightDirection);
        light.setInfluencingBounds(influenceRegion);
        root.addChild(light);
    }

    // ACCESS ******************************************************************

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
        return new Dimension(600, 500);
    }
}
