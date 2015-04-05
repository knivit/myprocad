package com.tsoft.myprocad.swing.dialog;

import com.sun.j3d.loaders.Scene;
import com.sun.j3d.loaders.objectfile.ObjectFile;
import com.sun.j3d.utils.geometry.Box;
import com.sun.j3d.utils.universe.SimpleUniverse;
import com.sun.j3d.utils.behaviors.mouse.MouseRotate;
import com.sun.j3d.utils.behaviors.mouse.MouseWheelZoom;
import com.sun.j3d.utils.behaviors.keyboard.KeyNavigatorBehavior;
import com.tsoft.myprocad.model.AbstractMaterialItem;
import com.tsoft.myprocad.model.ItemList;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.FileReader;
import java.io.IOException;

/**
 * From http://www.daltonfilho.com/articles/java3d/SimpleModelView.html
 * http://www.java3d.org/selection.html
 * https://java3d.java.net/
 */
public class J3dDialog extends AbstractDialogPanel {
    private Canvas3D canvas;
    private VirtualUniverse universe;

    public J3dDialog() {
        super(new BorderLayout());

        canvas = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        canvas.setDoubleBufferEnable(true);
        add(canvas, BorderLayout.CENTER);
    }

    public void addModelToUniverse(ItemList<AbstractMaterialItem> items) throws IOException {
        TransformGroup transformGroup = new TransformGroup();
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        transformGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);

        int dx = Math.abs(items.getXMax()) + Math.abs(items.getXMin());
        int dy = Math.abs(items.getYMax()) + Math.abs(items.getYMin());
        int dz = Math.abs(items.getZMax()) + Math.abs(items.getZMin());
        float scale = Math.max(dx, Math.max(dy, dz)) / 2;
        for (AbstractMaterialItem item : items) {
            Shape3D shape = item.getShape3D(scale);
            transformGroup.addChild(shape);
        }
/*
        MouseRotate mouseRotate = new MouseRotate(transformGroup);
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        mouseRotate.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseRotate);

        MouseWheelZoom mouseWheelZoom = new MouseWheelZoom(transformGroup);
        mouseWheelZoom.setSchedulingBounds(bounds);
        transformGroup.addChild(mouseWheelZoom);

        KeyNavigatorBehavior navigatorBehavior = new KeyNavigatorBehavior(transformGroup);
        navigatorBehavior.setSchedulingBounds(bounds);
        transformGroup.addChild(navigatorBehavior);
*/
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0, 0.0), 100.0);
        BranchGroup branchGroup = new BranchGroup();
        Color3f ambientColor = new Color3f(0.2f, 0.2f, 0.2f);
        AmbientLight ambientLight = new AmbientLight(ambientColor);
        ambientLight.setInfluencingBounds(bounds);
        branchGroup.addChild(ambientLight);

        branchGroup.addChild(transformGroup);
        branchGroup.compile();

        universe = new VirtualUniverse();
        //universe.getViewingPlatform().setNominalViewingTransform();
        //universe.addBranchGraph(branchGroup);
        Locale locale = new Locale(universe);
        locale.addBranchGraph(buildViewBranch(canvas));
        locale.addBranchGraph(buildContentBranch());
    }

    protected BranchGroup buildViewBranch(Canvas3D c) {
        BranchGroup viewBranch = new BranchGroup();
        Transform3D viewXfm = new Transform3D();
        viewXfm.set(new Vector3f(0.0f, 0.0f, 10.0f));
        TransformGroup viewXfmGroup = new TransformGroup(viewXfm);
        viewXfmGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_READ);
        viewXfmGroup.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        BoundingSphere movingBounds = new BoundingSphere(new Point3d(0.0, 0.0,  0.0), 100.0);
        BoundingLeaf boundLeaf = new BoundingLeaf(movingBounds);
        ViewPlatform myViewPlatform = new ViewPlatform();
        viewXfmGroup.addChild(boundLeaf);
        PhysicalBody myBody = new PhysicalBody();
        PhysicalEnvironment myEnvironment = new PhysicalEnvironment();
        viewXfmGroup.addChild(myViewPlatform);
        viewBranch.addChild(viewXfmGroup);
        View myView = new View();
        myView.addCanvas3D(c);
        myView.attachViewPlatform(myViewPlatform);
        myView.setPhysicalBody(myBody);
        myView.setPhysicalEnvironment(myEnvironment);

        KeyNavigatorBehavior keyNav = new KeyNavigatorBehavior(viewXfmGroup);
        keyNav.setSchedulingBounds(movingBounds);
        viewBranch.addChild(keyNav);

        return viewBranch;
    }

    protected void addLights(BranchGroup b) {
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0, 0.0,
                0.0), 100.0);

        // Create a bounds for the background and lights
        // Set up the global lights
        Color3f ambLightColour = new Color3f(0.5f, 0.5f, 0.5f);
        AmbientLight ambLight = new AmbientLight(ambLightColour);
        ambLight.setInfluencingBounds(bounds);
        Color3f dirLightColour = new Color3f(1.0f, 1.0f, 1.0f);
        Vector3f dirLightDir = new Vector3f(-1.0f, -1.0f, -1.0f);
        DirectionalLight dirLight = new DirectionalLight(dirLightColour,
                dirLightDir);
        dirLight.setInfluencingBounds(bounds);
        b.addChild(ambLight);
        b.addChild(dirLight);
    }

    protected BranchGroup buildContentBranch() {
        //Create the appearance an appearance for the two cubes
        Appearance app1 = new Appearance();
        Appearance app2 = new Appearance();
        Color3f ambientColour1 = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f ambientColour2 = new Color3f(1.0f, 1.0f, 0.0f);
        Color3f emissiveColour = new Color3f(0.0f, 0.0f, 0.0f);
        Color3f specularColour = new Color3f(1.0f, 1.0f, 1.0f);
        Color3f diffuseColour1 = new Color3f(1.0f, 0.0f, 0.0f);
        Color3f diffuseColour2 = new Color3f(1.0f, 1.0f, 0.0f);
        float shininess = 20.0f;
        app1.setMaterial(new Material(ambientColour1, emissiveColour,
                diffuseColour1, specularColour, shininess));
        app2.setMaterial(new Material(ambientColour2, emissiveColour,
                diffuseColour2, specularColour, shininess));
        //Make two cubes
        Box leftCube = new Box(1.0f, 1.0f, 1.0f, app1);
        Box rightCube = new Box(1.0f, 1.0f, 1.0f, app2);

        BranchGroup contentBranch = new BranchGroup();
        addLights(contentBranch);
        //Put it all together
        Transform3D leftGroupXfm = new Transform3D();
        leftGroupXfm.set(new Vector3d(-1.5, 0.0, 0.0));
        TransformGroup leftGroup = new TransformGroup(leftGroupXfm);
        Transform3D rightGroupXfm = new Transform3D();
        rightGroupXfm.set(new Vector3d(1.5, 0.0, 0.0));
        TransformGroup rightGroup = new TransformGroup(rightGroupXfm);

        leftGroup.addChild(leftCube);
        rightGroup.addChild(rightCube);
        contentBranch.addChild(leftGroup);
        contentBranch.addChild(rightGroup);
        return contentBranch;
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
