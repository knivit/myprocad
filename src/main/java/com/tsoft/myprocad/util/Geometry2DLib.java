package com.tsoft.myprocad.util;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.List;

/**
 * Useful links:
 * 1) http://geomalgorithms.com/
 * 2) http://paulbourke.net/geometry
 */
public class Geometry2DLib {
    /**
     * Source: http://paulbourke.net/geometry/polygonmesh/source2.c
     * Return whether a polygon in 2D is concave or convex
     *
     * It is assumed that the polygon is simple (does not intersect itself or have holes)
    */
    public enum PolygonType { CONVEX, CONCAVE, UNKNOWN }

    public static PolygonType getPolygonType(List<Point2D> points) {
        int n = points.size();
        if (n < 3) return PolygonType.UNKNOWN;

        int flag = 0;
        for (int i = 0; i < points.size(); i++) {
            int j = (i + 1) % n;
            int k = (i + 2) % n;

            double z = (points.get(j).getX() - points.get(i).getX()) * (points.get(k).getY() - points.get(j).getY());
            z -= (points.get(j).getY() - points.get(i).getY()) * (points.get(k).getX() - points.get(j).getX());
            if (z < 0) flag |= 1; else
            if (z > 0) flag |= 2;

            if (flag == 3) return PolygonType.CONCAVE;
        }
        if (flag != 0) return PolygonType.CONVEX;
        return PolygonType.UNKNOWN;
    }

    /**
     * Source: http://paulbourke.net/geometry/polygonmesh
     * Return whether the given point is inside or outside the polygon
     *
     * Solution forwarded by Philippe Reverdy is to compute the sum of the angles
     * made between the test point and each pair of points making up the polygon.
     * If this sum is 2pi then the point is an interior point, if 0 then the point is an exterior point.
     * This also works for polygons with holes given the polygon is defined with a path made up of
     * coincident edges into and out of the hole as is common practice in many CAD packages.
     */
    public enum PointPosition { INSIDE, OUTSIDE };

    public static PointPosition getPointPositionForPolygon(List<Point2D> points, Point2D p) {
        double angle=0;

        int n = points.size();
        for (int i = 0; i < n; i++) {
            int k = (i + 1) % n;
            double x1 = points.get(i).getX() - p.getX();
            double y1 = points.get(i).getY() - p.getY();
            double x2 = points.get(k).getX() - p.getX();
            double y2 = points.get(k).getY() - p.getY();

            angle += getAngleBetweenTwoVectors(x1, y1, x2, y2);
        }

        if (Math.abs(angle) < Math.PI) return PointPosition.OUTSIDE;
        return PointPosition.INSIDE;
    }

    /**
     * Source: http://paulbourke.net/geometry/polygonmesh
     * Return the angle between two vectors on a plane
     *
     * The angle is from vector 1 to vector 2, positive anticlockwise
     * The result is between -pi -> pi
    */
    private static double getAngleBetweenTwoVectors(double x1, double y1, double x2, double y2) {
        double theta1 = Math.atan2(y1, x1);
        double theta2 = Math.atan2(y2, x2);
        double dtheta = theta2 - theta1;

        while (dtheta > Math.PI) dtheta -= Math.PI * 2;
        while (dtheta < -Math.PI) dtheta += Math.PI * 2;
        return dtheta;
    }

    public static double getAngleBetweenTwoSections(double ax1, double ay1, double ax2, double ay2,
                                                    double bx1, double by1, double bx2, double by2) {
        if (ax1 > ax2) { double t = ax1; ax1 = ax2 ; ax2 = t; }
        if (ay1 > ay2) { double t = ay1; ay1 = ay2 ; ay2 = t; }
        if (bx1 > bx2) { double t = bx1; bx1 = bx2 ; bx2 = t; }
        if (by1 > by2) { double t = by1; by1 = by2 ; by2 = t; }

        // MyProCAD have OY axe from top to bottom
        return getAngleBetweenTwoVectors(ax2-ax1, -(ay2-ay1), bx2-bx1, -(by2-by1));
    }

    /**
     * Function to calculate the area of a polygon, according to the algorithm
     * defined at http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     * Return area of the polygon
     */
    public static double getPolygonArea(List<Point2D> points) {
        int n = points.size();
        double area = 0;

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            area += points.get(i).getX() * points.get(j).getY();
            area -= points.get(j).getX() * points.get(i).getY();
        }
        area /= 2.0;
        return area;
    }

    /**
     * Function to calculate the center of mass for a given polygon, according
     * ot the algorithm defined at
     * http://local.wasp.uwa.edu.au/~pbourke/geometry/polyarea/
     *
     * Return point that is the center of mass
     */
    public static Point2D getCenterOfMass(List<Point2D> points) {
        int n = points.size();

        double factor;
        double cx = 0, cy = 0;
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            factor = (points.get(i).getX() * points.get(j).getY() - points.get(j).getX() * points.get(i).getY());
            cx += (points.get(i).getX() + points.get(j).getX()) * factor;
            cy += (points.get(i).getY() + points.get(j).getY()) * factor;
        }

        double area = getPolygonArea(points) * 6.0f;
        factor = 1 / area;
        cx *= factor;
        cy *= factor;

        return new Point2D.Double(cx, cy);
    }

    /**
     * Source: http://paulbourke.net/geometry/pointlineplane/DistancePoint.java
     * Returns the distance of p3 to the segment defined by p1,p2;
     *
     * @param line segment
     * @param p Point to which we want to know the distance of the segment defined by p1,p2
     * @return The distance of p to the segment defined by p1, p2
     */
    public static double getMinimalDistanceToLine(Line2D line, Point2D p) {
        double xDelta = line.getX2() - line.getX1();
        double yDelta = line.getY2() - line.getY1();

        // Check is it a point
        if ((xDelta == 0) && (yDelta == 0)) return 0;

        double u = ((p.getX() - line.getX1()) * xDelta + (p.getY() - line.getY1()) * yDelta) / (xDelta * xDelta + yDelta * yDelta);

        final Point2D closestPoint;
        if (u < 0) closestPoint = line.getP1();
        else if (u > 1) closestPoint = line.getP2();
        else closestPoint = new Point2D.Double(line.getX1() + u * xDelta, line.getY1() + u * yDelta);

        return closestPoint.distance(p);
    }

    /**
     * Determine the intersection point of two line segments
     * Return null if the lines don't intersect
     */
    public static Point2D getLinesIntersectionPoint(Line2D L1, Line2D L2) {
        // Denominator for ua and ub are the same, so store this calculation
        double d = (L2.getY2() - L2.getY1()) * (L1.getX2() - L1.getX1())
                 - (L2.getX2() - L2.getX1()) * (L1.getY2() - L1.getY1());

        // n_a and n_b are calculated as separate values for readability
        double n_a = (L2.getX2() - L2.getX1()) * (L1.getY1() - L2.getY1())
                   - (L2.getY2() - L2.getY1()) * (L1.getX1() - L2.getX1());

        double n_b = (L1.getX2() - L1.getX1()) * (L1.getY1() - L2.getY1())
                   - (L1.getY2() - L1.getY1()) * (L1.getX1() - L2.getX1());

        // Make sure there is not a division by zero - this also indicates that
        // the lines are parallel.
        // If n_a and n_b were both equal to zero the lines would be on top of each
        // other (coincidental).  This check is not done because it is not
        // necessary for this implementation (the parallel check accounts for this).
        if (d == 0) return null;

        // Calculate the intermediate fractional point that the lines potentially intersect.
        double ua = n_a / d;
        double ub = n_b / d;

        // The fractional point will be between 0 and 1 inclusive if the lines
        // intersect.  If the fractional calculation is larger than 1 or smaller
        // than 0 the lines would need to be longer to intersect.
        if (ua >= 0.0 && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
            double x = L1.getX1() + (ua * (L1.getX2() - L1.getX1()));
            double y = L1.getY1() + (ua * (L1.getY2() - L1.getY1()));
            return new Point2D.Double(x, y);
        }
        return null;
    }
}
