package com.tsoft.myprocad.util.linealg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaneTest {
    @Test
    public void planeBy3Points() {
        Plane plane = new Plane(new Vec3(-1, 1, -1), new Vec3(0, 1, 1), new Vec3(4, -3, 2));

        // koeff may be multiplied by the same value, this is OK
        float off = plane.A() / 8;

        assertEquals(8*off, plane.A(), 0.0001);
        assertEquals(7*off, plane.B(), 0.0001);
        assertEquals(-4*off, plane.C(), 0.0001);
        assertEquals(-3*off, plane.D(), 0.0001);
    }

    @Test
    public void getAngle() {
        Plane p1 = new Plane(1, 1, 1, -1);
        Plane p2 = new Plane(1, -2, 3, -1);
        assertEquals(Math.toRadians(72), p2.getAngle(p1), 0.01);
    }
}
