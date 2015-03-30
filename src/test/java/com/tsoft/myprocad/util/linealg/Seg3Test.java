package com.tsoft.myprocad.util.linealg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Seg3Test {
    @Test
    public void distanceToPoint() {
        Seg3 seg1 = new Seg3(new Vec3(0, 0, 0), new Vec3(100, 0, 0));
        assertEquals(10, seg1.getDistanceToPoint(new Vec3(0, 10, 0)), 0.00001);
        assertEquals(10, seg1.getDistanceToPoint(new Vec3(0, 0, 10)), 0.00001);
    }

    @Test
    public void projectionOnPlane() {
        Seg3 seg1 = new Seg3(new Vec3(0, 0, 0), new Vec3(100, 0, 100));
        assertEquals(new Seg2(new Vec2(0, 0), new Vec2(100, 0)), seg1.get2dProjectionOnPlane(Plane.XOY));

        Seg3 seg2 = new Seg3(new Vec3(0, 0, 0), new Vec3(100, 100, 100));
        assertEquals(new Seg2(new Vec2(0, 0), new Vec2(100, 100)), seg2.get2dProjectionOnPlane(Plane.XOY));
    }
}
