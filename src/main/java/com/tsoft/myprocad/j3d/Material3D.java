package com.tsoft.myprocad.j3d;

import com.sun.j3d.utils.image.TextureLoader;
import com.tsoft.myprocad.model.Pattern;

import javax.media.j3d.Appearance;
import javax.media.j3d.Material;
import javax.media.j3d.TexCoordGeneration;
import javax.media.j3d.Texture;
import javax.media.j3d.Texture2D;
import javax.media.j3d.TransparencyAttributes;
import javax.vecmath.Color3f;
import java.awt.Color;

public class Material3D {
    public Color3f Ka;
    public Color3f Kd;
    public Color3f Ks;
    public int illum;
    public float Ns;
    public Texture2D t;
    public boolean transparent;
    public float transparencyLevel;

    public Material3D setKa(float r, float g, float b) {
        Ka = new Color3f(r, g, b);
        return this;
    }

    public Material3D setKd(float r, float g, float b) {
        Kd = new Color3f(r, g, b);
        return this;
    }

    public Material3D setKs(float r, float g, float b) {
        Ks = new Color3f(r, g, b);
        return this;
    }

    public Material3D setIllum(int value) {
        illum = value;
        return this;
    }

    public Material3D setTransparency(float transparencyLevel) {
        transparent = (transparencyLevel < 1.0f);
        return this;
    }

    public Material3D setNs(float value) {
        Ns = value;
        return this;
    }

    public Material3D setSharpness(float value) {
        // Not implemented
        return this;
    }

    /**
     * Materials have five properties that enable you to specify how the object appears.
     * There are four colors: Ambient, Emissive, Diffuse, and Specular.
     * The fifth property is shininess, that you specify with a number.
     *
     * Each color specifies what light is given off in a certain situation.
     *
     * Ambient color reflects light that been scattered so much by the environment that the direction
     * is impossible to determine. This is created by an AmbientLight in Java 3D.
     * Emissive color is given off even in darkness. You could use this for a neon sign or a glow-in-the-dark object
     * Diffuse color reflects light that comes from one direction, so it's brighter if it comes squarely down
     * on a surface that if it barely glances off the surface. This is used with a DirectionalLight.
     * Specular light comes from a particular direction, and it tends to bounce off the surface in a preferred direction.
     * Shiny metal or plastic have a high specular component.
     * The amount of specular light that reaches the viewer depends on the location of the viewer and the angle
     * of the light bouncing off the object.
     *
     * Changing the shininess factor affects not just how shiny the object is, but whether it shines
     * with a small glint in one area, or a larger area with less of a gleaming look.
     * For most objects you can use one color for both Ambient and Diffuse components, and black
     * for Emissive (most things don't glow in the dark). If it's a shiny object, you would use a
     * lighter color for Specular reflections. For example, the material for a red billiard ball might be:
     * Material mat = new Material(red, black, red, white, 70f);
     *
     * For a rubber ball, you could use a black or red specular light instead of white which would make
     * the ball appear less shiny. Reducing the shininess factor from 70 to 0 would not work the way you might expect,
     * it would spread the white reflection across the whole object instead of it being concentrated in one spot.
     */
    public Appearance getAppearence(Pattern pattern) {
        Appearance a = new Appearance();
        Material m = new Material();

        // Set ambient & diffuse color
        if (Ka != null) m.setAmbientColor(Ka);
        if (Kd != null) m.setDiffuseColor(Kd);

        // Set specular color
        if ((Ks != null) && (illum != 1)) m.setSpecularColor(Ks);
        else if (illum == 1) m.setSpecularColor(0.0f, 0.0f, 0.0f);

        if (illum >= 1) m.setLightingEnable(true);
        else if (illum == 0) m.setLightingEnable(false);

        if (Ns != -1.0f) m.setShininess(Ns);

        if (t != null) {
            Texture texture = new TextureLoader(pattern.getPatternImage(Color.WHITE.getRGB(), Color.BLACK.getRGB())).getTexture();
            a.setTexture(texture);

            // Create Texture Coordinates if not already present
            //if ((((GeometryArray)shape.getGeometry()).getVertexFormat() &
            //        GeometryArray.TEXTURE_COORDINATE_2) == 0) {
                TexCoordGeneration tcg = new TexCoordGeneration();
                a.setTexCoordGeneration(tcg);
            //}
        }

        if (transparent) {
            TransparencyAttributes ta = new TransparencyAttributes(TransparencyAttributes.NICEST, transparencyLevel);
            a.setTransparencyAttributes(ta);
        }

        a.setMaterial(m);
        return a;
    }
}
