package com.tsoft.myprocad.util.linealg;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class Seg2Test {
    @Test
    public void intersectionPoint() {
        Seg2 seg1 = new Seg2(new Vec2(0, 0), new Vec2(100, 0));
        Seg2 seg2 = new Seg2(new Vec2(50, -100), new Vec2(50, 100));
        assertEquals(new Vec2(50, 0), seg1.getIntersectionPoint(seg2));
    }
}
