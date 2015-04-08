package com.tsoft.myprocad.util.linealg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PlaneTest {
    @Test
    public void planeBy3Points() {
        Plane plane = new Plane(new Vec3(-1, 1, -1), new Vec3(0, 1, 1), new Vec3(4, -3, 2));

        assertEquals(8, plane.A(), 0.0001);
        assertEquals(7, plane.B(), 0.0001);
        assertEquals(-4, plane.C(), 0.0001);
        assertEquals(-3, plane.D(), 0.0001);
    }
}
