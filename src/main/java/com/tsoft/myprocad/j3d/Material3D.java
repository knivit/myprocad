package com.tsoft.myprocad.j3d;

import javax.media.j3d.Texture2D;
import javax.vecmath.Color3f;

public class Material3D {
    public Color3f Ka;
    public Color3f Kd;
    public Color3f Ks;
    public int illum;
    public float Ns;
    public Texture2D t;
    public boolean transparent;
    public float transparencyLevel;
}
