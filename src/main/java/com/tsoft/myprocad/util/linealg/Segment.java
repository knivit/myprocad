package com.tsoft.myprocad.util.linealg;

public class Segment {
    public static final Segment OX = new Segment(new Vec3f(0, 0, 0), new Vec3f(1, 0, 0));

    private Vec3f p0;
    private Vec3f p1;
    private Vec3f direction;

    public Segment(Vec3f p0, Vec3f p1) {
        setP0P1(p0, p1);
    }

    public Vec3f p0() { return p0; }

    public Vec3f p1() { return p1; }

    public void setP0P1(Vec3f p0, Vec3f p1) {
        this.p0 = new Vec3f(p0);
        this.p1 = new Vec3f(p1);
        recalc();
    }

    /**
     * Return the equation on the line passing through the segments' points
     * http://www.vitutor.com/geometry/space/line_space.html
     * Example:
     * Find the equation of the line that pass through the points A = (1, 0, 1) and B = (0, 1, 1).
     * AB = (0-1, 1-0, 1-1) = (-1, 1, 0)
     */
    private void recalc() {
        direction = p1.minus(p0);
    }

    public Vec3f direction() { return direction; }

    /** http://www.vitutor.com/geometry/space/collinear_points.html */
    public Vec3f getMidpoint() {
        Vec3f midp = new Vec3f((p0.x() + p1.x()) / 2, (p0.y() + p1.y()) / 2, (p0.z() + p1.z()) / 2);
        return midp;
    }

    /** The angle between a line and a plane is equal to the complementary
     * acute angle that forms between the direction vector of the line and
     * the normal vector of the plane.
     */
    public float getAngle(Plane plane) {
        Vec3f d = new Vec3f(direction);
        float val = d.dot(plane.getNormal()) / (plane.getNormal().length() * direction.length());
        return (float)Math.asin(val);
    }

    /** dist3D_Segment_to_Segment(): get the 3D minimum distance between 2 segments
     * Input:  two 3D line segments S1 and S2
     * Return: the shortest distance between S1 and S2
     * http://geomalgorithms.com/a07-_distance.html#dist3D_Line_to_Line%28%29
     */
    public float getMinimumDistance(Segment seg) {
        Vec3f u = p1.minus(p0);
        Vec3f v = seg.p1.minus(seg.p0);
        Vec3f w = p0.minus(seg.p0);

        float a = u.dot(u);         // always >= 0
        float b = u.dot(v);
        float c = v.dot(v);         // always >= 0
        float d = u.dot(w);
        float e = v.dot(w);
        float D = a*c - b*b;        // always >= 0
        float sc, sN, sD = D;       // sc = sN / sD, default sD = D >= 0
        float tc, tN, tD = D;       // tc = tN / tD, default tD = D >= 0

        // compute the line parameters of the two closest points
        if (D < 0.00000001) { // the lines are almost parallel
            sN = 0.0f;         // force using point P0 on segment S1
            sD = 1.0f;         // to prevent possible division by 0.0 later
            tN = e;
            tD = c;
        } else {                 // get the closest points on the infinite lines
            sN = (b*e - c*d);
            tN = (a*e - b*d);
            if (sN < 0.0f) {        // sc < 0 => the s=0 edge is visible
                sN = 0.0f;
                tN = e;
                tD = c;
            }
            else if (sN > sD) {  // sc > 1  => the s=1 edge is visible
                sN = sD;
                tN = e + b;
                tD = c;
            }
        }

        if (tN < 0.0f) {            // tc < 0 => the t=0 edge is visible
            tN = 0.0f;
            // recompute sc for this edge
            if (-d < 0.0f) sN = 0.0f;
            else if (-d > a) sN = sD;
            else {
                sN = -d;
                sD = a;
            }
        }
        else if (tN > tD) {      // tc > 1  => the t=1 edge is visible
            tN = tD;
            // recompute sc for this edge
            if ((-d + b) < 0.0)
                sN = 0;
            else if ((-d + b) > a)
                sN = sD;
            else {
                sN = (-d +  b);
                sD = a;
            }
        }

        // finally do the division to get sc and tc
        sc = (Math.abs(sN) < 0.00000001 ? 0.0f : sN / sD);
        tc = (Math.abs(tN) < 0.00000001 ? 0.0f : tN / tD);

        // get the difference of the two closest points
        Vec3f dP = w.plus(u.times(sc)).minus(v.times(tc));  // =  S1(sc) - S2(tc)
        return dP.length(); // return the closest distance
    }
}
