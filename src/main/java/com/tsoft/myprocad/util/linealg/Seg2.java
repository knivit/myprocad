package com.tsoft.myprocad.util.linealg;

/** A segment in a 2D space */
public class Seg2 {
    private Vec2 p0;
    private Vec2 p1;

    // calculated
    private Vec2 direction;

    public Seg2(Vec2 p0, Vec2 p1) {
        set(p0, p1);
    }

    public Vec2 p0() { return p0; }

    public Vec2 p1() { return p1; }

    public void set(Vec2 p0, Vec2 p1) {
        this.p0 = new Vec2(p0);
        this.p1 = new Vec2(p1);
        recalc();
    }

    private void recalc() {
        direction = p1.minus(p0);
        direction.normalize();
    }

    /**
     * Determine the intersection point of two line segments
     * Return null if the lines don't intersect
     */
    public Vec2 getIntersectionPoint(Seg2 seg) {
        // Denominator for ua and ub are the same, so store this calculation
        float d = (seg.p1.y() - seg.p0.y()) * (p1.x() - p0.x())
                - (seg.p1.x() - seg.p0.x()) * (p1.y() - p0.y());

        // n_a and n_b are calculated as separate values for readability
        float n_a = (seg.p1.x() - seg.p0.x()) * (p0.y() - seg.p0.y())
                - (seg.p1.y() - seg.p0.y()) * (p0.x() - seg.p0.x());

        float n_b = (p1.x() - p0.x()) * (p0.y() - seg.p0.y())
                - (p1.y() - p0.y()) * (p0.x() - seg.p0.x());

        // Make sure there is not a division by zero - this also indicates that
        // the lines are parallel.
        // If n_a and n_b were both equal to zero the lines would be on top of each
        // other (coincidental).  This check is not done because it is not
        // necessary for this implementation (the parallel check accounts for this).
        if (d == 0) return null;

        // Calculate the intermediate fractional point that the lines potentially intersect.
        float ua = n_a / d;
        float ub = n_b / d;

        // The fractional point will be between 0 and 1 inclusive if the lines
        // intersect.  If the fractional calculation is larger than 1 or smaller
        // than 0 the lines would need to be longer to intersect.
        if (ua >= 0.0 && ua <= 1.0 && ub >= 0.0 && ub <= 1.0) {
            float x = p0.x() + (ua * (p1.x() - p0.x()));
            float y = p0.y() + (ua * (p1.y() - p0.y()));
            return new Vec2(x, y);
        }
        return null;
    }

    @Override
    public String toString() {
        return "{p0=" + p0.toString() + ", p1=" + p1.toString() + ", direction=" + direction.toString() + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Seg2 seg2 = (Seg2) o;

        if (!p0.equals(seg2.p0)) return false;
        return p1.equals(seg2.p1);

    }

    @Override
    public int hashCode() {
        int result = p0.hashCode();
        result = 31 * result + p1.hashCode();
        return result;
    }
}
