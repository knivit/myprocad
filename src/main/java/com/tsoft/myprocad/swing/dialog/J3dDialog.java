package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;
import com.tsoft.myprocad.model.Light;
import com.tsoft.myprocad.model.LightType;
import com.tsoft.myprocad.util.linealg.Vec3;

import javax.media.j3d.AmbientLight;
import javax.media.j3d.Behavior;
import javax.media.j3d.BoundingSphere;
import javax.media.j3d.BranchGroup;
import javax.media.j3d.Canvas3D;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Shape3D;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.WakeupOnAWTEvent;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3f;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Enumeration;

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
    private Transform3D transform = new Transform3D();

    class MoveBehavior extends Behavior {
        private TransformGroup tg;

        MoveBehavior(TransformGroup tg) { this.tg = tg; }

        @Override
        public void initialize() {
            wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
        }

        @Override
        public void processStimulus(Enumeration enumeration) {
            Transform3D move = new Transform3D();
            move.setTranslation(new Vector3f(0f, 0f, 0f));
            tg.setTransform(move);
            wakeupOn(new WakeupOnAWTEvent(KeyEvent.KEY_PRESSED));
        }
    }

    public J3dDialog() {
        super("3D View");

        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);

        add(canvas, BorderLayout.CENTER);

        canvas.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    transform.setTranslation(new Vector3f(0f, 0f, 0f));
                    transformGroup.setTransform(transform);
                }

                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    Transform3D up = new Transform3D();
                    up.set(new Vector3f(0.9f, 1f, 1f));
                    transformGroup.setTransform(up);
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) { transformGroup.setTransform(new Transform3D(new float[] { 0f, 0f, -0.1f })); }
                if (e.getKeyCode() == KeyEvent.VK_LEFT) { transformGroup.setTransform(new Transform3D(new float[] { -0.1f, 0f, 0f })); }
                if (e.getKeyCode() == KeyEvent.VK_RIGHT) { transformGroup.setTransform(new Transform3D(new float[] { 0.1f, 0f, 0f })); }
            }
        });

        addWindowListener(new WindowAdapter() {
            public void windowOpened(WindowEvent we) {
                setMinimumSize(new Dimension(1024, 1024));
                setLocationRelativeTo(null);
            }

            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
    }

    public void addModelToUniverse(ItemList<AbstractMaterialItem> items, List<Light> planLights) {
        BoundingSphere bounds = new BoundingSphere();

        BranchGroup contentGroup = new BranchGroup();
        transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        List<Light> lights = new ArrayList<>(planLights);
        if (lights.isEmpty()) {
            Light ambientLight = new Light();
            ambientLight.setLightType(LightType.AMBIENT);
            ambientLight.setCenter(new Vec3(0, 0, 0));
            ambientLight.setColor(new Color(120, 120, 120));
            lights.add(ambientLight);

            Light dirLight = new Light();
            dirLight.setLightType(LightType.DIRECTIONAL);
            dirLight.setColor(Color.WHITE);
            dirLight.setCenter(0, 0, 0);
            dirLight.setDirection(1, 1, 1);
            lights.add(dirLight);
        }
        addLights(contentGroup, lights);

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

        MoveBehavior moveBehavior = new MoveBehavior(transformGroup);
        moveBehavior.setSchedulingBounds(bounds);
        transformGroup.addChild(moveBehavior);
        //KeyNavigatorBehavior navigatorBehavior = new KeyNavigatorBehavior(transformGroup);
        //navigatorBehavior.setSchedulingBounds(bounds);
        //transformGroup.addChild(navigatorBehavior);

        contentGroup.addChild(transformGroup);
        contentGroup.compile();

        universe = new SimpleUniverse(canvas);
        universe.getViewingPlatform().setNominalViewingTransform();
        universe.addBranchGraph(contentGroup);
    }

    protected void addLights(BranchGroup group, List<Light> lights) {
        for (Light light : lights) {
            Point3d center = new Point3d(light.getCx(), light.getCy(), light.getCz());
            BoundingSphere bounds = new BoundingSphere(center, 100.0);
            Color3f color = new Color3f(light.getColor());

            javax.media.j3d.Light li;
            switch (light.getLightType()) {
                case AMBIENT: {
                    li = new AmbientLight(color);
                    break;
                }
                case DIRECTIONAL: {
                    Vector3f dir = new Vector3f(light.getDx(), light.getDy(), light.getDz());
                    li = new DirectionalLight(color, dir);
                    break;
                }
                default:
                    throw new IllegalArgumentException("Unknown light " + light.getLightType().toString());
            }

            li.setInfluencingBounds(bounds);
            group.addChild(li);
        }
    }
}
