package com.tsoft.myprocad.util.linealg;

/**
 * Represents a plane in 3D space.
 * git://github.com/sgothel/jogl-demos.git : src/gleem/linalg
*/
public class Plane {
    public static final Plane XOY = new Plane(new Vec3f(0, 0, 1), new Vec3f(0, 0, 0));
    public static final Plane XOZ = new Plane(new Vec3f(0, 1, 0), new Vec3f(0, 0, 0));
    public static final Plane YOZ = new Plane(new Vec3f(1, 0, 0), new Vec3f(0, 0, 0));

    /** Normalized */
    private Vec3f normal;
    private Vec3f point;

    /** Constant for faster projection and intersection */
    private float c;

    /** Default constructor initializes normal to (0, 1, 0) and point to
     (0, 0, 0) */
    public Plane() {
        normal = new Vec3f(0, 1, 0);
        point = new Vec3f(0, 0, 0);
        recalc();
    }

    /** Sets all parameters of plane. Plane has normal <b>normal</b> and
     goes through the point <b>point</b>. Normal does not need to be
     unit length but must not be the zero vector. */
    public Plane(Vec3f normal, Vec3f point) {
        this.normal = new Vec3f(normal);
        this.normal.normalize();
        this.point = new Vec3f(point);
        recalc();
    }

    /** Setter does some work to maintain internal caches. Normal does
     not need to be unit length but must not be the zero vector. */
    public void setNormal(Vec3f normal) {
        this.normal.set(normal);
        this.normal.normalize();
        recalc();
    }

    /** Normal is normalized internally, so <b>normal</b> is not
     necessarily equal to <code>plane.setNormal(normal);
     plane.getNormal();</code> */
    public Vec3f getNormal() {
        return normal;
    }

    /** Setter does some work to maintain internal caches */
    public void setPoint(Vec3f point) {
        this.point.set(point);
        recalc();
    }


    public Vec3f getPoint() {
        return point;
    }

    /** Project a point onto the plane */
    public void projectPoint(Vec3f pt, Vec3f projPt) {
        float scale = normal.dot(pt) - c;
        projPt.set(pt.minus(normal.times(normal.dot(point) - c)));
    }

    /** Intersect a ray with the plane. Returns true if intersection occurred, false
     otherwise. This is a two-sided ray cast. */
    public boolean intersectRay(Vec3f rayStart, Vec3f rayDirection, Vec3f intPt) {
        float denom = normal.dot(rayDirection);
        if (denom == 0) return false;
        float t = (c - normal.dot(rayStart)) / denom;
        intPt.set(rayStart.plus(rayDirection.times(t)));
        return true;
    }

    private void recalc() {
        c = normal.dot(point);
    }

    /** intersect3D_SegmentPlane(): find the 3D intersection of a segment and a plane
     *  http://geomalgorithms.com/a05-_intersect-1.html
     *
     *  Input:  S = a segment, and Pn = a plane = {Point V0;  Vector n;}
     *  Output: *I0 = the intersect point (when it exists)
     *  Return: 0 = disjoint (no intersection)
     *   1 = intersection in the unique point *I0
     *   2 = the segment lies in the plane
     */
    public int intersectSegment(Segment S, Plane Pn, Vec3f intPt) {
        Vec3f u = new Vec3f(S.p1()).minus(S.p0());
        Vec3f w = new Vec3f(S.p0()).minus(Pn.point);

        float D = Pn.normal.dot(u);
        float N = -Pn.normal.dot(w);

        // segment is parallel to plane
        if (Math.abs(D) < 0.00000001) {
            if (N == 0)                      // segment lies in plane
                return 2;
            else
                return 0;                    // no intersection
        }

        // they are not parallel
        // compute intersect param
        float sI = N / D;
        if (sI < 0 || sI > 1)
            return 0;                        // no intersection

        intPt.set(S.p0().plus(u.times(sI)));  // compute segment intersect point
        return 1;
    }

}
