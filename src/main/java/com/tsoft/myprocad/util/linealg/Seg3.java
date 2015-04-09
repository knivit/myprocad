package com.tsoft.myprocad.util.linealg;

/**
 * Altitudes of 3d triangle = http://www.algebra.com/algebra/homework/Graphs.faq.question.127639.html
 * http://math.kennesaw.edu/~plaval/math2203/linesplanes.pdf
 */
public class Seg3 {
    public static final Seg3 OX = new Seg3(new Vec3(0, 0, 0), new Vec3(1, 0, 0));

    private Vec3 p0;
    private Vec3 p1;

    // calculated
    private Vec3 direction;

    public Seg3(Vec3 p0, Vec3 p1) {
        set(p0, p1);
    }

    public Vec3 p0() { return p0; }

    public Vec3 p1() { return p1; }

    public void set(Vec3 p0, Vec3 p1) {
        this.p0 = new Vec3(p0);
        this.p1 = new Vec3(p1);
        recalc();
    }

    private void recalc() {
        direction = p1.minus(p0);
        direction.normalize();
    }

    /**
     * Return the equation on the line passing through the segments' points
     * http://www.vitutor.com/geometry/space/line_space.html
     * Example:
     * Find the equation of the line that pass through the points A = (1, 0, 1) and B = (0, 1, 1).
     * AB = (0-1, 1-0, 1-1) = (-1, 1, 0)
     */
    public Vec3 direction() { return direction; }

    /** http://www.geom.uiuc.edu/docs/reference/CRC-formulas/node54.html */
    public float getLength() {
        return p0.minus(p1).length();
    }

    /** http://www.vitutor.com/geometry/space/collinear_points.html */
    public Vec3 getMidpoint() {
        Vec3 midp = new Vec3((p0.x() + p1.x()) / 2, (p0.y() + p1.y()) / 2, (p0.z() + p1.z()) / 2);
        return midp;
    }

    /** Returns a point on the segment
     * lying on it at a distance from p0
     * Or null if it is behind it's end
     */
    public Vec3 getPointOnSeg(float distanceFromP0) {
        float length = getLength();
        if (distanceFromP0 > length) return null;
        if (Math.abs(distanceFromP0 - length) < 0.0001) return new Vec3(p1);

        Vec3 off = direction.times(distanceFromP0);
        return p0.plus(off);
    }

    /** The angle between a line and a plane is equal to the complementary
     * acute angle that forms between the direction vector of the line and
     * the normal vector of the plane.
     */
    public float getAngle(Plane plane) {
        Vec3 d = new Vec3(direction);
        float val = d.dot(plane.getNormal()) / (plane.getNormal().length() * direction.length());
        return (float)Math.acos(val);
    }

    /** The Distance between the point (x0,y0,z0) and the line through (x1,y1,z1) in direction (a,b,c)
     * http://www.geom.uiuc.edu/docs/reference/CRC-formulas/node54.html
     */
    public float getDistanceToPoint(Vec3 p) {
        Mat2 a = new Mat2(p0.y() - p.y(), p0.z() - p.z(), direction.y(), direction.z());
        Mat2 b = new Mat2(p0.z() - p.z(), p0.x() - p.x(), direction.z(), direction.x());
        Mat2 c = new Mat2(p0.x() - p.x(), p0.y() - p.y(), direction.x(), direction.y());
        float ad = a.determinant();
        float bd = b.determinant();
        float cd = c.determinant();
        return (float)Math.sqrt(ad*ad + bd*bd + cd*cd) / direction.length();
    }

    /** dist3D_Segment_to_Segment(): get the 3D minimum distance between 2 segments
     * Input:  two 3D line segments S1 and S2
     * Return: the shortest distance between S1 and S2
     * http://geomalgorithms.com/a07-_distance.html#dist3D_Line_to_Line%28%29
     */
    public float getMinimumDistance(Seg3 seg) {
        Vec3 u = p1.minus(p0);
        Vec3 v = seg.p1.minus(seg.p0);
        Vec3 w = p0.minus(seg.p0);

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
        Vec3 dP = w.plus(u.times(sc)).minus(v.times(tc));  // =  S1(sc) - S2(tc)
        return dP.length(); // return the closest distance
    }

    public Seg2 get2dProjectionOnPlane(Plane plane) {
        Vec3 pp0 = plane.projectPoint(p0);
        Vec3 pp1 = plane.projectPoint(p1);
        return new Seg2(new Vec2(pp0.x(), pp0.y()), new Vec2(pp1.x(), pp1.y()));
    }

    public boolean isVertical() {
        return (Math.abs(direction.x()) < 0.00001) && (Math.abs(direction.y()) < 0.00001);
    }

    @Override
    public String toString() {
        return "{p0=" + p0.toString() + ", p1=" + p1.toString() + ", direction=" + direction.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seg3 seg3 = (Seg3) o;

        if (!p0.equals(seg3.p0)) return false;
        return p1.equals(seg3.p1);

    }

    @Override
    public int hashCode() {
        int result = p0.hashCode();
        result = 31 * result + p1.hashCode();
        return result;
    }
}
