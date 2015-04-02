package com.tsoft.myprocad.j3d;

import com.sun.j3d.utils.image.TextureLoader;
import com.tsoft.myprocad.MyProCAD;
import com.tsoft.myprocad.model.Pattern;

import javax.media.j3d.*;
import javax.vecmath.Color3f;
import java.awt.*;

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

        return a;
    }
}
