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

    /**
     * Return the equation on the line passing through the segments' points
     * http://www.vitutor.com/geometry/space/line_space.html
     * Example:
     * Find the equation of the line that pass through the points A = (1, 0, 1) and B = (0, 1, 1).
     * AB = (0-1, 1-0, 1-1) = (-1, 1, 0)
     */
    public Vec3f getEquation() {
        Vec3f eq = p1.minus(p0);
        return eq;
    }

    /** http://www.vitutor.com/geometry/space/collinear_points.html */
    public Vec3f getMidpoint() {
        Vec3f midp = new Vec3f((p0.x() + p1.x()) / 2, (p0.y() + p1.y()) / 2, (p0.z() + p1.z()) / 2);
        return midp;
    }
}
