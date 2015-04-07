package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileReader;
import java.io.IOException;

/**
 * From http://www.daltonfilho.com/articles/java3d/SimpleModelView.html
 * http://www.java3d.org/selection.html
 * https://java3d.java.net/
 *
 * Should be AWT, as Java3D can't render on Swing components
 */
public class J3dDialog extends Frame {
    private Canvas3D canvas;
    private SimpleUniverse universe;
    private TransformGroup transformGroup;

    public J3dDialog() {
        super("3D View");

        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);

        add(canvas, BorderLayout.CENTER);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_UP) { transformGroup.setTransform(new Transform3D(new float[] { 0f, 0f, 0.1f })); }
                if (e.getKeyCode() == KeyEvent.VK_DOWN) { transformGroup.setTransform(new Transform3D(new float[] { 0f, 0f, -0.1f })); }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) { transformGroup.setTransform(new Transform3D(new float[] { -0.1f, 0f, 0f })); }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) { transformGroup.setTransform(new Transform3D(new float[] { 0.1f, 0f, 0f })); }
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) {
                setMinimumSize(new Dimension(800, 800));
                setLocationRelativeTo(null);
            }

            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void addModelToUniverse(ItemList<AbstractMaterialItem> items) {
        BoundingSphere bounds = new BoundingSphere();

        BranchGroup contentGroup = new BranchGroup();
        transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        addLights(contentGroup);

        int xMin = items.getXMin(); int xMax = items.getXMax();
        int yMin = items.getYMin(); int yMax = items.getYMax();
        int zMin = items.getZMin(); int zMax = items.getZMax();
        int ox = (xMax - xMin) / 2;
        int oy = (yMax - yMin) / 2;
        int oz = (zMax - zMin) / 2;
        int dx = Math.abs(xMax) + Math.abs(xMin);
        int dy = Math.abs(yMax) + Math.abs(yMin);
        int dz = Math.abs(zMax) + Math.abs(zMin);
        float scale = Math.max(dx, Math.max(dy, dz)) / 2;
        for (AbstractMaterialItem item : items) {
            Shape3D shape = item.getShape3D(scale, ox, oy, oz);
            transformGroup.addChild(shape);
        }

        MouseRotate mouseRotate = new MouseRotate(transformGroup);
        mouseRotate.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseRotate);

        MouseWheelZoom mouseWheelZoom = new MouseWheelZoom(transformGroup);
        mouseWheelZoom.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseWheelZoom);

        //KeyNavigatorBehavior navigatorBehavior = new KeyNavigatorBehavior(transformGroup);
        //navigatorBehavior.setSchedulingBounds(bounds);
        //transformGroup.addChild(navigatorBehavior);

        contentGroup.addChild(transformGroup);
        contentGroup.compile();

        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(contentGroup);
    }

    protected void addLights(BranchGroup b) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);

        // Create a bounds for the background and lights
        // Set up the global lights
        Color3f ambLightColour = new Color3f(0.5f, 0.5f, 0.5f);
        AmbientLight ambLight = new AmbientLight(ambLightColour);
        ambLight.setInfluencingBounds(bounds);
        b.addChild(ambLight);

        Color3f dirLightColour = new Color3f(1.0f, 1.0f, 1.0f);
        DirectionalLight dirLight1 = new DirectionalLight(dirLightColour, new Vector3f(-1.0f, -1.0f, -1.0f));
        dirLight1.setInfluencingBounds(bounds);
        b.addChild(dirLight1);

        DirectionalLight dirLight2 = new DirectionalLight(dirLightColour, new Vector3f(1.0f, 1.0f, 1.0f));
        dirLight2.setInfluencingBounds(bounds);
        b.addChild(dirLight2);
    }
}
