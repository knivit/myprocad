package com.tsoft.myprocad.util.linealg;

/**
 * Represents a plane in 3D space.
 * git://github.com/sgothel/jogl-demos.git : src/gleem/linalg
*/
public class Plane {
    public static final Plane XOY = new Plane(new Vec3(0, 0, 1), new Vec3(0, 0, 0));
    public static final Plane XOZ = new Plane(new Vec3(0, 1, 0), new Vec3(0, 0, 0));
    public static final Plane YOZ = new Plane(new Vec3(1, 0, 0), new Vec3(0, 0, 0));

    /** Normalized */
    private Vec3 normal;
    private Vec3 point;

    /** Equation */
    private float A, B, C, D;

    /** Constant for faster projection and intersection */
    private float c;

    /** Default constructor initializes normal to (0, 1, 0) and point to
     (0, 0, 0) */
    public Plane() {
        normal = new Vec3(0, 1, 0);
        point = new Vec3(0, 0, 0);
        recalc();
    }

    /** Sets all parameters of plane. Plane has normal <b>normal</b> and
     goes through the point <b>point</b>. Normal does not need to be
     unit length but must not be the zero vector. */
    public Plane(Vec3 normal, Vec3 point) {
        this.normal = new Vec3(normal);
        this.normal.normalize();
        this.point = new Vec3(point);
        recalc();
    }

    public float A() { return A; }
    public float B() { return B; }
    public float C() { return C; }
    public float D() { return D; }

    /** Setter does some work to maintain internal caches. Normal does
     not need to be unit length but must not be the zero vector. */
    public void setNormal(Vec3 normal) {
        this.normal.set(normal);
        this.normal.normalize();
        recalc();
    }

    /** Normal is normalized internally, so <b>normal</b> is not
     necessarily equal to <code>plane.setNormal(normal);
     plane.getNormal();</code> */
    public Vec3 getNormal() {
        return normal;
    }

    /** Setter does some work to maintain internal caches */
    public void setPoint(Vec3 point) {
        this.point.set(point);
        recalc();
    }

    public Vec3 getPoint() {
        return point;
    }

    /** Project a point onto the plane */
    public Vec3 projectPoint(Vec3 pt) {
        float scale = normal.dot(pt) - c;
        return pt.minus(normal.times(normal.dot(point) - c));
    }

    /** Intersect a ray with the plane. Returns true if intersection occurred, false
     otherwise. This is a two-sided ray cast. */
    public boolean intersectRay(Vec3 rayStart, Vec3 rayDirection, Vec3 intPt) {
        float denom = normal.dot(rayDirection);
        if (denom == 0) return false;
        float t = (c - normal.dot(rayStart)) / denom;
        intPt.set(rayStart.plus(rayDirection.times(t)));
        return true;
    }

    private void recalc() {
        c = normal.dot(point);
        calcEquation();
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
    public int intersectSegment(Seg3 S, Plane Pn, Vec3 intPt) {
        Vec3 u = new Vec3(S.p1()).minus(S.p0());
        Vec3 w = new Vec3(S.p0()).minus(Pn.point);

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

    /** intersect3D_2Planes(): find the 3D intersection of two planes
     * Input:  two planes Pn1 and Pn2
     * Output: *L = the intersection line (when it exists)
     * Return: 0 = disjoint (no intersection)
     *        1 = the two  planes coincide
     *        2 =  intersection in the unique line *L
     * http://geomalgorithms.com/a05-_intersect-1.html
    */
    int intersectPlane(Plane plane, Seg3 line) {
        Vec3 u = normal.cross(plane.normal);          // cross product
        float ax = (u.x() >= 0 ? u.x() : -u.x());
        float ay = (u.y() >= 0 ? u.y() : -u.y());
        float az = (u.z() >= 0 ? u.z() : -u.z());

        // test if the two planes are parallel
        if ((ax+ay+az) < 0.00000001) {        // Pn1 and Pn2 are near parallel
            // test if disjoint or coincide
            Vec3 v = plane.point.minus(point);
            if (v.dot(normal) == 0)      // Pn2.V0 lies in Pn1
                return 1;                    // Pn1 and Pn2 coincide
            else
                return 0;                    // Pn1 and Pn2 are disjoint
        }

        // Pn1 and Pn2 intersect in a line
        // first determine max abs coordinate of cross product
        int maxc;                       // max coordinate
        if (ax > ay) {
            if (ax > az) maxc =  1;
            else maxc = 3;
        } else {
            if (ay > az) maxc =  2;
            else maxc = 3;
        }

        // next, to get a point on the intersect line
        // zero the max coord, and solve for the other two
        float d1, d2;            // the constants in the 2 plane equations
        d1 = -(normal.dot(point));  // note: could be pre-stored  with plane
        d2 = -(plane.normal.dot(plane.point));  // ditto

        float x, y, z;              // intersect point
        switch (maxc) {             // select max coordinate
            case 1:                     // intersect with x=0
                x = 0;
                y = (d2*normal.z() - d1*plane.normal.z()) / u.x();
                z = (d1*plane.normal.y() - d2*normal.y()) / u.x();
                break;
            case 2:                     // intersect with y=0
                x = (d1*plane.normal.z() - d2*normal.z()) / u.y();
                y = 0;
                z = (d2*normal.x() - d1*plane.normal.x()) / u.y();
                break;
            case 3:                     // intersect with z=0
                x = (d2*normal.y() - d1*plane.normal.y()) / u.z();
                y = (d1*plane.normal.x() - d2*normal.x()) / u.z();
                z = 0;
                break;
            default: throw new IllegalStateException();
        }

        Vec3 p0 = new Vec3(x, y, z);
        line.set(p0, u.plus(p0));
        return 2;
    }

    /**
      Determine the equation of the plane as
      Ax + By + Cz + D = 0
    */
    private void calcEquation() {
        float l = (float)Math.sqrt(normal.x() * normal.x() + normal.y() * normal.y() + normal.z() * normal.z());
        A = normal.x() / l;
        B = normal.y() / l;
        C = normal.z() / l;
        D = -(normal.x()*point.x() + normal.y()*point.y() + normal.z()*point.z());
    }

    /*-------------------------------------------------------------------------
       Clip a 3 vertex facet in place
       The 3 point facet is defined by vertices p[0],p[1],p[2], "p[3]"
          There must be a fourth point as a 4 point facet may result
       The normal to the plane is n
       A point on the plane is p0
       The side of the plane containing the normal is clipped away
       Return the number of vertices in the clipped polygon
       http://paulbourke.net/geometry/polygonmesh/source3.c
    */
    public int clipFacet(Vec3[] p) {
        float[] side = new float[3];

        /*
          Evaluate the equation of the plane for each vertex
          If side < 0 then it is on the side to be retained
          else it is to be clipped
        */
        side[0] = A*p[0].x() + B*p[0].y() + C*p[0].z() + D;
        side[1] = A*p[1].x() + B*p[1].y() + C*p[1].z() + D;
        side[2] = A*p[2].x() + B*p[2].y() + C*p[2].z() + D;

        /* Are all the vertices are on the clipped side */
        if (side[0] >= 0 && side[1] >= 0 && side[2] >= 0) return 0;

        /* Are all the vertices on the not-clipped side */
        if (side[0] <= 0 && side[1] <= 0 && side[2] <= 0) return 3;

        /* Is p0 the only point on the clipped side */
        if (side[0] > 0 && side[1] < 0 && side[2] < 0) {
            p[3] = new Vec3(
                    p[0].x() - side[0] * (p[2].x() - p[0].x()) / (side[2] - side[0]),
                    p[0].y() - side[0] * (p[2].y() - p[0].y()) / (side[2] - side[0]),
                    p[0].z() - side[0] * (p[2].z() - p[0].z()) / (side[2] - side[0]));

            p[0] = new Vec3(
                    p[0].x() - side[0] * (p[1].x() - p[0].x()) / (side[1] - side[0]),
                    p[0].y() - side[0] * (p[1].y() - p[0].y()) / (side[1] - side[0]),
                    p[0].z() - side[0] * (p[1].z() - p[0].z()) / (side[1] - side[0]));
            return 4;
        }

        /* Is p1 the only point on the clipped side */
        if (side[1] > 0 && side[0] < 0 && side[2] < 0) {
            p[3] = new Vec3(p[2]);
            p[2] = new Vec3(
                    p[1].x() - side[1] * (p[2].x() - p[1].x()) / (side[2] - side[1]),
                    p[1].y() - side[1] * (p[2].y() - p[1].y()) / (side[2] - side[1]),
                    p[1].z() - side[1] * (p[2].z() - p[1].z()) / (side[2] - side[1]));

            p[1] = new Vec3(
                    p[1].x() - side[1] * (p[0].x() - p[1].x()) / (side[0] - side[1]),
                    p[1].y() - side[1] * (p[0].y() - p[1].y()) / (side[0] - side[1]),
                    p[1].z() - side[1] * (p[0].z() - p[1].z()) / (side[0] - side[1]));
            return 4;
        }

        /* Is p2 the only point on the clipped side */
        if (side[2] > 0 && side[0] < 0 && side[1] < 0) {
            p[3] = new Vec3(
                    p[2].x() - side[2] * (p[0].x() - p[2].x()) / (side[0] - side[2]),
                    p[2].y() - side[2] * (p[0].y() - p[2].y()) / (side[0] - side[2]),
                    p[2].z() - side[2] * (p[0].z() - p[2].z()) / (side[0] - side[2]));

            p[2] = new Vec3(
                    p[2].x() - side[2] * (p[1].x() - p[2].x()) / (side[1] - side[2]),
                    p[2].y() - side[2] * (p[1].y() - p[2].y()) / (side[1] - side[2]),
                    p[2].z() - side[2] * (p[1].z() - p[2].z()) / (side[1] - side[2]));
            return 4;
        }

        /* Is p0 the only point on the not-clipped side */
        if (side[0] < 0 && side[1] > 0 && side[2] > 0) {
            p[1] = new Vec3(
                    p[0].x() - side[0] * (p[1].x() - p[0].x()) / (side[1] - side[0]),
                    p[0].y() - side[0] * (p[1].y() - p[0].y()) / (side[1] - side[0]),
                    p[0].z() - side[0] * (p[1].z() - p[0].z()) / (side[1] - side[0]));

            p[2] = new Vec3(
                    p[0].x() - side[0] * (p[2].x() - p[0].x()) / (side[2] - side[0]),
                    p[0].y() - side[0] * (p[2].y() - p[0].y()) / (side[2] - side[0]),
                    p[0].z() - side[0] * (p[2].z() - p[0].z()) / (side[2] - side[0]));
            return 3;
        }

        /* Is p1 the only point on the not-clipped side */
        if (side[1] < 0 && side[0] > 0 && side[2] > 0) {
            p[0] = new Vec3(
                    p[1].x() - side[1] * (p[0].x() - p[1].x()) / (side[0] - side[1]),
                    p[1].y() - side[1] * (p[0].y() - p[1].y()) / (side[0] - side[1]),
                    p[1].z() - side[1] * (p[0].z() - p[1].z()) / (side[0] - side[1]));

            p[2] = new Vec3(
                    p[1].x() - side[1] * (p[2].x() - p[1].x()) / (side[2] - side[1]),
                    p[1].y() - side[1] * (p[2].y() - p[1].y()) / (side[2] - side[1]),
                    p[1].z() - side[1] * (p[2].z() - p[1].z()) / (side[2] - side[1]));
            return 3;
        }

        /* Is p2 the only point on the not-clipped side */
        if (side[2] < 0 && side[0] > 0 && side[1] > 0) {
            p[1] = new Vec3(
                    p[2].x() - side[2] * (p[1].x() - p[2].x()) / (side[1] - side[2]),
                    p[2].y() - side[2] * (p[1].y() - p[2].y()) / (side[1] - side[2]),
                    p[2].z() - side[2] * (p[1].z() - p[2].z()) / (side[1] - side[2]));

            p[0] = new Vec3(
                    p[2].x() - side[2] * (p[0].x() - p[2].x()) / (side[0] - side[2]),
                    p[2].y() - side[2] * (p[0].y() - p[2].y()) / (side[0] - side[2]),
                    p[2].z() - side[2] * (p[0].z() - p[2].z()) / (side[0] - side[2]));
            return 3;
        }

        /* Shouldn't get here */
        throw new IllegalStateException();
    }

    @Override
    public String toString() {
        return "{normal=" + normal.toString() + ", point=" + point.toString() + ", equation=" + A + "x + " + B + "y + " + C + "z + " + D + " = 0}";
    }
}
