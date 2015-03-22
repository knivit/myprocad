package com.tsoft.myprocad.util.linealg;

public class Segment {
    private Vec3f p0;
    private Vec3f p1;

    public Segment(Vec3f p0, Vec3f p1) {
        this.p0 = new Vec3f(p0);
        this.p1 = new Vec3f(p1);
    }

    public Vec3f p0() { return p0; }

    public Vec3f p1() { return p1; }
}
